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
import java.util.logging.Level;
import java.util.logging.Logger;
import util.enumeration.CabinClassTypeEnum;
import util.enumeration.FlightSchedulePlanTypeEnum;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.AirportNotFoundException;
import util.exception.FlightNotFoundException;
import util.exception.FlightRouteDisabled;
import util.exception.FlightRouteNotFoundException;
import util.exception.ScheduleIsUsedException;
import util.exception.ScheduleOverlapException;

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

    public String searchSchedulePlanType(FlightSchedulePlanTypeEnum type) {
        if (type.equals(FlightSchedulePlanTypeEnum.SINGLE)) {
            return "First Class";
        } else if (type.equals(FlightSchedulePlanTypeEnum.MULTIPLE)) {
            return "Business Class";
        } else if (type.equals(FlightSchedulePlanTypeEnum.RECURRENT_DAY)) {
            return "Premium Economy Class";
        } else if (type.equals(FlightSchedulePlanTypeEnum.RECURRENT_WEEK)) {
            return "Economy Class";
        }
        return "invalid";
    }

    public void createFlight(Scanner sc) {
        System.out.println("*** FRS Schedule Manager :: Create Flight ***");
        sc.nextLine();

        System.out.println("Enter flight number> ");
        int flightNumber = sc.nextInt();
        sc.nextLine();

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
            return;
        }

        FlightRouteEntity route = null;
        try {
            route = flightRouteEntitySessionBeanRemote.retrieveRouteByAirport(originAirport, destinationAirport);
            boolean disabled = flightRouteEntitySessionBeanRemote.isDisabled(route);
        } catch (FlightRouteNotFoundException | FlightRouteDisabled ex) {
            System.out.println(ex.getMessage());
            return;
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
            System.out.println("Enter flight number> ");
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
            return;
        }

        AircraftConfigurationEntity aircraftConfiguration = flight.getAircraftConfigurationEntity();
        FlightRouteEntity route = flight.getRoute();
        String origin = route.getOriginAirport().getAirportCode();
        String destination = route.getDestinationAirport().getAirportCode();
        int maxCapacity = aircraftConfiguration.getType().getMaxCapacity();
        List<CabinClassConfigurationEntity> cabinClasses = flightEntitySessionBeanRemote.retrieveCabinClassByFlight(flight);

        System.out.println("FLIGHT " + flight.getFlightCode() + ": ");
        System.out.println("From: " + origin + " to: " + destination);
        System.out.println("Total maximum capacity: " + maxCapacity);
        System.out.println("Available cabin classes: ");
        for (int i = 1; i <= cabinClasses.size(); i++) {
            System.out.println("\t" + i + ". " + searchCabinType(cabinClasses.get(i - 1).getType()));
            System.out.println("\t" + "Available capacity: " + cabinClasses.get(i - 1).getMaxCapacity());
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

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm Z");

        if (typeResponse == 1) {
            FlightSchedulePlanTypeEnum type = FlightSchedulePlanTypeEnum.SINGLE;

            FlightScheduleEntity flightSchedule = this.createFlightSchedule(sc, dateFormat, flight);
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

            flightSchedulePlanEntitySessionBeanRemote.associatePlanWithFlight(schedulePlan, flight);
            System.out.println("Flight schedule with id " + schedulePlan.getSchedulePlanId() + "is created!");

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

                try {
                    flightScheduleEntitySessionBeanRemote.checkOverlapSchedule(schedulePlan, flightSchedule);
                } catch (ScheduleOverlapException ex) {
                    System.out.println("Schedule invalid with exisiting schedule!");
                    return;
                }

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

            flightSchedulePlanEntitySessionBeanRemote.associatePlanWithFlight(schedulePlan, flight);
            System.out.println("Flight schedule with id " + schedulePlan.getSchedulePlanId() + "is created!");

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

            flightSchedulePlanEntitySessionBeanRemote.associatePlanWithFlight(schedulePlan, flight);
            System.out.println("Flight schedule with id " + schedulePlan.getSchedulePlanId() + "is created!");

        } else if (typeResponse == 4) {
            FlightSchedulePlanTypeEnum type = FlightSchedulePlanTypeEnum.RECURRENT_WEEK;

            System.out.println("Enter starting date (yyyy-mm-dd)>");
            String startDate = sc.nextLine();

            System.out.println("Enter ending date (yyyy-mm-dd)>");
            String endDate = sc.nextLine();

            System.out.println("Enter departure hour (HH:mm)>");
            String departuretime = sc.nextLine();

            System.out.println("Enter duration of flight (HH:MM)>");
            String duration = sc.nextLine();

            FlightSchedulePlanEntity schedulePlan = flightSchedulePlanEntitySessionBeanRemote.createFlightSchedulePlanEntity(new FlightSchedulePlanEntity(type, startDate, endDate, 7, flight));

            if (flight.getReturnFlight() != null) {
                System.out.println("There is a return flight for this route. Do you want to create return flight schedule? (Y/N)>");
                String response = sc.nextLine();

                if (response.equals("Y")) {
                    int totalLayoverDuration = this.scanLayoverTime(sc);
                    flightScheduleEntitySessionBeanRemote.createRecurrentSchedule(schedulePlan, startDate, endDate, 7, departuretime, dateFormat, duration, true, totalLayoverDuration);
                } else {
                    flightScheduleEntitySessionBeanRemote.createRecurrentSchedule(schedulePlan, startDate, endDate, 7, departuretime, dateFormat, duration, false, 0);
                }
            }

            flightSchedulePlanEntitySessionBeanRemote.associatePlanWithFlight(schedulePlan, flight);
            System.out.println("Flight schedule with id " + schedulePlan.getSchedulePlanId() + "is created!");
        }
    }

    public void deleteSchedulePlan(Scanner sc) {
        System.out.println("*** FRS Schedule Manager :: Delete Schedule Plan ***");
        sc.nextLine();

        System.out.println("Enter schedule plan ID>");
        long planId = sc.nextLong();

        try {
            flightSchedulePlanEntitySessionBeanRemote.deleteSchedulePlan(planId);
        } catch (ScheduleIsUsedException ex) {
            System.out.println("Schedule plan is in use!");
            return;
        }
    }

    public void updateSchedulePlan(Scanner sc) {
        System.out.println("*** FRS Schedule Manager :: Update Schedule Plan ***");
        sc.nextLine();

        System.out.println("Enter schedule plan id>");
        long planId = sc.nextLong();
        sc.nextLine();

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm Z");
        FlightSchedulePlanEntity plan = flightSchedulePlanEntitySessionBeanRemote.retrieveSchedulePlanById(planId);
        FlightSchedulePlanTypeEnum type = plan.getType();

        FlightEntity flight = flightSchedulePlanEntitySessionBeanRemote.retrieveFlightFromPlan(plan);

        if (type.equals(FlightSchedulePlanTypeEnum.SINGLE)) {
            FlightScheduleEntity schedule = flightSchedulePlanEntitySessionBeanRemote.retrieveScheduleSinglePlan(plan);
            updateSingleSchedule(sc, dateFormat, flight, plan, schedule);
        } else if (type.equals(FlightSchedulePlanTypeEnum.MULTIPLE)) {
            System.out.println("Enter schedule id>");
            long scheduleId = sc.nextLong();
            sc.nextLine();

            FlightScheduleEntity schedule = flightScheduleEntitySessionBeanRemote.retrieveFlightScheduleById(scheduleId);
            updateSingleSchedule(sc, dateFormat, flight, plan, schedule);
        } else if (type.equals(FlightSchedulePlanTypeEnum.RECURRENT_DAY)) {
            System.out.println("Enter new starting date (yyyy-mm-dd)>");
            String startDate = sc.nextLine();

            System.out.println("Enter new ending date (yyyy-mm-dd)>");
            String endDate = sc.nextLine();

            System.out.println("Enter new departure hour (HH:mm)>");
            String departuretime = sc.nextLine();

            System.out.println("Enter new duration of flight (HH:MM)>");
            String duration = sc.nextLine();

            System.out.println("Enter new number of recurring days>");
            int recurringDays = sc.nextInt();
            sc.nextLine();

            System.out.println("Enter layover duration if needed (HH:MM)>");
            String layoverDuration = sc.nextLine();
            String layoverDurationHour = layoverDuration.substring(0, 1);
            String layoverDurationMin = layoverDuration.substring(3, 4);

            int layoverDurationHourInt = Integer.parseInt(layoverDurationHour);
            int layoverDurationMinInt = Integer.parseInt(layoverDurationMin);
            layoverDurationMinInt += (60 * layoverDurationHourInt);

            FlightSchedulePlanEntity newPlan = new FlightSchedulePlanEntity(FlightSchedulePlanTypeEnum.RECURRENT_DAY, startDate, endDate, layoverDurationMinInt, flight);

            try {
                flightSchedulePlanEntitySessionBeanRemote.replaceRecurrentSchedulePlan(plan, newPlan, dateFormat, departuretime, duration, recurringDays, layoverDurationMinInt);
                System.out.println("Schedule plan updated!");
            } catch (ScheduleIsUsedException ex) {
                System.out.println("Failed in updating schedule plan! At least one schedule is used!");
                return;
            } catch (ScheduleOverlapException ex) {
                System.out.println("Failed in updating schedule plan! Updated scheduled overlapped with existing schedule!");
                return;
            }
        } else if (type.equals(FlightSchedulePlanTypeEnum.RECURRENT_WEEK)) {
            System.out.println("Enter new starting date (yyyy-mm-dd)>");
            String startDate = sc.nextLine();

            System.out.println("Enter new ending date (yyyy-mm-dd)>");
            String endDate = sc.nextLine();

            System.out.println("Enter new departure hour (HH:mm)>");
            String departuretime = sc.nextLine();

            System.out.println("Enter new duration of flight (HH:MM)>");
            String duration = sc.nextLine();

            System.out.println("Enter layover duration if needed (HH:MM)>");
            String layoverDuration = sc.nextLine();
            String layoverDurationHour = layoverDuration.substring(0, 1);
            String layoverDurationMin = layoverDuration.substring(3, 4);

            int layoverDurationHourInt = Integer.parseInt(layoverDurationHour);
            int layoverDurationMinInt = Integer.parseInt(layoverDurationMin);
            layoverDurationMinInt += (60 * layoverDurationHourInt);

            FlightSchedulePlanEntity newPlan = new FlightSchedulePlanEntity(FlightSchedulePlanTypeEnum.RECURRENT_WEEK, startDate, endDate, layoverDurationMinInt, flight);

            try {
                flightSchedulePlanEntitySessionBeanRemote.replaceRecurrentSchedulePlan(plan, newPlan, dateFormat, departuretime, duration, 7, layoverDurationMinInt);
                System.out.println("Schedule plan updated!");
            } catch (ScheduleIsUsedException ex) {
                System.out.println("Failed in updating schedule plan! At least one schedule is used!");
                return;
            } catch (ScheduleOverlapException ex) {
                System.out.println("Failed in updating schedule plan! Updated scheduled overlapped with existing schedule!");
                return;
            }
        }
    }

    public void updateSingleSchedule(Scanner sc, DateTimeFormatter dateFormat, FlightEntity flight, FlightSchedulePlanEntity plan, FlightScheduleEntity schedule) {
        System.out.println("Enter new departure date and time (yyyy-MM-dd HH:mm)>");
        String newDepartureDateTime = sc.nextLine();

        String timeZone = flightEntitySessionBeanRemote.retrieveTimeZoneByFlight(flight);
        newDepartureDateTime = newDepartureDateTime + " " + timeZone;

        System.out.println("Enter new duration of flight (HH:MM)>");
        String duration = sc.nextLine();
        String durationHour = duration.substring(0, 1);
        String durationMin = duration.substring(3, 4);

        int durationHourInt = Integer.parseInt(durationHour);
        int durationMinInt = Integer.parseInt(durationMin);
        int totalDuration = durationMinInt + (60 * durationHourInt);

        ZonedDateTime departureDateTime = ZonedDateTime.parse(newDepartureDateTime, dateFormat);
        ZonedDateTime arrivalDateTime = departureDateTime.plusMinutes(totalDuration);
        String arrDateTime = arrivalDateTime.format(dateFormat);

        System.out.println("Expected new arrival date and time: " + arrDateTime);

        FlightScheduleEntity returnSchedule = flightScheduleEntitySessionBeanRemote.retrieveReturnSchedule(schedule);
        FlightScheduleEntity flightSchedule = new FlightScheduleEntity(newDepartureDateTime, totalDuration, arrDateTime);

        int layover = scanLayoverTime(sc);
        FlightScheduleEntity returnFlightSchedule = replaceReturnFlightSchedule(sc, dateFormat, layover, flightSchedule);
        FlightScheduleEntity overlap = null;
        FlightScheduleEntity returnOverlap = null;

        if (schedule.getReturnSchedule() != null) {
            overlap = flightScheduleEntitySessionBeanRemote.checkOverlapPlan(flight, plan, flightSchedule);
            returnOverlap = flightScheduleEntitySessionBeanRemote.checkOverlapPlan(flight, plan, returnFlightSchedule);
        }

        if (overlap != null) {
            System.out.println("New flight schedule overlaps with other schedule(s)!");
            System.out.println("Continue and delete overlapping schedule? (Y/N)");
            String response = sc.nextLine();

            if (response.equals("Y")) {
                try {
                    flightScheduleEntitySessionBeanRemote.deleteSchedule(overlap);
                } catch (ScheduleIsUsedException ex) {
                    System.out.println("Schedule is used and cannot be deleted!");
                    return;
                }
            }
        }

        if (returnOverlap != null) {
            System.out.println("New return flight schedule overlaps with other schedule(s)!");
            System.out.println("Continue and delete overlapping schedule? (Y/N)");
            String response = sc.nextLine();

            if (response.equals("Y")) {
                try {
                    flightScheduleEntitySessionBeanRemote.deleteSchedule(overlap);
                } catch (ScheduleIsUsedException ex) {
                    System.out.println("Schedule is used and cannot be deleted!");
                    return;
                }
            }
        }

        flightScheduleEntitySessionBeanRemote.replaceSchedule(schedule, flightSchedule);

        if (returnOverlap != null) {
            flightScheduleEntitySessionBeanRemote.replaceSchedule(returnSchedule, returnFlightSchedule);
        }

        System.out.println("Schedule plan updated!");
    }

    public FlightScheduleEntity replaceReturnFlightSchedule(Scanner sc, DateTimeFormatter dateFormat, int totalLayoverDuration, FlightScheduleEntity departureFlight) {

        String arrDateTime = departureFlight.getArrivalDateTime();
        int duration = departureFlight.getDuration();

        ZonedDateTime returnDepartureDateTime = ZonedDateTime.parse(arrDateTime).plusMinutes(totalLayoverDuration);
        String returnDepDateTime = returnDepartureDateTime.format(dateFormat);
        System.out.println("Return departure time: " + returnDepDateTime);

        ZonedDateTime returnArrivalDateTime = returnDepartureDateTime.plusMinutes(duration);
        String returnArrDateTime = returnArrivalDateTime.format(dateFormat);
        System.out.println("Expected return arrival time: " + returnArrDateTime);

        FlightScheduleEntity returnFlightSchedule = flightScheduleEntitySessionBeanRemote.createFlightScheduleEntity(new FlightScheduleEntity(returnDepDateTime, duration, returnArrDateTime));

        return returnFlightSchedule;
    }
}
