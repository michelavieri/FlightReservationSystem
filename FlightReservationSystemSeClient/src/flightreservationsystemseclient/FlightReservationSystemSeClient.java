/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flightreservationsystemseclient;

import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import ws.client.holidayReservation.BookingTicketEntity;
import ws.client.holidayReservation.CabinClassConfigurationEntity;
import ws.client.holidayReservation.CabinClassTypeEnum;
import static ws.client.holidayReservation.CabinClassTypeEnum.BUSINESS_CLASS;
import static ws.client.holidayReservation.CabinClassTypeEnum.ECONOMY_CLASS;
import static ws.client.holidayReservation.CabinClassTypeEnum.FIRST_CLASS;
import static ws.client.holidayReservation.CabinClassTypeEnum.PREMIUM_ECONOMY_CLASS;
import ws.client.holidayReservation.CreditCardEntity;
import ws.client.holidayReservation.CustomerEntity;
import ws.client.holidayReservation.FareEntity;
import ws.client.holidayReservation.FlightEntity;
import ws.client.holidayReservation.FlightScheduleEntity;
import ws.client.holidayReservation.FlightTypeEnum;
import ws.client.holidayReservation.PartnerEntity;
import ws.client.holidayReservation.PassengerEntity;
import ws.client.holidayReservation.ReservationEntity;
import ws.client.holidayReservation.SeatEntity;
import ws.client.holidayReservation.SeatsInventoryEntity;

/**
 *
 * @author Chrisya
 */
public class FlightReservationSystemSeClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        menuMain(sc);

    }

    public static void menuMain(Scanner sc) {

        PartnerEntity partner = null;

        while (true) {
            System.out.println("*** Welcome, this is Holiday Reservation System Application ***");
            System.out.println("1: Partner Login");
            System.out.println("2: Search Flight");
            System.out.println("3: View All Reservations");
            System.out.println("4: View Reservation Details");
            System.out.println("5: Exit");
            int response = 0;

            while (response < 1 || response > 5) {
                System.out.print("> ");
                response = sc.nextInt();
                sc.nextLine();
                if (response == 1) {

                    System.out.print("Enter username >");
                    String username = sc.nextLine();
                    System.out.print("Enter password >");
                    String password = sc.nextLine();

                    partner = partnerLogin(username, password);
                    System.out.println("Login successful!");

                } else if (response == 2) {

                } else if (response == 3) {

                    viewMyFlightReservations(partner);

                } else if (response == 4) {

                    viewFlightReservationDetails(partner, sc);

                } else if (response == 5) {
                    break;
                }

                if (response == 5) {
                    break;
                }
            }
        }
    }

    private static PartnerEntity partnerLogin(java.lang.String arg0, java.lang.String arg1) {
        ws.client.holidayReservation.HolidayReservationService_Service service = new ws.client.holidayReservation.HolidayReservationService_Service();
        ws.client.holidayReservation.HolidayReservationService port = service.getHolidayReservationServicePort();
        return port.partnerLogin(arg0, arg1);
    }

    public static void viewMyFlightReservations(PartnerEntity partner) {
        List<ReservationEntity> reservations = retrieveFlightReservationsByPartner(partner.getUsername());

        if (reservations.isEmpty()) {
            System.out.println("No reservations made!");
        }

        for (ReservationEntity reservation : reservations) {
            System.out.println("RESERVATION ID: " + reservation.getReservationId());
            System.out.println("\t Number of Passengers: " + reservation.getNumOfPassengers());
            System.out.println("\t Total Amount Paid: " + reservation.getTotalAmount());
        }
    }

    public static void viewFlightReservationDetails(PartnerEntity partner, Scanner sc) {
        System.out.println("Enter reservation Id to view details> ");
        Long reservationId = sc.nextLong();
        ReservationEntity reservation = null;

        reservation = retrieveReservationByReservationIdPartner(reservationId, partner);

        List<BookingTicketEntity> tickets = null;

        tickets = retrieveTickets(reservationId);

        List<FlightScheduleEntity> outboundSchedules = new ArrayList<>();
        List<FlightScheduleEntity> returnSchedules = new ArrayList<>();

        List<BookingTicketEntity> outboundTickets = new ArrayList<>();
        List<BookingTicketEntity> returnTickets = new ArrayList<>();

        for (BookingTicketEntity ticket : tickets) {
            if (ticket.getFlightType().equals(FlightTypeEnum.OUTBOUND_FLIGHT)) {
                if (!outboundSchedules.contains(ticket.getFlightSchedule())) {
                    outboundSchedules.add(ticket.getFlightSchedule());
                }
                outboundTickets.add(ticket);
            } else {
                if (!returnSchedules.contains(ticket.getFlightSchedule())) {
                    returnSchedules.add(ticket.getFlightSchedule());
                }
                returnTickets.add(ticket);
            }
        }

        for (FlightScheduleEntity outboundSchedule : outboundSchedules) {
            FlightEntity outboundFlight = outboundSchedule.getPlan().getFlight();
            System.out.println("** OUTBOUND FLIGHT **");
            System.out.println("FLIGHT ITINERARY: ");
            System.out.println("\t Flight Number: " + outboundFlight.getFlightCode());
            System.out.println("\t Flight Route: " + outboundFlight.getRoute().getOriginAirport().getAirportCode() + " - "
                    + outboundFlight.getRoute().getDestinationAirport().getAirportCode());
            System.out.println("\t Flight Departure Time: " + outboundSchedule.getDepartureDateTime());
            System.out.println("\t Flight Arrival Time: " + outboundSchedule.getArrivalDateTime());
            System.out.println("\t Flight Duration: " + outboundSchedule.getDuration());
            System.out.println("RESERVATION DETAILS:");
            System.out.println("\t Number of passengers: " + reservation.getNumOfPassengers());
            System.out.println("\t Passengers:");
            for (BookingTicketEntity ticket : outboundTickets) {
                if (ticket.getFlightSchedule().equals(outboundSchedule)) {
                    System.out.println("\t\t Passenger name: " + ticket.getPassenger().getPassengerName());
                    System.out.println("\t\t Passenger's Passport number: " + ticket.getPassenger().getPassportNumber());
                    System.out.println("\t\t Cabin Class: " + searchCabinType(ticket.getSeat().getSeatsInventory().getCabinClass().getType()));
                    System.out.println("\t\t Seat Number: " + ticket.getSeat().getSeatNumber() + ticket.getSeat().getSeatLetter());
//            System.out.println("\t\t Fare Basis Code: " + ticket.getFare().getFareBasisCode());
                    System.out.println();
                }
            }
        }

        if (!returnTickets.isEmpty()) {
            for (FlightScheduleEntity returnSchedule : returnSchedules) {
                FlightEntity returnFlight = returnSchedule.getPlan().getFlight();
                System.out.println("** RETURN FLIGHT **");
                System.out.println("FLIGHT ITINERARY: ");
                System.out.println("\t Flight Number: " + returnFlight.getFlightCode());
                System.out.println("\t Flight Route: " + returnFlight.getRoute().getOriginAirport().getAirportCode() + " - "
                        + returnFlight.getRoute().getDestinationAirport().getAirportCode());
                System.out.println("\t Flight Departure Time: " + returnSchedule.getDepartureDateTime());
                System.out.println("\t Flight Arrival Time: " + returnSchedule.getArrivalDateTime());
                System.out.println("\t Flight Duration: " + returnSchedule.getDuration());
                System.out.println("RESERVATION DETAILS:");
                System.out.println("\t Number of passengers: " + reservation.getNumOfPassengers());
                System.out.println("\t Passengers:");
                for (BookingTicketEntity ticket : returnTickets) {
                    if (ticket.getFlightSchedule().equals(returnSchedule)) {
                        System.out.println("\t\t Passenger name: " + ticket.getPassenger().getPassengerName());
                        System.out.println("\t\t Passenger's Passport number: " + ticket.getPassenger().getPassportNumber());
                        System.out.println("\t\t Cabin Class: " + searchCabinType(ticket.getSeat().getSeatsInventory().getCabinClass().getType()));
                        System.out.println("\t\t Seat Number: " + ticket.getSeat().getSeatNumber() + ticket.getSeat().getSeatLetter());
                        System.out.println("\t\t Price per passenger: " + ticket.getFare().getAmount());
                        System.out.println();
                    }
                }
            }
        }
        System.out.println("TOTAL AMOUNT PAID: " + reservation.getTotalAmount());
    }

    public static String searchCabinType(CabinClassTypeEnum type) {
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

    public void searchFlights(Scanner sc, CustomerEntity customer) {
        boolean validReserve = true;
        long totalAmount = 0L;
        System.out.println("*** SEARCH FOR FLIGHTS ***");
        System.out.println("1: One-way Trip");
        System.out.println("2: Round Trip/Return");
        System.out.println("Enter trip type> ");
        int tripType = sc.nextInt();

        sc.nextLine();
        System.out.println("Enter departure airport code> ");
        String departureAirportCode = sc.nextLine();

        System.out.println("Enter destination airport> ");
        String destinationAirportCode = sc.nextLine();

        System.out.println("Enter departure date in this format: YYYY-MM-dd> ");
        String departureDate = sc.nextLine();

        String returnDate = null;
        if (tripType == 2) {
            System.out.println("Enter return date in this format: YYYY-MM-dd> ");
            returnDate = sc.nextLine();
        }

        System.out.println("Enter number of passengers> ");
        int numOfPassengers = sc.nextInt();

        System.out.println("Do you have preference for Direct Flight or Connecting Flight?");
        System.out.println("1: Direct Flight");
        System.out.println("2: No preference for Direct or Connecting Flight");
        int preferenceFlight = sc.nextInt();

        int stopovers = 10;

        System.out.println("Do you have preference for Cabin Class?");
        System.out.println("1: First Class");
        System.out.println("2: Business Class");
        System.out.println("3: Premium Economy Class");
        System.out.println("4: Economy Class");
        System.out.println("5: No Preference");
        int preferenceClass = sc.nextInt();

        CabinClassTypeEnum preferenceClassEnum;
        switch (preferenceClass) {
            case 1:
                preferenceClassEnum = CabinClassTypeEnum.FIRST_CLASS;
                break;
            case 2:
                preferenceClassEnum = CabinClassTypeEnum.BUSINESS_CLASS;
                break;
            case 3:
                preferenceClassEnum = CabinClassTypeEnum.PREMIUM_ECONOMY_CLASS;
                break;
            case 4:
                preferenceClassEnum = CabinClassTypeEnum.ECONOMY_CLASS;
                break;
            default:
                preferenceClassEnum = null;
                break;
        }

        List<List<FlightScheduleEntity>> outboundFlightsSameDate;
        List<List<FlightScheduleEntity>> outboundFlightsBeforeDate;
        List<List<FlightScheduleEntity>> outboundFlightsAfterDate;
        if (preferenceFlight == 1) {
            outboundFlightsSameDate = searchDirectFlights(departureAirportCode, destinationAirportCode, departureDate, numOfPassengers, preferenceClassEnum);
            outboundFlightsBeforeDate = searchDirectFlightsBefore(departureAirportCode, destinationAirportCode, departureDate, numOfPassengers, preferenceClassEnum);
            outboundFlightsAfterDate = searchDirectFlightsAfter(departureAirportCode, destinationAirportCode, departureDate, numOfPassengers, preferenceClassEnum);
        } else {
            outboundFlightsSameDate = searchConnectingFlights(departureAirportCode, destinationAirportCode, departureDate, numOfPassengers, stopovers, preferenceClassEnum);
            outboundFlightsBeforeDate = searchConnectingFlightsBefore(departureAirportCode, destinationAirportCode, departureDate, numOfPassengers, stopovers, preferenceClassEnum);
            outboundFlightsAfterDate = searchConnectingFlightsAfter(departureAirportCode, destinationAirportCode, departureDate, numOfPassengers, stopovers, preferenceClassEnum);
        }
        List<List<FlightScheduleEntity>> returnFlightsSameDate = new ArrayList<>();
        List<List<FlightScheduleEntity>> returnFlightsBeforeDate = new ArrayList<>();
        List<List<FlightScheduleEntity>> returnFlightsAfterDate = new ArrayList<>();
        if (tripType == 2) {
            if (preferenceFlight == 1) {
                returnFlightsSameDate = searchDirectFlights(destinationAirportCode, departureAirportCode, returnDate, numOfPassengers, preferenceClassEnum);
                returnFlightsBeforeDate = searchDirectFlightsBefore(destinationAirportCode, departureAirportCode, returnDate, numOfPassengers, preferenceClassEnum);
                returnFlightsAfterDate = searchDirectFlightsAfter(destinationAirportCode, departureAirportCode, returnDate, numOfPassengers, preferenceClassEnum);
            } else {
                returnFlightsSameDate = searchConnectingFlights(destinationAirportCode, departureAirportCode, returnDate, numOfPassengers, stopovers, preferenceClassEnum);
                returnFlightsBeforeDate = searchConnectingFlightsBefore(destinationAirportCode, departureAirportCode, returnDate, numOfPassengers, stopovers, preferenceClassEnum);
                returnFlightsAfterDate = searchConnectingFlightsAfter(destinationAirportCode, departureAirportCode, returnDate, numOfPassengers, stopovers, preferenceClassEnum);
            }
        }

        System.out.println("*** SEARCH RESULTS: ***");
        System.out.println("OUTBOUND FLIGHTS: ");
        System.out.println("Departure Date " + departureDate + ":");
        if (outboundFlightsSameDate.size() == 0) {
            System.out.println("No Departure Flights on " + returnDate);
        }

        int pricePerPassenger = 0;
        for (int i = 0; i < outboundFlightsSameDate.size(); i++) {

            List<FlightScheduleEntity> schedules = outboundFlightsSameDate.get(i);
            long priceFirstClass = 0;
            long priceBusinessClass = 0;
            long pricePremiumEconomyClass = 0;
            long priceEconomyClass = 0;
            if (schedules.size() == 1) {
                System.out.println((i + 1) + ". DIRECT FLIGHT : ");
            } else {
                System.out.println((i + 1) + ". CONNECTING FLIGHTS: ");
            }
            List<CabinClassConfigurationEntity> classes = new ArrayList<>();
            for (int j = 0; j < schedules.size(); j++) {
                System.out.println("\t -SCHEDULE ID: " + schedules.get(j).getScheduleId());
                System.out.println("\t  " + schedules.get(j).getPlan().getFlight().getRoute().getOriginAirport().getAirportCode()
                        + " - " + schedules.get(j).getPlan().getFlight().getRoute().getDestinationAirport().getAirportCode() + " ("
                        + schedules.get(j).getPlan().getFlight().getFlightCode() + ")");
                System.out.println("\t  Departure Time: " + schedules.get(j).getDepartureDateTime());
                System.out.println("\t  Arrival Time: " + schedules.get(j).getArrivalDateTime());
                classes = getCabinClassConfig(schedules.get(j).getPlan().getFlight().getAircraftConfigurationEntity());
                System.out.println("\t  Available Classes: ");
                for (CabinClassConfigurationEntity c : classes) {
                    System.out.println("\t\t " + searchCabinType(c.getType()));
                    switch (c.getType()) {
                        case FIRST_CLASS:
                            priceFirstClass += Long.parseLong(retrieveHighestFareUnmanaged(schedules.get(j), CabinClassTypeEnum.FIRST_CLASS).getAmount());
                            break;
                        case BUSINESS_CLASS:
                            priceBusinessClass += Long.parseLong(retrieveHighestFareUnmanaged(schedules.get(j), CabinClassTypeEnum.BUSINESS_CLASS).getAmount());
                            break;
                        case PREMIUM_ECONOMY_CLASS:
                            pricePremiumEconomyClass += Long.parseLong(retrieveHighestFareUnmanaged(schedules.get(j), CabinClassTypeEnum.PREMIUM_ECONOMY_CLASS).getAmount());
                            break;
                        case ECONOMY_CLASS:
                            priceEconomyClass += Long.parseLong(retrieveHighestFareUnmanaged(schedules.get(j), CabinClassTypeEnum.ECONOMY_CLASS).getAmount());
                            break;
                        default:
                            break;
                    }
                }
                if (preferenceClassEnum != null) {
                    pricePerPassenger += Long.parseLong(retrieveHighestFareUnmanaged(schedules.get(j), preferenceClassEnum).getAmount());
                }
            }
            System.out.println();
            if (preferenceClassEnum != null) {
                System.out.println("   Price: ");
                System.out.println("   \t Price per passenger for " + searchCabinType(preferenceClassEnum) + ": " + pricePerPassenger);
                System.out.println("   \t Total Price: " + pricePerPassenger * numOfPassengers);
            } else {
                System.out.println("   Price: ");
                for (CabinClassConfigurationEntity c : classes) {
                    System.out.println("\t " + searchCabinType(c.getType()) + ":");
                    System.out.print("\t\t Price per passenger: ");
                    switch (c.getType()) {
                        case FIRST_CLASS:
                            System.out.println(priceFirstClass);
                            System.out.println("\t\t Total Price: " + priceFirstClass * numOfPassengers);
                            break;
                        case BUSINESS_CLASS:
                            System.out.println(priceBusinessClass);
                            System.out.println("\t\t Total Price: " + priceBusinessClass * numOfPassengers);
                            break;
                        case PREMIUM_ECONOMY_CLASS:
                            System.out.println(pricePremiumEconomyClass);
                            System.out.println("\t\t Total Price: " + pricePremiumEconomyClass * numOfPassengers);
                            break;
                        case ECONOMY_CLASS:
                            System.out.println(priceEconomyClass);
                            System.out.println("\t\t Total Price: " + priceEconomyClass * numOfPassengers);
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        System.out.println("***");
        System.out.println("Departure Date 3/2/1 Days before " + departureDate + ":");
        if (outboundFlightsBeforeDate.size() == 0) {
            System.out.println("No Departure Flights for 3/2/1 Days before " + returnDate);
        }

        pricePerPassenger = 0;
        for (int i = 0; i < outboundFlightsBeforeDate.size(); i++) {
            List<FlightScheduleEntity> schedules = outboundFlightsBeforeDate.get(i);
            long priceFirstClass = 0;
            long priceBusinessClass = 0;
            long pricePremiumEconomyClass = 0;
            long priceEconomyClass = 0;
            if (schedules.size() == 1) {
                System.out.println((i + 1) + ". DIRECT FLIGHT : ");
            } else {
                System.out.println((i + 1) + ". CONNECTING FLIGHTS: ");
            }
            List<CabinClassConfigurationEntity> classes = new ArrayList<>();
            for (int j = 0; j < schedules.size(); j++) {
                System.out.println("\t -SCHEDULE ID: " + schedules.get(j).getScheduleId());
                System.out.println("\t  " + schedules.get(j).getPlan().getFlight().getRoute().getOriginAirport().getAirportCode()
                        + " - " + schedules.get(j).getPlan().getFlight().getRoute().getDestinationAirport().getAirportCode() + " ("
                        + schedules.get(j).getPlan().getFlight().getFlightCode() + ")");
                System.out.println("\t  Departure Time: " + schedules.get(j).getDepartureDateTime());
                System.out.println("\t  Arrival Time: " + schedules.get(j).getArrivalDateTime());
                classes = getCabinClassConfig(schedules.get(j).getPlan().getFlight().getAircraftConfigurationEntity());
                System.out.println("\t  Available Classes: ");
                for (CabinClassConfigurationEntity c : classes) {
                    System.out.println("\t\t " + searchCabinType(c.getType()));
                    switch (c.getType()) {
                        case FIRST_CLASS:
                            priceFirstClass += Long.parseLong(retrieveHighestFareUnmanaged(schedules.get(j), CabinClassTypeEnum.FIRST_CLASS).getAmount());
                            break;
                        case BUSINESS_CLASS:
                            priceBusinessClass += Long.parseLong(retrieveHighestFareUnmanaged(schedules.get(j), CabinClassTypeEnum.BUSINESS_CLASS).getAmount());
                            break;
                        case PREMIUM_ECONOMY_CLASS:
                            pricePremiumEconomyClass += Long.parseLong(retrieveHighestFareUnmanaged(schedules.get(j), CabinClassTypeEnum.PREMIUM_ECONOMY_CLASS).getAmount());
                            break;
                        case ECONOMY_CLASS:
                            priceEconomyClass += Long.parseLong(retrieveHighestFareUnmanaged(schedules.get(j), CabinClassTypeEnum.ECONOMY_CLASS).getAmount());
                            break;
                        default:
                            break;
                    }
                }
                if (preferenceClassEnum != null) {
                    pricePerPassenger += Long.parseLong(retrieveHighestFareUnmanaged(schedules.get(j), preferenceClassEnum).getAmount());
                }
            }
            System.out.println();
            if (preferenceClassEnum != null) {
                System.out.println("   Price: ");
                System.out.println("   \t Price per passenger for " + searchCabinType(preferenceClassEnum) + ": " + pricePerPassenger);
                System.out.println("   \t Total Price: " + pricePerPassenger * numOfPassengers);
            } else {
                System.out.println("   Price: ");
                for (CabinClassConfigurationEntity c : classes) {
                    System.out.println("\t " + searchCabinType(c.getType()) + ":");
                    System.out.print("\t\t Price per passenger: ");
                    switch (c.getType()) {
                        case FIRST_CLASS:
                            System.out.println(priceFirstClass);
                            System.out.println("\t\t Total Price: " + priceFirstClass * numOfPassengers);
                            break;
                        case BUSINESS_CLASS:
                            System.out.println(priceBusinessClass);
                            System.out.println("\t\t Total Price: " + priceBusinessClass * numOfPassengers);
                            break;
                        case PREMIUM_ECONOMY_CLASS:
                            System.out.println(pricePremiumEconomyClass);
                            System.out.println("\t\t Total Price: " + pricePremiumEconomyClass * numOfPassengers);
                            break;
                        case ECONOMY_CLASS:
                            System.out.println(priceEconomyClass);
                            System.out.println("\t\t Total Price: " + priceEconomyClass * numOfPassengers);
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        System.out.println("***");

        System.out.println("Departure Date 3/2/1 Days after " + departureDate + ":");
        if (outboundFlightsAfterDate.size() == 0) {
            System.out.println("No Departure Flights for 3/2/1 Days after " + returnDate);
        }

        pricePerPassenger = 0;
        for (int i = 0; i < outboundFlightsAfterDate.size(); i++) {
            List<FlightScheduleEntity> schedules = outboundFlightsAfterDate.get(i);
            long priceFirstClass = 0;
            long priceBusinessClass = 0;
            long pricePremiumEconomyClass = 0;
            long priceEconomyClass = 0;
            if (schedules.size() == 1) {
                System.out.println((i + 1) + ". DIRECT FLIGHT : ");
            } else {
                System.out.println((i + 1) + ". CONNECTING FLIGHTS: ");
            }
            List<CabinClassConfigurationEntity> classes = new ArrayList<>();
            for (int j = 0; j < schedules.size(); j++) {
                System.out.println("\t -SCHEDULE ID: " + schedules.get(j).getScheduleId());
                System.out.println("\t  " + schedules.get(j).getPlan().getFlight().getRoute().getOriginAirport().getAirportCode()
                        + " - " + schedules.get(j).getPlan().getFlight().getRoute().getDestinationAirport().getAirportCode() + " ("
                        + schedules.get(j).getPlan().getFlight().getFlightCode() + ")");
                System.out.println("\t  Departure Time: " + schedules.get(j).getDepartureDateTime());
                System.out.println("\t  Arrival Time: " + schedules.get(j).getArrivalDateTime());
                classes = getCabinClassConfig(schedules.get(j).getPlan().getFlight().getAircraftConfigurationEntity());
                System.out.println("\t  Available Classes: ");
                for (CabinClassConfigurationEntity c : classes) {
                    System.out.println("\t\t " + searchCabinType(c.getType()));
                    switch (c.getType()) {
                        case FIRST_CLASS:
                            priceFirstClass += Long.parseLong(retrieveHighestFareUnmanaged(schedules.get(j), CabinClassTypeEnum.FIRST_CLASS).getAmount());
                            break;
                        case BUSINESS_CLASS:
                            priceBusinessClass += Long.parseLong(retrieveHighestFareUnmanaged(schedules.get(j), CabinClassTypeEnum.BUSINESS_CLASS).getAmount());
                            break;
                        case PREMIUM_ECONOMY_CLASS:
                            pricePremiumEconomyClass += Long.parseLong(retrieveHighestFareUnmanaged(schedules.get(j), CabinClassTypeEnum.PREMIUM_ECONOMY_CLASS).getAmount());
                            break;
                        case ECONOMY_CLASS:
                            priceEconomyClass += Long.parseLong(retrieveHighestFareUnmanaged(schedules.get(j), CabinClassTypeEnum.ECONOMY_CLASS).getAmount());
                            break;
                        default:
                            break;
                    }
                }
                if (preferenceClassEnum != null) {
                    pricePerPassenger += Long.parseLong(retrieveHighestFareUnmanaged(schedules.get(j), preferenceClassEnum).getAmount());
                }
            }
            System.out.println();
            if (preferenceClassEnum != null) {
                System.out.println("   Price: ");
                System.out.println("   \t Price per passenger for " + searchCabinType(preferenceClassEnum) + ": " + pricePerPassenger);
                System.out.println("   \t Total Price: " + pricePerPassenger * numOfPassengers);
            } else {
                System.out.println("   Price: ");
                for (CabinClassConfigurationEntity c : classes) {
                    System.out.println("\t " + searchCabinType(c.getType()) + ":");
                    System.out.print("\t\t Price per passenger: ");
                    switch (c.getType()) {
                        case FIRST_CLASS:
                            System.out.println(priceFirstClass);
                            System.out.println("\t\t Total Price: " + priceFirstClass * numOfPassengers);
                            break;
                        case BUSINESS_CLASS:
                            System.out.println(priceBusinessClass);
                            System.out.println("\t\t Total Price: " + priceBusinessClass * numOfPassengers);
                            break;
                        case PREMIUM_ECONOMY_CLASS:
                            System.out.println(pricePremiumEconomyClass);
                            System.out.println("\t\t Total Price: " + pricePremiumEconomyClass * numOfPassengers);
                            break;
                        case ECONOMY_CLASS:
                            System.out.println(priceEconomyClass);
                            System.out.println("\t\t Total Price: " + priceEconomyClass * numOfPassengers);
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        if (tripType == 2) {
            System.out.println("***********************************");
            System.out.println("RETURN FLIGHTS: ");

            System.out.println("Return Date " + returnDate + ":");
            if (returnFlightsSameDate.size() == 0) {
                System.out.println("No Return Flights on " + returnDate);
            }

            pricePerPassenger = 0;
            for (int i = 0; i < returnFlightsSameDate.size(); i++) {

                List<FlightScheduleEntity> schedules = returnFlightsSameDate.get(i);
                long priceFirstClass = 0;
                long priceBusinessClass = 0;
                long pricePremiumEconomyClass = 0;
                long priceEconomyClass = 0;
                if (schedules.size() == 1) {
                    System.out.println((i + 1) + ". DIRECT FLIGHT : ");
                } else {
                    System.out.println((i + 1) + ". CONNECTING FLIGHTS: ");
                }
                List<CabinClassConfigurationEntity> classes = new ArrayList<>();
                for (int j = 0; j < schedules.size(); j++) {
                    System.out.println("\t -SCHEDULE ID: " + schedules.get(j).getScheduleId());
                    System.out.println("\t  " + schedules.get(j).getPlan().getFlight().getRoute().getOriginAirport().getAirportCode()
                            + " - " + schedules.get(j).getPlan().getFlight().getRoute().getDestinationAirport().getAirportCode() + " ("
                            + schedules.get(j).getPlan().getFlight().getFlightCode() + ")");
                    System.out.println("\t  Departure Time: " + schedules.get(j).getDepartureDateTime());
                    System.out.println("\t  Arrival Time: " + schedules.get(j).getArrivalDateTime());
                    classes = getCabinClassConfig(schedules.get(j).getPlan().getFlight().getAircraftConfigurationEntity());
                    System.out.println("\t  Available Classes: ");
                    for (CabinClassConfigurationEntity c : classes) {
                        System.out.println("\t\t " + searchCabinType(c.getType()));
                        switch (c.getType()) {
                            case FIRST_CLASS:
                                priceFirstClass += Long.parseLong(retrieveHighestFareUnmanaged(schedules.get(j), CabinClassTypeEnum.FIRST_CLASS).getAmount());
                                break;
                            case BUSINESS_CLASS:
                                priceBusinessClass += Long.parseLong(retrieveHighestFareUnmanaged(schedules.get(j), CabinClassTypeEnum.BUSINESS_CLASS).getAmount());
                                break;
                            case PREMIUM_ECONOMY_CLASS:
                                pricePremiumEconomyClass += Long.parseLong(retrieveHighestFareUnmanaged(schedules.get(j), CabinClassTypeEnum.PREMIUM_ECONOMY_CLASS).getAmount());
                                break;
                            case ECONOMY_CLASS:
                                priceEconomyClass += Long.parseLong(retrieveHighestFareUnmanaged(schedules.get(j), CabinClassTypeEnum.ECONOMY_CLASS).getAmount());
                                break;
                            default:
                                break;
                        }
                    }
                    if (preferenceClassEnum != null) {
                        pricePerPassenger += Long.parseLong(retrieveHighestFareUnmanaged(schedules.get(j), preferenceClassEnum).getAmount());
                    }
                }
                System.out.println();
                if (preferenceClassEnum != null) {
                    System.out.println("   Price: ");
                    System.out.println("   \t Price per passenger for " + searchCabinType(preferenceClassEnum) + ": " + pricePerPassenger);
                    System.out.println("   \t Total Price: " + pricePerPassenger * numOfPassengers);
                } else {
                    System.out.println("   Price: ");
                    for (CabinClassConfigurationEntity c : classes) {
                        System.out.println("\t " + searchCabinType(c.getType()));
                        System.out.print("\t\t Price per passenger: ");
                        switch (c.getType()) {
                            case FIRST_CLASS:
                                System.out.println(priceFirstClass);
                                System.out.println("\t\t Total Price: " + priceFirstClass * numOfPassengers);
                                break;
                            case BUSINESS_CLASS:
                                System.out.println(priceBusinessClass);
                                System.out.println("\t\t Total Price: " + priceBusinessClass * numOfPassengers);
                                break;
                            case PREMIUM_ECONOMY_CLASS:
                                System.out.println(pricePremiumEconomyClass);
                                System.out.println("\t\t Total Price: " + pricePremiumEconomyClass * numOfPassengers);
                                break;
                            case ECONOMY_CLASS:
                                System.out.println(priceEconomyClass);
                                System.out.println("\t\t Total Price: " + priceEconomyClass * numOfPassengers);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }

            System.out.println("***");
            System.out.println("Return Date 3/2/1 Days before " + returnDate + ":");
            if (returnFlightsBeforeDate.size() == 0) {
                System.out.println("No Return Flights for 3/2/1 Days before " + returnDate);
            }

            pricePerPassenger = 0;
            for (int i = 0; i < returnFlightsBeforeDate.size(); i++) {
                List<FlightScheduleEntity> schedules = returnFlightsBeforeDate.get(i);
                long priceFirstClass = 0;
                long priceBusinessClass = 0;
                long pricePremiumEconomyClass = 0;
                long priceEconomyClass = 0;
                if (schedules.size() == 1) {
                    System.out.println((i + 1) + ". DIRECT FLIGHT : ");
                } else {
                    System.out.println((i + 1) + ". CONNECTING FLIGHTS: ");
                }
                List<CabinClassConfigurationEntity> classes = new ArrayList<>();
                for (int j = 0; j < schedules.size(); j++) {
                    System.out.println("\t -SCHEDULE ID: " + schedules.get(j).getScheduleId());
                    System.out.println("\t  " + schedules.get(j).getPlan().getFlight().getRoute().getOriginAirport().getAirportCode()
                            + " - " + schedules.get(j).getPlan().getFlight().getRoute().getDestinationAirport().getAirportCode() + " ("
                            + schedules.get(j).getPlan().getFlight().getFlightCode() + ")");
                    System.out.println("\t  Departure Time: " + schedules.get(j).getDepartureDateTime());
                    System.out.println("\t  Arrival Time: " + schedules.get(j).getArrivalDateTime());
                    classes = getCabinClassConfig(schedules.get(j).getPlan().getFlight().getAircraftConfigurationEntity());
                    System.out.println("\t  Available Classes: ");
                    for (CabinClassConfigurationEntity c : classes) {
                        System.out.println("\t\t " + searchCabinType(c.getType()));
                        switch (c.getType()) {
                            case FIRST_CLASS:
                                priceFirstClass += Long.parseLong(retrieveHighestFareUnmanaged(schedules.get(j), CabinClassTypeEnum.FIRST_CLASS).getAmount());
                                break;
                            case BUSINESS_CLASS:
                                priceBusinessClass += Long.parseLong(retrieveHighestFareUnmanaged(schedules.get(j), CabinClassTypeEnum.BUSINESS_CLASS).getAmount());
                                break;
                            case PREMIUM_ECONOMY_CLASS:
                                pricePremiumEconomyClass += Long.parseLong(retrieveHighestFareUnmanaged(schedules.get(j), CabinClassTypeEnum.PREMIUM_ECONOMY_CLASS).getAmount());
                                break;
                            case ECONOMY_CLASS:
                                priceEconomyClass += Long.parseLong(retrieveHighestFareUnmanaged(schedules.get(j), CabinClassTypeEnum.ECONOMY_CLASS).getAmount());
                                break;
                            default:
                                break;
                        }
                    }
                    if (preferenceClassEnum != null) {
                        pricePerPassenger += Long.parseLong(retrieveHighestFareUnmanaged(schedules.get(j), preferenceClassEnum).getAmount());
                    }
                }
                System.out.println();
                if (preferenceClassEnum != null) {
                    System.out.println("   Price: ");
                    System.out.println("   \t Price per passenger for " + searchCabinType(preferenceClassEnum) + ": " + pricePerPassenger);
                    System.out.println("   \t Total Price: " + pricePerPassenger * numOfPassengers);
                } else {
                    System.out.println("   Price: ");
                    for (CabinClassConfigurationEntity c : classes) {
                        System.out.println("\t " + searchCabinType(c.getType()) + ":");
                        System.out.print("\t\t Price per passenger: ");
                        switch (c.getType()) {
                            case FIRST_CLASS:
                                System.out.println(priceFirstClass);
                                System.out.println("\t\t Total Price: " + priceFirstClass * numOfPassengers);
                                break;
                            case BUSINESS_CLASS:
                                System.out.println(priceBusinessClass);
                                System.out.println("\t\t Total Price: " + priceBusinessClass * numOfPassengers);
                                break;
                            case PREMIUM_ECONOMY_CLASS:
                                System.out.println(pricePremiumEconomyClass);
                                System.out.println("\t\t Total Price: " + pricePremiumEconomyClass * numOfPassengers);
                                break;
                            case ECONOMY_CLASS:
                                System.out.println(priceEconomyClass);
                                System.out.println("\t\t Total Price: " + priceEconomyClass * numOfPassengers);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }

            System.out.println("***");

            System.out.println("Return Date 3/2/1 Days after " + returnDate + ":");
            if (returnFlightsAfterDate.size() == 0) {
                System.out.println("No Return Flights for 3/2/1 Days after " + returnDate + ": ");
            }

            pricePerPassenger = 0;
            for (int i = 0; i < returnFlightsAfterDate.size(); i++) {
                List<FlightScheduleEntity> schedules = returnFlightsAfterDate.get(i);
                long priceFirstClass = 0;
                long priceBusinessClass = 0;
                long pricePremiumEconomyClass = 0;
                long priceEconomyClass = 0;
                if (schedules.size() == 1) {
                    System.out.println((i + 1) + ". DIRECT FLIGHT : ");
                } else {
                    System.out.println((i + 1) + ". CONNECTING FLIGHTS: ");
                }
                List<CabinClassConfigurationEntity> classes = new ArrayList<>();
                for (int j = 0; j < schedules.size(); j++) {
                    System.out.println("\t -SCHEDULE ID: " + schedules.get(j).getScheduleId());
                    System.out.println("\t  " + schedules.get(j).getPlan().getFlight().getRoute().getOriginAirport().getAirportCode()
                            + " - " + schedules.get(j).getPlan().getFlight().getRoute().getDestinationAirport().getAirportCode() + " ("
                            + schedules.get(j).getPlan().getFlight().getFlightCode() + ")");
                    System.out.println("\t  Departure Time: " + schedules.get(j).getDepartureDateTime());
                    System.out.println("\t  Arrival Time: " + schedules.get(j).getArrivalDateTime());
                    classes = getCabinClassConfig(schedules.get(j).getPlan().getFlight().getAircraftConfigurationEntity());
                    System.out.println("\t  Available Classes: ");
                    for (CabinClassConfigurationEntity c : classes) {
                        System.out.println("\t\t " + searchCabinType(c.getType()));
                        switch (c.getType()) {
                            case FIRST_CLASS:
                                priceFirstClass += Long.parseLong(retrieveHighestFareUnmanaged(schedules.get(j), CabinClassTypeEnum.FIRST_CLASS).getAmount());
                                break;
                            case BUSINESS_CLASS:
                                priceBusinessClass += Long.parseLong(retrieveHighestFareUnmanaged(schedules.get(j), CabinClassTypeEnum.BUSINESS_CLASS).getAmount());
                                break;
                            case PREMIUM_ECONOMY_CLASS:
                                pricePremiumEconomyClass += Long.parseLong(retrieveHighestFareUnmanaged(schedules.get(j), CabinClassTypeEnum.PREMIUM_ECONOMY_CLASS).getAmount());
                                break;
                            case ECONOMY_CLASS:
                                priceEconomyClass += Long.parseLong(retrieveHighestFareUnmanaged(schedules.get(j), CabinClassTypeEnum.ECONOMY_CLASS).getAmount());
                                break;
                            default:
                                break;
                        }
                    }
                    if (preferenceClassEnum != null) {
                        pricePerPassenger += Long.parseLong(retrieveHighestFareUnmanaged(schedules.get(j), preferenceClassEnum).getAmount());
                    }
                }
                System.out.println();
                if (preferenceClassEnum != null) {
                    System.out.println("   Price: ");
                    System.out.println("   \t Price per passenger for " + searchCabinType(preferenceClassEnum) + ": " + pricePerPassenger);
                    System.out.println("   \t Total Price: " + pricePerPassenger * numOfPassengers);
                } else {
                    System.out.println("   Price: ");
                    for (CabinClassConfigurationEntity c : classes) {
                        System.out.println("\t " + searchCabinType(c.getType()) + ":");
                        System.out.print("\t\t Price per passenger: ");
                        switch (c.getType()) {
                            case FIRST_CLASS:
                                System.out.println(priceFirstClass);
                                System.out.println("\t\t Total Price: " + priceFirstClass * numOfPassengers);
                                break;
                            case BUSINESS_CLASS:
                                System.out.println(priceBusinessClass);
                                System.out.println("\t\t Total Price: " + priceBusinessClass * numOfPassengers);
                                break;
                            case PREMIUM_ECONOMY_CLASS:
                                System.out.println(pricePremiumEconomyClass);
                                System.out.println("\t\t Total Price: " + pricePremiumEconomyClass * numOfPassengers);
                                break;
                            case ECONOMY_CLASS:
                                System.out.println(priceEconomyClass);
                                System.out.println("\t\t Total Price: " + priceEconomyClass * numOfPassengers);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
        if (customer == null) {
            System.out.println("Please sign in if you want to reserve a flight!");
            return;
        }
        sc.nextLine();
        System.out.println("Do you want to reserve flight? (Y/N)");
        String reserveFlight = sc.nextLine();

        if (reserveFlight.equals("N")) {
            return;
        } else {
            System.out.println("Enter your trip type:");
            System.out.println("1: One-way Trip");
            System.out.println("2: Round/Return Trip");
            tripType = sc.nextInt();
            sc.nextLine();

            System.out.println("Are you reserving a connecting flight? (Y/N)");
            String connectingFlightQuery = sc.nextLine();

            if (connectingFlightQuery.equals("N")) {
                HashSet<SeatEntity> seatOutbound = new HashSet<>();
                HashSet<SeatEntity> seatReturn = new HashSet<>();

                System.out.println("Enter the Schedule ID for Outbound Flight> ");
                long outboundId = sc.nextLong();

                long returnId = 0L;
                if (tripType == 2) {
                    System.out.println("Enter the Schedule ID for Return Flight> ");
                    returnId = sc.nextLong();
                }
                FlightScheduleEntity outboundFlightSchedule = retrieveFlightScheduleByIdUnmanaged(outboundId);
                FlightScheduleEntity returnFlightSchedule = null;
                if (tripType == 2) {
                    returnFlightSchedule = retrieveFlightScheduleByIdUnmanaged(returnId);
                }
                sc.nextLine();
                List<BookingTicketEntity> tickets = new ArrayList<>();
                for (int i = 0; i < numOfPassengers; i++) {
                    System.out.print((i + 1) + ". ");
                    System.out.println("Enter the passenger name>");
                    String passengerName = sc.nextLine();
                    System.out.println("Enter the pasport number>");
                    String passportNumber = sc.nextLine();
                    PassengerEntity passenger = new PassengerEntity(passengerName, passportNumber);
                    System.out.println("CHOOSE SEAT FOR OUTBOUND FLIGHT:");
                    System.out.println("Choose cabin class type: ");
                    System.out.println("1: FIRST CLASS");
                    System.out.println("2: BUSINESS CLASS");
                    System.out.println("3: PREMIUM ECONOMY CLASS");
                    System.out.println("4: ECONOMY CLASS");
                    preferenceClass = sc.nextInt();
                    switch (preferenceClass) {
                        case 1:
                            preferenceClassEnum = CabinClassTypeEnum.FIRST_CLASS;
                            break;
                        case 2:
                            preferenceClassEnum = CabinClassTypeEnum.BUSINESS_CLASS;
                            break;
                        case 3:
                            preferenceClassEnum = CabinClassTypeEnum.PREMIUM_ECONOMY_CLASS;
                            break;
                        case 4:
                            preferenceClassEnum = CabinClassTypeEnum.ECONOMY_CLASS;
                            break;
                        default:
                            preferenceClassEnum = null;
                            break;
                    }

                    printAirplaneSeats(outboundFlightSchedule, preferenceClassEnum);
                    sc.nextLine();
                    System.out.print("Enter seat number - DO NOT ENTER THE LETTER (e.g. 12)> ");
                    int seatNumber = sc.nextInt();

                    sc.nextLine();
                    System.out.print("Enter seat letter - ONLY THE LETTER (e.g. A)> ");
                    String seatLetter = sc.nextLine();
                    SeatsInventoryEntity seatsInventory = null;
                    try {
                        seatsInventory = seatsInventoryEntitySessionBeanRemote.retrieveSeatsInventoryByScheduleIdClass(outboundId, preferenceClassEnum);
                    } catch (NoCabinClassException ex) {
                        System.out.println(ex.getMessage());
                        validReserve = false;
                        break;
                    } catch (FlightScheduleNotFoundException ex) {
                        System.out.println(ex.getMessage());
                        validReserve = false;
                        break;
                    }
                    SeatEntity seat = seatEntitySessionBeanRemote.retrieveSeat(seatNumber, seatLetter, seatsInventory.getInventoryId());

                    if (seat.isBooked() || seatOutbound.contains(seat)) {
                        System.out.println("This seat has been booked!");
                        validReserve = false;
                        break;
                    }
                    FareEntity fare = retrieveHighestFareUnmanaged(outboundFlightSchedule, preferenceClassEnum);
                    totalAmount += Integer.parseInt(fare.getAmount());
                    seatOutbound.add(seat);
                    BookingTicketEntity ticket = new BookingTicketEntity(passenger, seat, fare, outboundFlightSchedule, FlightTypeEnum.OUTBOUND_FLIGHT);
                    tickets.add(ticket);

                    if (tripType == 2) {
                        System.out.println("CHOOSE SEAT FOR RETURN FLIGHT:");
                        System.out.println("Choose cabin class type: ");
                        System.out.println("1: FIRST CLASS");
                        System.out.println("2: BUSINESS CLASS");
                        System.out.println("3: PREMIUM ECONOMY CLASS");
                        System.out.println("4: ECONOMY CLASS");
                        preferenceClass = sc.nextInt();
                        switch (preferenceClass) {
                            case 1:
                                preferenceClassEnum = CabinClassTypeEnum.FIRST_CLASS;
                                break;
                            case 2:
                                preferenceClassEnum = CabinClassTypeEnum.BUSINESS_CLASS;
                                break;
                            case 3:
                                preferenceClassEnum = CabinClassTypeEnum.PREMIUM_ECONOMY_CLASS;
                                break;
                            case 4:
                                preferenceClassEnum = CabinClassTypeEnum.ECONOMY_CLASS;
                                break;
                            default:
                                preferenceClassEnum = null;
                                break;
                        }

                        printAirplaneSeats(outboundFlightSchedule, preferenceClassEnum);
                        sc.nextLine();
                        System.out.print("Enter seat number (e.g. 12)> ");
                        seatNumber = sc.nextInt();

                        sc.nextLine();
                        System.out.print("Enter seat letter(e.g. A)> ");
                        seatLetter = sc.nextLine();

                        seatsInventory = null;
                        try {
                            seatsInventory = seatsInventoryEntitySessionBeanRemote.retrieveSeatsInventoryByScheduleIdClass(returnId, preferenceClassEnum);
                        } catch (NoCabinClassException ex) {
                            System.out.println(ex.getMessage());
                            validReserve = false;
                            break;
                        } catch (FlightScheduleNotFoundException ex) {
                            System.out.println(ex.getMessage());
                            validReserve = false;
                            break;
                        }

                        if (seat.isBooked() || seatReturn.contains(seat)) {
                            System.out.println("This seat has been booked!");
                            validReserve = false;
                            break;
                        }

                        fare = retrieveHighestFareUnmanaged(returnFlightSchedule, preferenceClassEnum);
                        seatReturn.add(seat);
                        ticket = new BookingTicketEntity(passenger, seat, fare, returnFlightSchedule, FlightTypeEnum.RETURN_FLIGHT);
                        tickets.add(ticket);
                        totalAmount += Integer.parseInt(fare.getAmount());

                    }
                }
                if (validReserve) {
                    System.out.println("Enter Credit Card Number> ");
                    String cardNum = sc.nextLine();
                    System.out.println("Enter Credit Card Name> ");
                    String cardName = sc.nextLine();
                    System.out.println("Enter Credit Card Expiry Date> ");
                    String expiryDate = sc.nextLine();
                    System.out.println("Enter Credit Card CVV> ");
                    String cvv = sc.nextLine();
                    CreditCardEntity card = creditCardEntitySessionBeanRemote.createCreditCard(new CreditCardEntity(cardNum, cardName, expiryDate, cvv));
                    reservationEntitySessionBeanRemote.createNewReservation(customer, card, new ReservationEntity(numOfPassengers, String.valueOf(totalAmount), tickets));
                    System.out.println("Successfully Reserved!");
                }
            } else {
                List<BookingTicketEntity> tickets = new ArrayList<>();
                BookingTicketEntity ticket = null;
                FareEntity fare = null;
                HashSet<SeatEntity> seatOutbound = new HashSet<>();
                HashSet<SeatEntity> seatReturn = new HashSet<>();

                System.out.println("Enter the number of Outbound Schedule IDs you would input> ");
                int transits = sc.nextInt();
                List<FlightScheduleEntity> outboundSchedules = new ArrayList<>();
                List<FlightScheduleEntity> returnSchedules = new ArrayList<>();
                for (int i = 0; i < transits; i++) {
                    System.out.print((i + 1) + ". Enter Outbound Schedule ID>");
                    try {
                        outboundSchedules.add(flightScheduleEntitySessionBeanRemote.retrieveFlightScheduleById(sc.nextLong()));
                    } catch (FlightScheduleNotFoundException ex) {
                        System.out.println(ex.getMessage());
                        return;
                    }
                }

                if (tripType == 2) {
                    for (int i = 0; i < transits; i++) {
                        System.out.print((i + 1) + ". Enter Return Schedule ID>");
                        try {
                            returnSchedules.add(flightScheduleEntitySessionBeanRemote.retrieveFlightScheduleById(sc.nextLong()));
                        } catch (FlightScheduleNotFoundException ex) {
                            System.out.println(ex.getMessage());
                            return;
                        }
                    }
                }

                totalAmount = 0;
                sc.nextLine();
                for (int i = 0; i < numOfPassengers; i++) {
                    System.out.print((i + 1) + ". ");
                    System.out.println("Enter the passenger name>");
                    String passengerName = sc.nextLine();
                    System.out.println("Enter the pasport number>");
                    String passportNumber = sc.nextLine();
                    PassengerEntity passenger = new PassengerEntity(passengerName, passportNumber);
                    System.out.println("CHOOSE SEAT TYPE FOR OUTBOUND & RETURN FLIGHT:");
                    System.out.println("Choose cabin class type: ");
                    System.out.println("1: FIRST CLASS (May not be available in all flights)");
                    System.out.println("2: BUSINESS CLASS (May not be available in all flights)");
                    System.out.println("3: PREMIUM ECONOMY CLASS (May not be available in all flights)");
                    System.out.println("4: ECONOMY CLASS");
                    preferenceClass = sc.nextInt();
                    switch (preferenceClass) {
                        case 1:
                            preferenceClassEnum = CabinClassTypeEnum.FIRST_CLASS;
                            break;
                        case 2:
                            preferenceClassEnum = CabinClassTypeEnum.BUSINESS_CLASS;
                            break;
                        case 3:
                            preferenceClassEnum = CabinClassTypeEnum.PREMIUM_ECONOMY_CLASS;
                            break;
                        case 4:
                            preferenceClassEnum = CabinClassTypeEnum.ECONOMY_CLASS;
                            break;
                        default:
                            preferenceClassEnum = null;
                            break;
                    }
                    sc.nextLine();
                    for (FlightScheduleEntity schedule : outboundSchedules) {
                        SeatEntity seat;

                            seat = seatEntitySessionBeanRemote.randomAvailableSeat(schedule.getScheduleId(), preferenceClassEnum, seatOutbound);
                            seatOutbound.add(seat);

                        fare = retrieveHighestFareUnmanaged(schedule, preferenceClassEnum);
                        ticket = new BookingTicketEntity(passenger, seat, fare, schedule, FlightTypeEnum.OUTBOUND_FLIGHT);
                        tickets.add(ticket);
                        totalAmount += Integer.parseInt(fare.getAmount());
                    }
                    for (FlightScheduleEntity schedule : returnSchedules) {
                        SeatEntity seat;

                            seat = seatEntitySessionBeanRemote.randomAvailableSeat(schedule.getScheduleId(), preferenceClassEnum, seatReturn);
                            seatReturn.add(seat);

                        fare = retrieveHighestFareUnmanaged(schedule, preferenceClassEnum);
                        ticket = new BookingTicketEntity(passenger, seat, fare, schedule, FlightTypeEnum.RETURN_FLIGHT);
                        tickets.add(ticket);
                        totalAmount += Integer.parseInt(fare.getAmount());
                    }
                }
                System.out.println("Enter Credit Card Number> ");
                String cardNum = sc.nextLine();
                System.out.println("Enter Credit Card Name> ");
                String cardName = sc.nextLine();
                System.out.println("Enter Credit Card Expiry Date> ");
                String expiryDate = sc.nextLine();
                System.out.println("Enter Credit Card CVV> ");
                String cvv = sc.nextLine();
                CreditCardEntity card = creditCardEntitySessionBeanRemote.createCreditCard(new CreditCardEntity(cardNum, cardName, expiryDate, cvv));
                reservationEntitySessionBeanRemote.createNewReservation(customer, card, new ReservationEntity(numOfPassengers, String.valueOf(totalAmount), tickets));
                System.out.println("Successfully Reserved!");

            }
        }
    }

    private static java.util.List<ws.client.holidayReservation.ReservationEntity> retrieveFlightReservationsByPartner(java.lang.String arg0) {
        ws.client.holidayReservation.HolidayReservationService_Service service = new ws.client.holidayReservation.HolidayReservationService_Service();
        ws.client.holidayReservation.HolidayReservationService port = service.getHolidayReservationServicePort();
        return port.retrieveFlightReservationsByPartner(arg0);
    }

    private static ReservationEntity retrieveReservationByReservationIdPartner(long arg0, ws.client.holidayReservation.PartnerEntity arg1) {
        ws.client.holidayReservation.HolidayReservationService_Service service = new ws.client.holidayReservation.HolidayReservationService_Service();
        ws.client.holidayReservation.HolidayReservationService port = service.getHolidayReservationServicePort();
        return port.retrieveReservationByReservationIdPartner(arg0, arg1);
    }

    private static java.util.List<ws.client.holidayReservation.BookingTicketEntity> retrieveTickets(long arg0) {
        ws.client.holidayReservation.HolidayReservationService_Service service = new ws.client.holidayReservation.HolidayReservationService_Service();
        ws.client.holidayReservation.HolidayReservationService port = service.getHolidayReservationServicePort();
        return port.retrieveTickets(arg0);
    }

    private static java.util.List<ws.client.holidayReservation.NestedList> searchConnectingFlights(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, int arg3, int arg4, ws.client.holidayReservation.CabinClassTypeEnum arg5) {
        ws.client.holidayReservation.HolidayReservationService_Service service = new ws.client.holidayReservation.HolidayReservationService_Service();
        ws.client.holidayReservation.HolidayReservationService port = service.getHolidayReservationServicePort();
        return port.searchConnectingFlights(arg0, arg1, arg2, arg3, arg4, arg5);
    }

    private static java.util.List<ws.client.holidayReservation.NestedList> searchConnectingFlightsAfter(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, int arg3, int arg4, ws.client.holidayReservation.CabinClassTypeEnum arg5) {
        ws.client.holidayReservation.HolidayReservationService_Service service = new ws.client.holidayReservation.HolidayReservationService_Service();
        ws.client.holidayReservation.HolidayReservationService port = service.getHolidayReservationServicePort();
        return port.searchConnectingFlightsAfter(arg0, arg1, arg2, arg3, arg4, arg5);
    }

    private static java.util.List<ws.client.holidayReservation.NestedList> searchConnectingFlightsBefore(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, int arg3, int arg4, ws.client.holidayReservation.CabinClassTypeEnum arg5) {
        ws.client.holidayReservation.HolidayReservationService_Service service = new ws.client.holidayReservation.HolidayReservationService_Service();
        ws.client.holidayReservation.HolidayReservationService port = service.getHolidayReservationServicePort();
        return port.searchConnectingFlightsBefore(arg0, arg1, arg2, arg3, arg4, arg5);
    }

    private static java.util.List<ws.client.holidayReservation.NestedList> searchDirectFlights(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, int arg3, ws.client.holidayReservation.CabinClassTypeEnum arg4) {
        ws.client.holidayReservation.HolidayReservationService_Service service = new ws.client.holidayReservation.HolidayReservationService_Service();
        ws.client.holidayReservation.HolidayReservationService port = service.getHolidayReservationServicePort();
        return port.searchDirectFlights(arg0, arg1, arg2, arg3, arg4);
    }

    private static java.util.List<ws.client.holidayReservation.NestedList> searchDirectFlightsAfter(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, int arg3, int arg4, ws.client.holidayReservation.CabinClassTypeEnum arg5) {
        ws.client.holidayReservation.HolidayReservationService_Service service = new ws.client.holidayReservation.HolidayReservationService_Service();
        ws.client.holidayReservation.HolidayReservationService port = service.getHolidayReservationServicePort();
        return port.searchDirectFlightsAfter(arg0, arg1, arg2, arg3, arg4, arg5);
    }

    private static java.util.List<ws.client.holidayReservation.NestedList> searchDirectFlightsBefore(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, int arg3, int arg4, ws.client.holidayReservation.CabinClassTypeEnum arg5) {
        ws.client.holidayReservation.HolidayReservationService_Service service = new ws.client.holidayReservation.HolidayReservationService_Service();
        ws.client.holidayReservation.HolidayReservationService port = service.getHolidayReservationServicePort();
        return port.searchDirectFlightsBefore(arg0, arg1, arg2, arg3, arg4, arg5);
    }

    private static FareEntity retrieveHighestFareUnmanaged(ws.client.holidayReservation.FlightScheduleEntity arg0, ws.client.holidayReservation.CabinClassTypeEnum arg1) {
        ws.client.holidayReservation.HolidayReservationService_Service service = new ws.client.holidayReservation.HolidayReservationService_Service();
        ws.client.holidayReservation.HolidayReservationService port = service.getHolidayReservationServicePort();
        return port.retrieveHighestFareUnmanaged(arg0, arg1);
    }

    private static java.util.List<ws.client.holidayReservation.CabinClassConfigurationEntity> getCabinClassConfig(ws.client.holidayReservation.AircraftConfigurationEntity arg0) {
        ws.client.holidayReservation.HolidayReservationService_Service service = new ws.client.holidayReservation.HolidayReservationService_Service();
        ws.client.holidayReservation.HolidayReservationService port = service.getHolidayReservationServicePort();
        return port.getCabinClassConfig(arg0);
    }

    private void printAirplaneSeats(FlightScheduleEntity outboundFlightSchedule, CabinClassTypeEnum preferenceClassEnum) {
        System.out.println("AVAILABILITY OF SEATS:");
        List<SeatsInventoryEntity> seatInventories = new ArrayList<>();

        seatInventories = seatsInventoryEntitySessionBeanRemote.retrieveSeatsInventoryByScheduleId(outboundFlightSchedule.getScheduleId());
        for (SeatsInventoryEntity seatInventory : seatInventories) {
            if (seatInventory.getCabinClass().getType().equals(preferenceClassEnum)) {
                List<SeatEntity> seats = seatsInventoryEntitySessionBeanRemote.retrieveSeats(seatInventory);
                for (SeatEntity seat : seats) {
                    if (!seat.isBooked()) {
                        System.out.print(seat.getSeatNumber() + seat.getSeatLetter() + ", ");
                    }
                }
            }
        }
        System.out.println();
    }
}
