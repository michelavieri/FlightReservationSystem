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
import ejb.session.stateless.FareEntitySessionBeanRemote;
import ejb.session.stateless.FlightEntitySessionBeanRemote;
import ejb.session.stateless.FlightRouteEntitySessionBeanRemote;
import ejb.session.stateless.FlightScheduleEntitySessionBeanRemote;
import ejb.session.stateless.FlightSchedulePlanEntitySessionBeanRemote;
import entity.AircraftConfigurationEntity;
import entity.AirportEntity;
import entity.CabinClassConfigurationEntity;
import entity.FareEntity;
import entity.FlightEntity;
import entity.FlightRouteEntity;
import entity.FlightScheduleEntity;
import entity.FlightSchedulePlanEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import util.exception.FlightScheduleNotFoundException;
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

    private FareEntitySessionBeanRemote fareEntitySessionBeanRemote;

    public FlightOperationModule(AirportEntitySessionBeanRemote airportEntitySessionBeanRemote,
            AircraftTypeEntitySessionBeanRemote aircraftTypeEntitySessionBeanRemote,
            CabinClassConfigurationSessionBeanRemote cabinClassConfigurationSessionBeanRemote,
            AircraftConfigurationEntitySessionBeanRemote aircraftConfigurationEntitySessionBeanRemote,
            FlightEntitySessionBeanRemote flightEntitySessionBeanRemote,
            FlightRouteEntitySessionBeanRemote flightRouteEntitySessionBeanRemote,
            FlightScheduleEntitySessionBeanRemote flightScheduleEntitySessionBeanRemote,
            FlightSchedulePlanEntitySessionBeanRemote flightSchedulePlanEntitySessionBeanRemote,
            FareEntitySessionBeanRemote fareEntitySessionBeanRemote) {
        this.airportEntitySessionBeanRemote = airportEntitySessionBeanRemote;
        this.aircraftTypeEntitySessionBeanRemote = aircraftTypeEntitySessionBeanRemote;
        this.cabinClassConfigurationSessionBeanRemote = cabinClassConfigurationSessionBeanRemote;
        this.aircraftConfigurationEntitySessionBeanRemote = aircraftConfigurationEntitySessionBeanRemote;
        this.flightEntitySessionBeanRemote = flightEntitySessionBeanRemote;
        this.flightRouteEntitySessionBeanRemote = flightRouteEntitySessionBeanRemote;
        this.flightScheduleEntitySessionBeanRemote = flightScheduleEntitySessionBeanRemote;
        this.flightSchedulePlanEntitySessionBeanRemote = flightSchedulePlanEntitySessionBeanRemote;
        this.fareEntitySessionBeanRemote = fareEntitySessionBeanRemote;
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
            return "Single Plan";
        } else if (type.equals(FlightSchedulePlanTypeEnum.MULTIPLE)) {
            return "Multiple Plan";
        } else if (type.equals(FlightSchedulePlanTypeEnum.RECURRENT_DAY)) {
            return "Recurrent Daily Plan";
        } else if (type.equals(FlightSchedulePlanTypeEnum.RECURRENT_WEEK)) {
            return "Recurrent Weekly Plan";
        }
        return "invalid";
    }

    public void createFlight(Scanner sc) {
        System.out.println("*** FRS Schedule Manager :: Create Flight ***");
        sc.nextLine();

        System.out.print("Enter flight number ML> ");
        int flightNumber = sc.nextInt();
        sc.nextLine();
        String number = "ML" + flightNumber;
        try {
            flightEntitySessionBeanRemote.retrieveFlightByCode(number);
            System.out.println("This flight number has been registered before!");
            return;
        } catch (FlightNotFoundException ex) {
        }

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

    public FlightScheduleEntity createFlightSchedule(Scanner sc, DateTimeFormatter dateFormat, FlightEntity flight, FlightSchedulePlanEntity plan) throws ScheduleOverlapException {

        System.out.println("Enter departure date and time (yyyy-MM-dd HH:mm)>");
        String departureDate = sc.nextLine();
        String timeZone = flightEntitySessionBeanRemote.retrieveTimeZoneByFlight(flight);
        departureDate = departureDate + " " + timeZone;

        System.out.println("Enter duration of flight (HH:MM)>");
        String duration = sc.nextLine();
        String durationHour = duration.substring(0, 2);
        String durationMin = duration.substring(3);

        int durationHourInt = Integer.parseInt(durationHour);
        int durationMinInt = Integer.parseInt(durationMin);
        int totalDuration = durationMinInt + (60 * durationHourInt);

        ZonedDateTime departureDateTime = ZonedDateTime.parse(departureDate, dateFormat);
        ZonedDateTime arrivalDateTime = departureDateTime.plusMinutes(totalDuration);
        String arrDateTime = arrivalDateTime.format(dateFormat);

        System.out.println("Expected arrival date and time: " + arrDateTime);

        FlightScheduleEntity flightSchedule = new FlightScheduleEntity(departureDate, totalDuration, arrDateTime);
//        if (plan.getType().equals(FlightSchedulePlanTypeEnum.SINGLE)) {
//            plan = new FlightSchedulePlanEntity(FlightSchedulePlanTypeEnum.SINGLE, departureDate, totalDuration, flight);
//        }
//
//        boolean overlap = this.checkOverlap(flightSchedule, plan, dateFormat);
//
//        if (overlap) {
//            throw new ScheduleOverlapException("Schedule overlaps with existing shedule");
//        }

        flightSchedule = flightScheduleEntitySessionBeanRemote.createFlightScheduleEntity(flightSchedule);

        return flightSchedule;
    }

    public FlightScheduleEntity createReturnFlightSchedule(Scanner sc, DateTimeFormatter dateFormat, int totalLayoverDuration, FlightScheduleEntity departureFlight) {

        String arrDateTime = departureFlight.getArrivalDateTime();
        int duration = departureFlight.getDuration();

        ZonedDateTime returnDepartureDateTime = ZonedDateTime.parse(arrDateTime, dateFormat).plusMinutes(totalLayoverDuration);
        String returnDepDateTime = returnDepartureDateTime.format(dateFormat);
        System.out.println("Return departure time: " + returnDepDateTime);

        ZonedDateTime returnArrivalDateTime = returnDepartureDateTime.plusMinutes(duration);
        String returnArrDateTime = returnArrivalDateTime.format(dateFormat);
        System.out.println("Expected return arrival time: " + returnArrDateTime);

        FlightScheduleEntity returnFlightSchedule = flightScheduleEntitySessionBeanRemote.createFlightScheduleEntity(new FlightScheduleEntity(returnDepDateTime, duration, returnArrDateTime));

        flightScheduleEntitySessionBeanRemote.associateReturnSchedule(departureFlight, returnFlightSchedule);

        return returnFlightSchedule;
    }

    public int scanLayoverTime(Scanner sc) {
        System.out.println("Enter layover duration (HH:MM)>");
        String layoverDuration = sc.nextLine();
        String layoverDurationHour = layoverDuration.substring(0, 2);
        String layoverDurationMin = layoverDuration.substring(3);

        int layoverDurationHourInt = Integer.parseInt(layoverDurationHour);
        int layoverDurationMinInt = Integer.parseInt(layoverDurationMin);
        return layoverDurationMinInt + (60 * layoverDurationHourInt);
    }

    public void createFare(Scanner sc, FlightEntity flight, FlightSchedulePlanEntity plan) {
        List<CabinClassConfigurationEntity> cabinClasses = flightEntitySessionBeanRemote.retrieveCabinClassByFlight(flight);

        for (CabinClassConfigurationEntity cabinClass : cabinClasses) {
            System.out.println("Cabin class type: " + searchCabinType(cabinClass.getType()));

            System.out.println("Enter number of fare >");
            int numFare = sc.nextInt();
            sc.nextLine();

            for (int i = 0; i < numFare; i++) {
                System.out.println("Enter fare basis code>");
                String code = sc.nextLine();
                System.out.println("Enter fare amount>");
                String amount = sc.nextLine();

                FareEntity fare = new FareEntity(code, amount);
                fare = fareEntitySessionBeanRemote.createFlightSchedulePlanEntity(fare);

                fareEntitySessionBeanRemote.associateFareWithCabinClass(cabinClass, fare);
            }
        }
    }

    public void updateFare(Scanner sc, FlightSchedulePlanEntity plan) {
        List<FareEntity> fares = fareEntitySessionBeanRemote.retrieveFareBySchedulePlan(plan);

        for (FareEntity fare : fares) {
            System.out.println("Cabin class type: " + searchCabinType(fare.getCabinClass().getType()));

            System.out.println("Fare basis code: " + fare.getFareBasisCode());

            System.out.println("Enter updated fare amount>");
            String amount = sc.nextLine();

            fare.setAmount(amount);
        }
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
            FlightSchedulePlanEntity schedulePlan = new FlightSchedulePlanEntity();
            FlightSchedulePlanEntity returnPlan = null;

            FlightScheduleEntity flightSchedule = null;

            try {
                flightSchedule = this.createFlightSchedule(sc, dateFormat, flight, schedulePlan);
            } catch (ScheduleOverlapException ex) {
                ex.getMessage();
                return;
            }

            FlightScheduleEntity returnFlightSchedule = null;
            int totalLayoverDuration = 0;
            String response = "";

            String startDate = flightSchedule.getDepartureDateTime();

            if (flight.getReturnFlight() != null) {
                System.out.print("There is a return flight for this route. Do you want to create return flight schedule plan? (Y/N)>");
                response = sc.nextLine();

                if (response.equals("Y")) {

                    totalLayoverDuration = this.scanLayoverTime(sc);
                    returnFlightSchedule = this.createReturnFlightSchedule(sc, dateFormat, totalLayoverDuration, flightSchedule);
                }
            }

            if (returnFlightSchedule == null) {

                schedulePlan = flightSchedulePlanEntitySessionBeanRemote.createFlightSchedulePlanEntity(new FlightSchedulePlanEntity(type, startDate, 0, flight));
                flightScheduleEntitySessionBeanRemote.associateWithPlan(flightSchedule, schedulePlan);

            } else {

                schedulePlan = flightSchedulePlanEntitySessionBeanRemote.createFlightSchedulePlanEntity(new FlightSchedulePlanEntity(type, startDate, totalLayoverDuration, flight));
                returnPlan = flightSchedulePlanEntitySessionBeanRemote.createFlightSchedulePlanEntity(new FlightSchedulePlanEntity(type, returnFlightSchedule.getDepartureDateTime(), totalLayoverDuration, flight.getReturnFlight()));

                flightScheduleEntitySessionBeanRemote.associateWithPlan(flightSchedule, schedulePlan);
                flightScheduleEntitySessionBeanRemote.associateWithPlan(returnFlightSchedule, returnPlan);

                flightSchedulePlanEntitySessionBeanRemote.associatePlanWithFlight(returnPlan, flight.getReturnFlight());
                flightScheduleEntitySessionBeanRemote.associateNewSeatsInventory(returnPlan);

                flightSchedulePlanEntitySessionBeanRemote.associateReturnSchedulePlan(schedulePlan, returnPlan);
            }

            flightSchedulePlanEntitySessionBeanRemote.associatePlanWithFlight(schedulePlan, flight);
            flightScheduleEntitySessionBeanRemote.associateNewSeatsInventory(schedulePlan);

            createFare(sc, flight, schedulePlan);

            if (returnPlan != null) {
                fareEntitySessionBeanRemote.setReturnFare(schedulePlan, returnPlan);
            }

            System.out.println("Flight schedule plan with id " + schedulePlan.getSchedulePlanId() + " is created!");
            if (response.equals("Y")) {
                System.out.println("Return flight schedule plan with id " + returnPlan.getSchedulePlanId() + " is created!");
            }

        } else if (typeResponse == 2) {
            FlightSchedulePlanTypeEnum type = FlightSchedulePlanTypeEnum.MULTIPLE;

            System.out.println("Enter number of schedule>");
            int numSchedule = sc.nextInt();
            sc.nextLine();

            System.out.println("Enter starting date (yyyy-mm-dd)>");
            String startDate = sc.nextLine();

            FlightSchedulePlanEntity schedulePlan = flightSchedulePlanEntitySessionBeanRemote.createFlightSchedulePlanEntity(new FlightSchedulePlanEntity(type, startDate, 0, flight));
            FlightSchedulePlanEntity returnPlan = null;

            int totalLayoverDuration = 0;

            for (int i = 0; i < numSchedule; i++) {
                FlightScheduleEntity flightSchedule = null;

                try {
                    flightSchedule = this.createFlightSchedule(sc, dateFormat, flight, schedulePlan);
                } catch (ScheduleOverlapException ex) {
                    ex.getMessage();
                    return;
                }

                flightScheduleEntitySessionBeanRemote.associateWithPlan(flightSchedule, schedulePlan);
            }

            flightSchedulePlanEntitySessionBeanRemote.associatePlanWithFlight(schedulePlan, flight);

            schedulePlan.getFlightSchedules().size();
            List<FlightScheduleEntity> schedules = schedulePlan.getFlightSchedules();
            String response = "";

            if (flight.getReturnFlight() != null) {
                System.out.println("There is a return flight for this route. Do you want to create return flight schedule? (Y/N)>");
                response = sc.nextLine();
                FlightScheduleEntity returnFlightSchedule = null;

                if (response.equals("Y")) {
                    DateTimeFormatter dateFormat2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                    totalLayoverDuration = this.scanLayoverTime(sc);

                    returnPlan = flightSchedulePlanEntitySessionBeanRemote.createFlightSchedulePlanEntity(new FlightSchedulePlanEntity(type, schedulePlan.getStartDate(), totalLayoverDuration, flight.getReturnFlight()));

                    for (FlightScheduleEntity schedule : schedules) {
                        returnFlightSchedule = this.createReturnFlightSchedule(sc, dateFormat, totalLayoverDuration, schedule);

                        boolean overlap = this.checkOverlap(schedule, returnPlan, dateFormat);

                        if (overlap) {
                            System.out.println("Schedule overlaps with existing shedule");
                            return;
                        }
                    }

                    flightSchedulePlanEntitySessionBeanRemote.associatePlanWithFlight(returnPlan, flight.getReturnFlight());
                    createFare(sc, flight.getReturnFlight(), returnPlan);

                    flightSchedulePlanEntitySessionBeanRemote.associateReturnSchedulePlan(schedulePlan, returnPlan);
                    flightScheduleEntitySessionBeanRemote.associateNewSeatsInventory(returnPlan);
                }
            }
            flightSchedulePlanEntitySessionBeanRemote.associatePlanWithFlight(schedulePlan, flight);
            flightScheduleEntitySessionBeanRemote.associateNewSeatsInventory(schedulePlan);
            createFare(sc, flight, schedulePlan);

            if (returnPlan != null) {
                fareEntitySessionBeanRemote.setReturnFare(schedulePlan, returnPlan);
            }

            System.out.println("Flight schedule plan with id " + schedulePlan.getSchedulePlanId() + " is created!");

            if (response.equals("Y")) {
                System.out.println("Return flight schedule plan with id " + returnPlan.getSchedulePlanId() + " is created!");
            }

        } else if (typeResponse == 3) {
            FlightSchedulePlanTypeEnum type = FlightSchedulePlanTypeEnum.RECURRENT_DAY;

            System.out.println("Enter number of recurring days>");
            int day = sc.nextInt();
            sc.nextLine();

            System.out.println("Enter starting date (yyyy-mm-dd)>");
            String startDate = sc.nextLine();

            System.out.println("Enter ending date (yyyy-mm-dd)>");
            String endDate = sc.nextLine();

            System.out.println("Enter departure hour (HH:mm)>");
            String departuretime = sc.nextLine();

            System.out.println("Enter duration of flight (HH:MM)>");
            String duration = sc.nextLine();

            FlightSchedulePlanEntity schedulePlan = flightSchedulePlanEntitySessionBeanRemote.createFlightSchedulePlanEntity(new FlightSchedulePlanEntity(type, startDate, endDate, 0, flight));
            FlightSchedulePlanEntity returnPlan = null;
            String response = "";

            if (flight.getReturnFlight() != null) {
                System.out.println("There is a return flight for this route. Do you want to create return flight schedule? (Y/N)>");
                response = sc.nextLine();

                if (response.equals("Y")) {
                    int totalLayoverDuration = this.scanLayoverTime(sc);

                    flightScheduleEntitySessionBeanRemote.setLayover(schedulePlan, totalLayoverDuration);

                    String returnTime = this.createRecurrentSchedule("", schedulePlan, startDate, endDate, day, departuretime, dateFormat, duration, totalLayoverDuration);
                    String zone = flightEntitySessionBeanRemote.retrieveTimeZoneByFlight(flight);

                    LocalDate localfirstDate = LocalDate.parse(schedulePlan.getStartDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    ZonedDateTime firstDate = localfirstDate.atStartOfDay(ZoneId.systemDefault());

                    firstDate = firstDate.plusMinutes(totalLayoverDuration);
                    String firstDateStr = firstDate.format(dateFormat).substring(0, 10);

                    LocalDate localLastDate = LocalDate.parse(schedulePlan.getEndDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    ZonedDateTime lastDate = localLastDate.atStartOfDay(ZoneId.systemDefault());

                    lastDate = lastDate.plusMinutes(totalLayoverDuration);
                    String lastDateStr = lastDate.format(dateFormat).substring(0, 10);

                    returnPlan = flightSchedulePlanEntitySessionBeanRemote.createFlightSchedulePlanEntity(new FlightSchedulePlanEntity(type, firstDateStr, lastDateStr, 0, flight.getReturnFlight()));
                    flightSchedulePlanEntitySessionBeanRemote.associateReturnSchedulePlan(schedulePlan, returnPlan);

                    this.createRecurrentSchedule("", returnPlan, firstDateStr, lastDateStr, day, returnTime, dateFormat, duration, totalLayoverDuration);
                    flightSchedulePlanEntitySessionBeanRemote.associatePlanWithFlight(returnPlan, flight.getReturnFlight());
                    flightScheduleEntitySessionBeanRemote.associateNewSeatsInventory(returnPlan);

                } else {
                    this.createRecurrentSchedule("", schedulePlan, startDate, endDate, day, departuretime, dateFormat, duration, 0);
                }
            }

            createFare(sc, flight, schedulePlan);
            if (returnPlan != null) {
                fareEntitySessionBeanRemote.setReturnFare(schedulePlan, returnPlan);
            }
            flightSchedulePlanEntitySessionBeanRemote.associatePlanWithFlight(schedulePlan, flight);
            flightScheduleEntitySessionBeanRemote.associateNewSeatsInventory(schedulePlan);
            System.out.println("Flight schedule with id " + schedulePlan.getSchedulePlanId() + "is created!");

            if (response.equals("Y")) {
                System.out.println("Return flight schedule plan with id " + returnPlan.getSchedulePlanId() + " is created!");
            }

        } else if (typeResponse == 4) {
            FlightSchedulePlanTypeEnum type = FlightSchedulePlanTypeEnum.RECURRENT_WEEK;

            System.out.println("Enter day of recurring (All capital letters)>");
            String day = sc.nextLine();

            System.out.println("Enter starting date (yyyy-mm-dd)>");
            String startDate = sc.nextLine();

            System.out.println("Enter ending date (yyyy-mm-dd)>");
            String endDate = sc.nextLine();

            System.out.println("Enter departure hour (HH:mm)>");
            String departuretime = sc.nextLine();

            System.out.println("Enter duration of flight (HH:MM)>");
            String duration = sc.nextLine();

            FlightSchedulePlanEntity schedulePlan = flightSchedulePlanEntitySessionBeanRemote.createFlightSchedulePlanEntity(new FlightSchedulePlanEntity(type, startDate, endDate, 0, flight));
            FlightSchedulePlanEntity returnPlan = null;
            String response = "";

            if (flight.getReturnFlight() != null) {
                System.out.println("There is a return flight for this route. Do you want to create return flight schedule? (Y/N)>");
                response = sc.nextLine();

                if (response.equals("Y")) {
                    int totalLayoverDuration = this.scanLayoverTime(sc);

                    flightScheduleEntitySessionBeanRemote.setLayover(schedulePlan, totalLayoverDuration);
                    String returnTime = this.createRecurrentSchedule(day, schedulePlan, startDate, endDate, 7, departuretime, dateFormat, duration, totalLayoverDuration);
                    String zone = flightEntitySessionBeanRemote.retrieveTimeZoneByFlight(flight);

                    LocalDate localfirstDate = LocalDate.parse(schedulePlan.getStartDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    ZonedDateTime firstDate = localfirstDate.atStartOfDay(ZoneId.systemDefault());

                    firstDate = firstDate.plusMinutes(totalLayoverDuration);
                    String firstDateStr = firstDate.format(dateFormat).substring(0, 10);

                    LocalDate localLastDate = LocalDate.parse(schedulePlan.getEndDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    ZonedDateTime lastDate = localLastDate.atStartOfDay(ZoneId.systemDefault());

                    lastDate = lastDate.plusMinutes(totalLayoverDuration);
                    String lastDateStr = lastDate.format(dateFormat).substring(0, 10);

                    returnPlan = flightSchedulePlanEntitySessionBeanRemote.createFlightSchedulePlanEntity(new FlightSchedulePlanEntity(type, firstDateStr, lastDateStr, 0, flight.getReturnFlight()));
                    flightSchedulePlanEntitySessionBeanRemote.associateReturnSchedulePlan(schedulePlan, returnPlan);

                    this.createRecurrentSchedule(day, returnPlan, firstDateStr, lastDateStr, 7, returnTime, dateFormat, duration, totalLayoverDuration);
                    flightSchedulePlanEntitySessionBeanRemote.associatePlanWithFlight(returnPlan, flight.getReturnFlight());
                    flightScheduleEntitySessionBeanRemote.associateNewSeatsInventory(returnPlan);

                } else {
                    this.createRecurrentSchedule(day, schedulePlan, startDate, endDate, 7, departuretime, dateFormat, duration, 0);
                }
            }

            createFare(sc, flight, schedulePlan);
            if (returnPlan != null) {
                fareEntitySessionBeanRemote.setReturnFare(schedulePlan, returnPlan);
            }
            flightSchedulePlanEntitySessionBeanRemote.associatePlanWithFlight(schedulePlan, flight);
            flightScheduleEntitySessionBeanRemote.associateNewSeatsInventory(schedulePlan);
            System.out.println("Flight schedule with id " + schedulePlan.getSchedulePlanId() + "is created!");

            if (response.equals("Y")) {
                System.out.println("Return flight schedule plan with id " + returnPlan.getSchedulePlanId() + " is created!");
            }
        }
    }

    public String createRecurrentSchedule(String day, FlightSchedulePlanEntity schedule, String startDate, String endDate, int days, String departureTime, DateTimeFormatter dateFormat, String duration, int layoverDuration) {

        FlightEntity flight = flightSchedulePlanEntitySessionBeanRemote.retrieveFlightFromPlan(schedule);
        String timeZone = flightEntitySessionBeanRemote.retrieveTimeZoneByFlight(flight);

        ZonedDateTime startingDate = ZonedDateTime.parse((startDate + " " + departureTime + " " + timeZone), dateFormat);

        if (schedule.getType().equals(FlightSchedulePlanTypeEnum.RECURRENT_WEEK)) {
            while (!startingDate.getDayOfWeek().toString().equals(day)) {
                startingDate = startingDate.plusDays(1);
            }
        }

        String startingDateTime = startingDate.format(dateFormat);
        LocalDate localEndingDate = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        ZonedDateTime endingDate = localEndingDate.atStartOfDay(ZoneId.systemDefault());

        String durationHour = duration.substring(0, 2);
        String durationMin = duration.substring(3);

        int durationHourInt = Integer.parseInt(durationHour);
        int durationMinInt = Integer.parseInt(durationMin);
        int totalDuration = durationMinInt + (60 * durationHourInt);

        ZonedDateTime arrivalDateTime = startingDate.plusMinutes(totalDuration);
        String arrDateTime = arrivalDateTime.format(dateFormat);

        arrivalDateTime = startingDate.plusMinutes(totalDuration);
        arrDateTime = arrivalDateTime.format(dateFormat);

        while (startingDate.isBefore(endingDate)) {

            startingDateTime = startingDate.format(dateFormat);

            arrivalDateTime = startingDate.plusMinutes(totalDuration);
            arrDateTime = arrivalDateTime.format(dateFormat);

            FlightScheduleEntity departure = flightScheduleEntitySessionBeanRemote.createFlightScheduleEntity(new FlightScheduleEntity(startingDateTime, totalDuration, arrDateTime));
            flightScheduleEntitySessionBeanRemote.associateWithPlan(departure, schedule);

            ZonedDateTime temp1 = startingDate.plusDays(days);
            startingDate = temp1;

            ZonedDateTime temp2 = arrivalDateTime.plusDays(days);
            arrivalDateTime = temp2;
        }

        arrivalDateTime.plusMinutes(layoverDuration);
        return arrivalDateTime.format(dateFormat).substring(11, 16);
    }

    public void viewAllSchedulePlan(Scanner sc) {
        System.out.println("*** FRS Schedule Manager :: View All Schedule Plan ***");
        sc.nextLine();

        List<FlightSchedulePlanEntity> plans = flightSchedulePlanEntitySessionBeanRemote.retrieveAllSchedulePlan();

        for (FlightSchedulePlanEntity plan : plans) {
            System.out.println("- Schedule plan id: " + plan.getSchedulePlanId());

            FlightEntity flight = flightSchedulePlanEntitySessionBeanRemote.retrieveFlightFromPlan(plan);
            System.out.println("  Flight number: " + flight.getFlightCode());
            System.out.println("  Disabled: " + plan.isDisabled());
            System.out.println();
        }
    }

    public void viewSchedulePlanDetails(Scanner sc) {
        System.out.println("*** FRS Schedule Manager :: View Schedule Plan Details***");
        sc.nextLine();

        System.out.println("Enter schedule plan id>");
        Long id = sc.nextLong();
        sc.nextLine();

        FlightSchedulePlanEntity plan = flightSchedulePlanEntitySessionBeanRemote.retrieveSchedulePlanById(id);

        System.out.println("Schedule plan id: " + plan.getSchedulePlanId());
        System.out.println("Schedule plan type: " + searchSchedulePlanType(plan.getType()));
        System.out.println("Schedule plan starting date: " + plan.getStartDate());
        if (plan.getEndDate() != null) {
            System.out.println("Schedule plan ending date: " + plan.getEndDate());
        }

        FlightEntity flight = flightSchedulePlanEntitySessionBeanRemote.retrieveFlightFromPlan(plan);
        System.out.println("Flight number: " + flight.getFlightCode());

        if (plan.getLayoverDuration() > 0) {
            System.out.println("Schedule plan layover duration (in minutes): " + plan.getLayoverDuration());
        }

        System.out.println("Do you want to update this schedule? (Y/N)");
        String response1 = sc.nextLine();

        if (response1.equals("Y")) {
            this.updateSchedulePlan(sc);
        }

        System.out.println("Do you want to delete this schedule? (Y/N)");
        String response2 = sc.nextLine();

        if (response2.equals("Y")) {
            this.deleteSchedulePlan(sc);
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

            FlightScheduleEntity schedule;
            try {
                schedule = flightScheduleEntitySessionBeanRemote.retrieveFlightScheduleById(scheduleId);
            } catch (FlightScheduleNotFoundException ex) {
                System.out.println(ex.getMessage());
                return;
            }
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
            String layoverDurationHour = layoverDuration.substring(0, 2);
            String layoverDurationMin = layoverDuration.substring(3);

            int layoverDurationHourInt = Integer.parseInt(layoverDurationHour);
            int layoverDurationMinInt = Integer.parseInt(layoverDurationMin);
            layoverDurationMinInt += (60 * layoverDurationHourInt);

            FlightSchedulePlanEntity newPlan = new FlightSchedulePlanEntity(FlightSchedulePlanTypeEnum.RECURRENT_DAY, startDate, endDate, layoverDurationMinInt, flight);

            try {
                flightSchedulePlanEntitySessionBeanRemote.replaceRecurrentSchedulePlan(plan, newPlan, dateFormat, departuretime, duration, recurringDays, layoverDurationMinInt);
                this.updateFare(sc, plan);
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

            //FlightSchedulePlanEntity newPlan = new FlightSchedulePlanEntity(FlightSchedulePlanTypeEnum.RECURRENT_WEEK, startDate, endDate, layoverDurationMinInt, flight);
//           try {              
//             flightSchedulePlanEntitySessionBeanRemote.replaceRecurrentSchedulePlan(plan, newPlan, dateFormat, departuretime, duration, 7, layoverDurationMinInt);
//              System.out.println("Schedule plan updated!");
//          } catch (ScheduleIsUsedException ex) {
//              System.out.println("Failed in updating schedule plan! At least one schedule is used!");
//              return;
//          } catch (ScheduleOverlapException ex) {
//              System.out.println("Failed in updating schedule plan! Updated scheduled overlapped with existing schedule!");
//              return;
//          }
            this.updateFare(sc, plan);
        }
    }

    public void updateSingleSchedule(Scanner sc, DateTimeFormatter dateFormat, FlightEntity flight, FlightSchedulePlanEntity plan,
            FlightScheduleEntity schedule) {
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
//
//        if (schedule.getReturnSchedule() != null) {
//            overlap = flightScheduleEntitySessionBeanRemote.checkOverlapPlan(flight, plan, flightSchedule, dateFormat);
//            returnOverlap = flightScheduleEntitySessionBeanRemote.checkOverlapPlan(flight, plan, returnFlightSchedule, dateFormat);
//        }

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

    public boolean checkOverlap(FlightScheduleEntity flightSchedule, FlightSchedulePlanEntity flightPlan, DateTimeFormatter dateFormat) {

        boolean overlap = false;
        FlightEntity flight = flightSchedulePlanEntitySessionBeanRemote.retrieveFlightFromPlan(flightPlan);

        List<FlightSchedulePlanEntity> plans = flightSchedulePlanEntitySessionBeanRemote.retrievePlanByFlight(flight);

        for (FlightSchedulePlanEntity plan : plans) {
            if (!plan.equals(flightPlan)) {
                List<FlightScheduleEntity> schedules = flightSchedulePlanEntitySessionBeanRemote.retrieveSchedulesByPlan(plan);

                for (FlightScheduleEntity schedule : schedules) {
                    ZonedDateTime newScheduleDep = ZonedDateTime.parse(flightSchedule.getDepartureDateTime(), dateFormat);
                    ZonedDateTime newScheduleArr = ZonedDateTime.parse(flightSchedule.getArrivalDateTime(), dateFormat);
                    ZonedDateTime oldScheduleDep = ZonedDateTime.parse(schedule.getDepartureDateTime(), dateFormat);
                    ZonedDateTime oldScheduleArr = ZonedDateTime.parse(schedule.getArrivalDateTime(), dateFormat);

                    if (newScheduleDep.isAfter(oldScheduleArr) || newScheduleArr.isBefore(oldScheduleDep)) {

                    } else {
                        return true;
                    }
                }
            }
        }

        return false;
    }

}
