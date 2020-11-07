/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frsmanagementclient;

import ejb.session.stateless.AircraftConfigurationEntitySessionBeanRemote;
import ejb.session.stateless.AircraftTypeEntitySessionBeanRemote;
import ejb.session.stateless.AirportEntitySessionBeanRemote;
import ejb.session.stateless.CabinClassConfigurationSessionBeanRemote;
import ejb.session.stateless.FlightEntitySessionBeanRemote;
import ejb.session.stateless.FlightRouteEntitySessionBeanRemote;
import ejb.session.stateless.FlightScheduleEntitySessionBeanRemote;
import ejb.session.stateless.FlightSchedulePlanEntitySessionBeanRemote;
import entity.AircraftConfigurationEntity;
import entity.AirportEntity;
import entity.CabinClassConfigurationEntity;
import entity.FlightEntity;
import entity.FlightRouteEntity;
import entity.FlightScheduleEntity;
import entity.FlightSchedulePlanEntity;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import util.enumeration.FlightSchedulePlanTypeEnum;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.AirportNotFoundException;
import util.exception.FlightNotFoundException;
import util.exception.FlightRouteDisabled;
import util.exception.FlightRouteNotFoundException;

/**
 *
 * @author Chrisya
 */
public class FlightOperationModule {

    private AirportEntitySessionBeanRemote airportEntitySessionBeanRemote;

    private AircraftTypeEntitySessionBeanRemote aircraftTypeEntitySessionBeanRemote;

    private CabinClassConfigurationSessionBeanRemote cabinClassConfigurationSessionBeanRemote;

    private AircraftConfigurationEntitySessionBeanRemote aircraftConfigurationEntitySessionBeanRemote;

    private FlightEntitySessionBeanRemote flightEntitySessionBeanRemote;

    private FlightRouteEntitySessionBeanRemote flightRouteEntitySessionBeanRemote;

    private FlightScheduleEntitySessionBeanRemote flightScheduleEntitySessionBeanRemote;

    private FlightSchedulePlanEntitySessionBeanRemote flightSchedulePlanEntitySessionBeanRemote;

    public FlightOperationModule(AirportEntitySessionBeanRemote airportEntitySessionBeanRemote,
            AircraftTypeEntitySessionBeanRemote aircraftTypeEntitySessionBeanRemote,
            CabinClassConfigurationSessionBeanRemote cabinClassConfigurationSessionBeanRemote,
            AircraftConfigurationEntitySessionBeanRemote aircraftConfigurationEntitySessionBeanRemote,
            FlightEntitySessionBeanRemote flightEntitySessionBeanRemote,
            FlightRouteEntitySessionBeanRemote flightRouteEntitySessionBeanRemote,
            FlightScheduleEntitySessionBeanRemote flightScheduleEntitySessionBeanRemote,
            FlightSchedulePlanEntitySessionBeanRemote flightSchedulePlanEntitySessionBeanRemote) {
        this.airportEntitySessionBeanRemote = airportEntitySessionBeanRemote;
        this.aircraftTypeEntitySessionBeanRemote = aircraftTypeEntitySessionBeanRemote;
        this.cabinClassConfigurationSessionBeanRemote = cabinClassConfigurationSessionBeanRemote;
        this.aircraftConfigurationEntitySessionBeanRemote = aircraftConfigurationEntitySessionBeanRemote;
        this.flightEntitySessionBeanRemote = flightEntitySessionBeanRemote;
        this.flightRouteEntitySessionBeanRemote = flightRouteEntitySessionBeanRemote;
        this.flightScheduleEntitySessionBeanRemote = flightScheduleEntitySessionBeanRemote;
        this.flightSchedulePlanEntitySessionBeanRemote = flightSchedulePlanEntitySessionBeanRemote;
    }

    public void createFlight(Scanner sc) {
        System.out.println("*** FRS Schedule Manager :: Create Flight ***");
        sc.nextLine();

        System.out.println("Enter flight number> ");
        int flightNumber = sc.nextInt();

        System.out.println("Enter origin airport> ");
        String origin = sc.nextLine();
        AirportEntity originAirport = null;
        try {
            originAirport = airportEntitySessionBeanRemote.retrieveAirportByCode(origin);
        } catch (AirportNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("Enter destination airport> ");
        String destination = sc.nextLine();
        AirportEntity destinationAirport = null;
        try {
            destinationAirport = airportEntitySessionBeanRemote.retrieveAirportByCode(destination);
        } catch (AirportNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        FlightRouteEntity route = null;
        try {
            route = flightRouteEntitySessionBeanRemote.findFlightRoute(originAirport, destinationAirport);
            boolean disabled = flightRouteEntitySessionBeanRemote.isDisabled(route);
        } catch (FlightRouteNotFoundException | FlightRouteDisabled ex) {
            System.out.println(ex.getMessage());
        }

        System.out.print("Enter aircraft configuration code> ");
        String code = sc.nextLine();

        AircraftConfigurationEntity aircraftConfiguration = null;
        try {
            aircraftConfiguration = aircraftConfigurationEntitySessionBeanRemote.retrieveAircraftTypeByCode(code);
        } catch (AircraftConfigurationNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        FlightEntity departureFlight = flightEntitySessionBeanRemote.createFlightEntity(new FlightEntity(flightNumber, aircraftConfiguration, route));
        FlightEntity returnFlight = null;
        System.out.println("Flight " + flightNumber + " from " + origin + " to " + destination + " has been created!");

        boolean returnRouteAvailability = flightRouteEntitySessionBeanRemote.checkReturnRouteAvailability(route);
        String returnFlightConfirmation = "A";

        if (returnRouteAvailability) {
            while (!returnFlightConfirmation.equals("Y") && !returnFlightConfirmation.equals("N")) {
                System.out.println("There is a complementary return route. Do you want to create a complementary return flight? (Y/N)> ");
                returnFlightConfirmation = sc.nextLine();
            }
        }

        if (returnFlightConfirmation.equals("Y")) {
            System.out.println("Enter flight number> ");
            int returnFlightNumber = sc.nextInt();
            FlightRouteEntity returnRoute = route.getReturnFlightRoute();

            flightEntitySessionBeanRemote.createFlightEntity(new FlightEntity(returnFlightNumber, aircraftConfiguration, returnRoute));
            flightEntitySessionBeanRemote.setReturnFlight(departureFlight, returnFlight);
            flightEntitySessionBeanRemote.setDepartureFlight(returnFlight, departureFlight);

            System.out.println("Return flight " + flightNumber + " from " + destination + " to " + origin + " has been created!");
        }
    }

    public void viewAllFlights(Scanner sc) {
        System.out.println("*** FRS Schedule Manager :: View All Flights ***");
        sc.nextLine();
        List<FlightEntity> flights
                = flightEntitySessionBeanRemote.retrieveAllFlights();
        for (int i = 1; i <= flights.size(); i++) {
            if (flights.get(i - 1).getDepartureFlight() == null) {
                System.out.print(i + ". ");
                System.out.println("FLIGHT: " + flights.get(i - 1).getFlightCode() + " with route " + flights.get(i - 1).getRoute().getOriginAirport() + " to " + flights.get(i - 1).getRoute().getDestinationAirport());

                if (flights.get(i - 1).getReturnFlight() != null) {
                    FlightEntity returnFlight = flights.get(i - 1).getReturnFlight();
                    System.out.println("RETURN FLIGHT: " + returnFlight.getFlightCode() + " with route " + returnFlight.getRoute().getOriginAirport() + " to " + returnFlight.getRoute().getDestinationAirport());
                }
            }
        }
    }

    public void viewFlightDetails(Scanner sc) {
        System.out.println("*** FRS Schedule Manager :: View Flight Details ***");
        sc.nextLine();

        System.out.println("Enter flight code>");
        String flightCode = sc.nextLine();

        FlightEntity flight = null;

        try {
            flight = flightEntitySessionBeanRemote.retrieveFlightByCode(flightCode);
        } catch (FlightNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        AircraftConfigurationEntity aircraftConfiguration = flight.getAircraftConfigurationEntity();
        FlightRouteEntity route = flight.getRoute();
        String origin = route.getOriginAirport().getAirportCode();
        String destination = route.getDestinationAirport().getAirportCode();
        int maxCapacity = aircraftConfiguration.getType().getMaxCapacity();
        List<CabinClassConfigurationEntity> cabinClasses = aircraftConfiguration.getCabinClassConfigurationEntitys();

        System.out.println("FLIGHT " + flight.getFlightCode() + ": ");
        System.out.println("From: " + origin + " to: " + destination);
        System.out.println("Total maximum capacity: " + maxCapacity);
        System.out.println("Available cabin classes: ");
        for (int i = 1; i <= cabinClasses.size(); i++) {
            System.out.println("/t" + i + ". " + cabinClasses.get(i - 1).getType().toString());
            System.out.println("/t" + "Available capacity: " + cabinClasses.get(i - 1).getMaxCapacity());
        }
    }

    public FlightScheduleEntity createFlightSchedule(Scanner sc, DateTimeFormatter dateFormat) {

        System.out.println("Enter departure date and time (DD MM YYYY HH:MM)>");
        String departureDate = sc.nextLine();

        System.out.println("Enter duration of flight (HH:MM)>");
        String duration = sc.nextLine();
        String durationHour = duration.substring(0, 1);
        String durationMin = duration.substring(3, 4);

        int durationHourInt = Integer.parseInt(durationHour);
        int durationMinInt = Integer.parseInt(durationMin);
        int totalDuration = durationMinInt + (60 * durationHourInt);

        ZonedDateTime departureDateTime = ZonedDateTime.parse(departureDate);
        ZonedDateTime arrivalDateTime = departureDateTime.plusMinutes(totalDuration);
        String arrDateTime = arrivalDateTime.format(dateFormat);

        System.out.println("Expected arrival date and time: " + arrDateTime);

        FlightScheduleEntity flightSchedule = flightScheduleEntitySessionBeanRemote.createFlightScheduleEntity(new FlightScheduleEntity(departureDate, totalDuration, arrDateTime));

        flightScheduleEntitySessionBeanRemote.associateNewSeatsInventory(flightSchedule);

        return flightSchedule;
    }

    public FlightScheduleEntity createReturnFlightSchedule(Scanner sc, DateTimeFormatter dateFormat, int totalLayoverDuration, FlightScheduleEntity departureFlight) {

        String arrDateTime = departureFlight.getArrivalDateTime();
        int duration = departureFlight.getDuration();

        ZonedDateTime returnDepartureDateTime = ZonedDateTime.parse(arrDateTime).plusMinutes(totalLayoverDuration);
        String returnDepDateTime = returnDepartureDateTime.format(dateFormat);
        System.out.println("Return departure time: " + returnDepDateTime);

        ZonedDateTime returnArrivalDateTime = returnDepartureDateTime.plusMinutes(duration);
        String returnArrDateTime = returnArrivalDateTime.format(dateFormat);
        System.out.println("Expected return arrival time: " + returnArrDateTime);

        FlightScheduleEntity returnFlightSchedule = flightScheduleEntitySessionBeanRemote.createFlightScheduleEntity(new FlightScheduleEntity(returnDepDateTime, duration, returnArrDateTime));

        flightScheduleEntitySessionBeanRemote.associateReturnSchedule(departureFlight, returnFlightSchedule);
        flightScheduleEntitySessionBeanRemote.associateNewSeatsInventory(returnFlightSchedule);

        return returnFlightSchedule;
    }

    public int scanLayoverTime(Scanner sc) {
        System.out.println("Enter layover duration (HH:MM)>");
        String layoverDuration = sc.nextLine();
        String layoverDurationHour = layoverDuration.substring(0, 1);
        String layoverDurationMin = layoverDuration.substring(3, 4);

        int layoverDurationHourInt = Integer.parseInt(layoverDurationHour);
        int layoverDurationMinInt = Integer.parseInt(layoverDurationMin);
        return layoverDurationMinInt + (60 * layoverDurationHourInt);
    }

    public void createFlightSchedulePlan(Scanner sc) {
        System.out.println("*** FRS Schedule Manager :: Create Schedule Plan ***");
        sc.nextLine();

        System.out.println("Enter flight code>");
        String flightCode = sc.nextLine();
        FlightEntity flight = null;
        try {
            flight = flightEntitySessionBeanRemote.retrieveFlightByCode(flightCode);
        } catch (FlightNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        //List<CabinClassConfigurationEntity> cabinClassList = flight.getAircraftConfigurationEntity().getCabinClassConfigurationEntitys();
        System.out.println("Select schedule plan type>");
        System.out.println("1: Single schedule");
        System.out.println("2: Multiple schedules");
        System.out.println("3: Recurrent daily schedules");
        System.out.println("4: Recurrent weekly schedules");

        int typeResponse = sc.nextInt();
        String datePattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(datePattern);

        if (typeResponse == 1) {
            FlightSchedulePlanTypeEnum type = FlightSchedulePlanTypeEnum.SINGLE;

            FlightScheduleEntity flightSchedule = this.createFlightSchedule(sc, dateFormat);
            FlightScheduleEntity returnFlightSchedule = null;
            int totalLayoverDuration = 0;

            String startDate = flightSchedule.getDepartureDateTime().substring(0, 9);

            if (flight.getReturnFlight() != null) {
                System.out.println("There is a return flight for this route. Do you want to create return flight schedule? (Y/N)>");
                String response = sc.nextLine();

                if (response.equals("Y")) {

                    totalLayoverDuration = this.scanLayoverTime(sc);
                    returnFlightSchedule = this.createReturnFlightSchedule(sc, dateFormat, totalLayoverDuration, flightSchedule);
                }
            }

            FlightSchedulePlanEntity schedulePlan = null;

            if (returnFlightSchedule == null) {

                schedulePlan = flightSchedulePlanEntitySessionBeanRemote.createFlightSchedulePlanEntity(new FlightSchedulePlanEntity(type, startDate, 0, flight));
                flightScheduleEntitySessionBeanRemote.associateWithPlan(flightSchedule, schedulePlan);

            } else {

                schedulePlan = flightSchedulePlanEntitySessionBeanRemote.createFlightSchedulePlanEntity(new FlightSchedulePlanEntity(type, startDate, totalLayoverDuration, flight));

                flightScheduleEntitySessionBeanRemote.associateWithPlan(flightSchedule, schedulePlan);
                flightScheduleEntitySessionBeanRemote.associateWithPlan(returnFlightSchedule, schedulePlan);

            }
        } else if (typeResponse == 2) {
            FlightSchedulePlanTypeEnum type = FlightSchedulePlanTypeEnum.MULTIPLE;

            System.out.println("Enter number of schedule>");
            int numSchedule = sc.nextInt();

            System.out.println("Enter starting date (yyyy-mm-dd)>");
            String startDate = sc.nextLine();

            FlightSchedulePlanEntity schedulePlan = flightSchedulePlanEntitySessionBeanRemote.createFlightSchedulePlanEntity(new FlightSchedulePlanEntity(type, startDate, 0, flight));

            for (int i = 0; i < numSchedule; i++) {
                FlightScheduleEntity flightSchedule = this.createFlightSchedule(sc, dateFormat);
                FlightScheduleEntity returnFlightSchedule = null;

                int totalLayoverDuration = 0;

                if (flight.getReturnFlight() != null) {
                    System.out.println("There is a return flight for this route. Do you want to create return flight schedule? (Y/N)>");
                    String response = sc.nextLine();

                    if (response.equals("Y")) {

                        totalLayoverDuration = this.scanLayoverTime(sc);
                        returnFlightSchedule = this.createReturnFlightSchedule(sc, dateFormat, totalLayoverDuration, flightSchedule);
                    }
                }

                if (returnFlightSchedule == null) {

                    flightScheduleEntitySessionBeanRemote.associateWithPlan(flightSchedule, schedulePlan);

                } else {

                    flightScheduleEntitySessionBeanRemote.associateWithPlan(flightSchedule, schedulePlan);
                    flightScheduleEntitySessionBeanRemote.associateWithPlan(returnFlightSchedule, schedulePlan);

                }
            }
        } else if (typeResponse == 3) {
            FlightSchedulePlanTypeEnum type = FlightSchedulePlanTypeEnum.RECURRENT_DAY;

            System.out.println("Enter starting date (yyyy-mm-dd)>");
            String startDate = sc.nextLine();
            
            System.out.println("Enter ending date (yyyy-mm-dd)>");
            String endDate = sc.nextLine();

            System.out.println("Enter number of recurring days>");
            int recurringDay = sc.nextInt();

            System.out.println("Enter departure hour (HH:mm)>");
            String departuretime = sc.nextLine();

            System.out.println("Enter duration of flight (HH:MM)>");
            String duration = sc.nextLine();

            FlightSchedulePlanEntity schedulePlan = flightSchedulePlanEntitySessionBeanRemote.createFlightSchedulePlanEntity(new FlightSchedulePlanEntity(type, startDate, endDate, 0, flight));

            if (flight.getReturnFlight() != null) {
                System.out.println("There is a return flight for this route. Do you want to create return flight schedule? (Y/N)>");
                String response = sc.nextLine();

                if (response.equals("Y")) {
                    int totalLayoverDuration = this.scanLayoverTime(sc);
                    flightScheduleEntitySessionBeanRemote.createRecurrentSchedule(schedulePlan, startDate, endDate, recurringDay, departuretime, dateFormat, duration, true, totalLayoverDuration);
                } else {
                    flightScheduleEntitySessionBeanRemote.createRecurrentSchedule(schedulePlan, startDate, endDate, recurringDay, departuretime, dateFormat, duration, false, 0);
                }
            }
        } else if (typeResponse == 4) {
        }

    }
}
