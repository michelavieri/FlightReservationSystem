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
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.enumeration.CabinClassTypeEnum;
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

    public String searchCabinType(CabinClassTypeEnum type) {
        if (type.equals(CabinClassTypeEnum.FIRST_CLASS)) {
            return "First Class";
        } else if (type.equals(CabinClassTypeEnum.BUSINESS_CLASS)) {
            return "Business Class";
        } else if (type.equals(CabinClassTypeEnum.PREMIUM_ECONOMY_CLASS)) {
            return "Premium Economy Class";
        } else if (type.equals(CabinClassTypeEnum.ECONOMY_CLASS)) {
            return "Economy Class";
        }
        return "invalid";
    }

    public void createFlight(Scanner sc) {
        System.out.println("*** FRS Schedule Manager :: Create Flight ***");
        sc.nextLine();

        System.out.print("Enter flight number ML> ");
        int flightNumber = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter origin airport code> ");
        String origin = sc.nextLine();
        AirportEntity originAirport = null;
        try {
            originAirport = airportEntitySessionBeanRemote.retrieveAirportByCode(origin);
        } catch (AirportNotFoundException ex) {
            System.out.println(ex.getMessage());
            return;
        }

        System.out.print("Enter destination airport code> ");
        String destination = sc.nextLine();
        AirportEntity destinationAirport = null;
        try {
            destinationAirport = airportEntitySessionBeanRemote.retrieveAirportByCode(destination);
        } catch (AirportNotFoundException ex) {
            System.out.println(ex.getMessage());
            return;
        }

        FlightRouteEntity route = null;
        try {
            route = flightRouteEntitySessionBeanRemote.retrieveRouteByAirport(originAirport, destinationAirport);
            boolean disabled = flightRouteEntitySessionBeanRemote.isDisabled(route);
            if (disabled) {
                throw new FlightRouteDisabled("Flight Route has been disabled!");
            }
        } catch (FlightRouteNotFoundException | FlightRouteDisabled ex) {
            System.out.println(ex.getMessage());
            return;
        }

        System.out.println("Here are the list of aircraft configurations code:");
        List<AircraftConfigurationEntity> configurations
                = aircraftConfigurationEntitySessionBeanRemote.retrieveAllAircraftConfigurations();
        for (int i = 1; i <= configurations.size(); i++) {
            System.out.print(i + ". ");
            System.out.println("CODE: " + configurations.get(i - 1).getCode());
        }

        System.out.print("Enter aircraft configuration code> ");
        String code = sc.nextLine();

        AircraftConfigurationEntity aircraftConfiguration = null;
        try {
            aircraftConfiguration = aircraftConfigurationEntitySessionBeanRemote.retrieveAircraftTypeByCode(code);
        } catch (AircraftConfigurationNotFoundException ex) {
            System.out.println(ex.getMessage());
            return;
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
            System.out.print("Enter return flight number ML> ");
            int returnFlightNumber = sc.nextInt();
            FlightRouteEntity returnRoute = route.getReturnFlightRoute();

            returnFlight = flightEntitySessionBeanRemote.createFlightEntity(new FlightEntity(returnFlightNumber, aircraftConfiguration, returnRoute));
            flightEntitySessionBeanRemote.setReturnFlight(departureFlight, returnFlight);
            flightEntitySessionBeanRemote.setDepartureFlight(returnFlight, departureFlight);

            System.out.println("Return flight " + returnFlightNumber + " from " + destination + " to " + origin + " has been created!");
        }
    }

    public void viewAllFlights(Scanner sc) {
        System.out.println("*** FRS Schedule Manager :: View All Flights ***");
        sc.nextLine();
        List<FlightEntity> flights
                = flightEntitySessionBeanRemote.retrieveAllFlights();
        if (flights.isEmpty()) {
            System.out.println("There are no flights registered");
            return;
        }
        for (int i = 1; i <= flights.size(); i++) {
            if (flights.get(i - 1).getDepartureFlight() == null) {
                System.out.print(i + ". ");
                System.out.println("FLIGHT: " + flights.get(i - 1).getFlightCode() + " with route " + flights.get(i - 1).getRoute().getOriginAirport().getAirportCode() + " to " + flights.get(i - 1).getRoute().getDestinationAirport().getAirportCode());

                if (flights.get(i - 1).getReturnFlight() != null) {
                    FlightEntity returnFlight = flights.get(i - 1).getReturnFlight();
                    System.out.println("   RETURN FLIGHT: " + returnFlight.getFlightCode() + " with route " + returnFlight.getRoute().getOriginAirport().getAirportCode() + " to " + returnFlight.getRoute().getDestinationAirport().getAirportCode());
                }
            }
        }
    }

    public void viewFlightDetails(Scanner sc) {
        System.out.println("*** FRS Schedule Manager :: View Flight Details ***");
        sc.nextLine();

        System.out.print("Enter flight code>");
        String flightCode = sc.nextLine();

        FlightEntity flight = null;

        try {
            flight = flightEntitySessionBeanRemote.retrieveFlightByCode(flightCode);
        } catch (FlightNotFoundException ex) {
            System.out.println(ex.getMessage());
            return;
        }

        if (flightEntitySessionBeanRemote.isReturnFlight(flight)) {
            System.out.println("Please select the departure flight, not the return flight!");
            return;
        }

        AircraftConfigurationEntity aircraftConfiguration = flight.getAircraftConfigurationEntity();
        FlightRouteEntity route = flight.getRoute();
        String origin = route.getOriginAirport().getAirportCode();
        String destination = route.getDestinationAirport().getAirportCode();
        int maxCapacity = aircraftConfiguration.getType().getMaxCapacity();
        List<CabinClassConfigurationEntity> cabinClasses = flightEntitySessionBeanRemote.retrieveCabinClassByFlight(flight);

        System.out.println("DEPARTURE FLIGHT " + flight.getFlightCode() + ": ");
        System.out.println("From: " + origin + " to: " + destination);
        System.out.println("Total maximum capacity: " + maxCapacity);
        System.out.println("Disabled: " + flight.isDisabled());
        System.out.println("Available cabin classes: ");
        for (int i = 1; i <= cabinClasses.size(); i++) {
            System.out.println("\t" + i + ". " + searchCabinType(cabinClasses.get(i - 1).getType()));
            System.out.println("\t" + "Available capacity: " + cabinClasses.get(i - 1).getMaxCapacity());
        }
        System.out.println();
        FlightEntity returnFlight = flight.getReturnFlight();

        if (returnFlight != null) {
            System.out.println("RETURN FLIGHT " + returnFlight.getFlightCode() + ":");
            System.out.println("From: " + returnFlight.getRoute().getOriginAirport().getAirportCode() + " to: " + returnFlight.getRoute().getDestinationAirport().getAirportCode());
            System.out.println("Total maximum capacity: " + returnFlight.getAircraftConfigurationEntity().getMaxCapacity());
            System.out.println("Disabled: " + returnFlight.isDisabled());
            System.out.println("Available cabin classes: ");
            for (int i = 1; i <= cabinClasses.size(); i++) {
                System.out.println("\t" + i + ". " + searchCabinType(cabinClasses.get(i - 1).getType()));
                System.out.println("\t" + "Available capacity: " + cabinClasses.get(i - 1).getMaxCapacity());
            }

        }

        String modificationConfirmation = "A";
        while (!modificationConfirmation.equals("Y") && !modificationConfirmation.equals("N")) {
            System.out.print("Do you want update/delete this flight? (Y/N)> ");
            modificationConfirmation = sc.nextLine();
        }

        if (modificationConfirmation.equals("N")) {
            return;
        } else {
            int response = 0;
            while (response < 1 || response > 2) {
                System.out.println("Please choose:");
                System.out.println("1: Update Flight");
                System.out.println("2: Delete Flight");
                System.out.print("> ");
                response = sc.nextInt();

                if (response == 1) {
                    System.out.println("*** Update Flight ***");
                    System.out.println("Flight's Aircraft Configuration Code: " + flight.getAircraftConfigurationEntity().getCode());
                    String updateConfirmation = "A";
                    sc.nextLine();
                    while (!updateConfirmation.equals("Y") && !updateConfirmation.equals("N")) {
                        System.out.print("Do you want to update the aircraft configuration? (Y/N)> ");
                        updateConfirmation = sc.nextLine();
                    }

                    if (updateConfirmation.equals("Y")) {
                        System.out.println("Here are the lists of aircraft configurations:");
                        List<AircraftConfigurationEntity> configurations
                                = aircraftConfigurationEntitySessionBeanRemote.retrieveAllAircraftConfigurations();
                        for (int i = 1; i <= configurations.size(); i++) {
                            System.out.print(i + ". ");
                            System.out.println("CODE: " + configurations.get(i - 1).getCode());
                        }

                        System.out.print("Enter the new aircraft configuration code> ");
                        String newCode = sc.nextLine();
                        AircraftConfigurationEntity newConfig = null;
                        try {
                            newConfig = aircraftConfigurationEntitySessionBeanRemote.retrieveAircraftTypeByCode(newCode);
                        } catch (AircraftConfigurationNotFoundException ex) {
                            System.out.println(ex.getMessage());
                            break;
                        }
                        flightEntitySessionBeanRemote.updateAircraftConfiguration(newConfig, flight);
                        System.out.println("The aircraft configuration for flight " + flight.getFlightCode()
                                + " has been successfully updated to " + newConfig.getCode());
                    }

                    System.out.println("Flight's Route: " + flight.getRoute().getOriginAirport().getAirportCode() + " - " + flight.getRoute().getDestinationAirport().getAirportCode());
                    updateConfirmation = "A";
                    while (!updateConfirmation.equals("Y") && !updateConfirmation.equals("N")) {
                        System.out.print("Do you want to update the flight's route? (Y/N)> ");
                        updateConfirmation = sc.nextLine();
                    }

                    if (updateConfirmation.equals("Y")) {
                        System.out.println("Here are the lists of available flight routes:");
                        List<FlightRouteEntity> routes
                                = flightRouteEntitySessionBeanRemote.retrieveAllAvailableRoutesNotReturn();
                        for (int i = 1; i <= routes.size(); i++) {
                            System.out.print(i + ". ");
                            System.out.println("ROUTE ID: " + routes.get(i - 1).getRouteId());
                            System.out.println("   ROUTE: " + routes.get(i - 1).getOriginAirport().getAirportCode() + " - "
                                    + routes.get(i - 1).getDestinationAirport().getAirportCode());
                        }

                        System.out.print("Enter the Route ID> ");
                        Long routeId = sc.nextLong();
                        FlightRouteEntity newRoute = null;
                        try {
                            newRoute = flightRouteEntitySessionBeanRemote.retrieveRouteById(routeId);

                            if (flightRouteEntitySessionBeanRemote.isReturnRoute(newRoute)) {
                                System.out.println("Do not choose a return route!");
                                return;
                            }
                        } catch (FlightRouteNotFoundException ex) {
                            System.out.println(ex.getMessage());
                            break;
                        }

                        flightEntitySessionBeanRemote.updateFlightRoute(newRoute, flight);

                        System.out.println("Route of flight " + flight.getFlightCode() + " has been updated to "
                                + newRoute.getOriginAirport().getAirportCode() + " - "
                                + newRoute.getDestinationAirport().getAirportCode());
                    }
                } else if (response == 2) {
                    flightEntitySessionBeanRemote.deleteFlight(flight);
                    System.out.println("Flight " + flight.getFlightCode() + " successfully deleted!");
                }
            }
        }
    }

    public FlightScheduleEntity createFlightSchedule(Scanner sc, DateTimeFormatter dateFormat, FlightEntity flight) {

        System.out.println("Enter departure date and time (yyyy-MM-dd HH:mm)>");
        String departureDate = sc.nextLine();
        String timeZone = flightEntitySessionBeanRemote.retrieveTimeZoneByFlight(flight);
        departureDate = departureDate + " " + timeZone;

        System.out.println("Enter duration of flight (HH:MM)>");
        String duration = sc.nextLine();
        String durationHour = duration.substring(0, 1);
        String durationMin = duration.substring(3, 4);

        int durationHourInt = Integer.parseInt(durationHour);
        int durationMinInt = Integer.parseInt(durationMin);
        int totalDuration = durationMinInt + (60 * durationHourInt);

        ZonedDateTime departureDateTime = ZonedDateTime.parse(departureDate, dateFormat);
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
            return;
        }

        //List<CabinClassConfigurationEntity> cabinClassList = flight.getAircraftConfigurationEntity().getCabinClassConfigurationEntitys();
        System.out.println("Select schedule plan type>");
        System.out.println("1: Single schedule");
        System.out.println("2: Multiple schedules");
        System.out.println("3: Recurrent daily schedules");
        System.out.println("4: Recurrent weekly schedules");

        int typeResponse = sc.nextInt();
        sc.nextLine();

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm Z");;

        if (typeResponse == 1) {
            FlightSchedulePlanTypeEnum type = FlightSchedulePlanTypeEnum.SINGLE;

            FlightScheduleEntity flightSchedule = this.createFlightSchedule(sc, dateFormat, flight);
            FlightScheduleEntity returnFlightSchedule = null;
            int totalLayoverDuration = 0;

            String startDate = flightSchedule.getDepartureDateTime().substring(0, 9);

            if (flight.getReturnFlight() != null) {
                System.out.print("There is a return flight for this route. Do you want to create return flight schedule? (Y/N)>");
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
                FlightScheduleEntity flightSchedule = this.createFlightSchedule(sc, dateFormat, flight);
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
