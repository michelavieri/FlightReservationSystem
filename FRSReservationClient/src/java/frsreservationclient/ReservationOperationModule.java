/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frsreservationclient;

import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.FlightScheduleEntitySessionBeanRemote;
import ejb.session.stateless.ReservationEntitySessionBeanRemote;
import entity.BookingTicketEntity;
import entity.CreditCardEntity;
import entity.CustomerEntity;
import entity.FareEntity;
import entity.FlightEntity;
import entity.FlightScheduleEntity;
import entity.PassengerEntity;
import entity.ReservationEntity;
import entity.SeatEntity;
import static java.lang.System.out;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import util.enumeration.CabinClassTypeEnum;
import util.enumeration.FlightTypeEnum;
import util.exception.InvalidReservationId;
import util.exception.NotMyReservationException;

/**
 *
 * @author miche
 */
public class ReservationOperationModule {

    public ReservationEntitySessionBeanRemote reservationEntitySessionBeanRemote;
    public CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;
    public FlightScheduleEntitySessionBeanRemote flightScheduleEntitySessionBeanRemote;

    public ReservationOperationModule(ReservationEntitySessionBeanRemote reservationEntitySessionBeanRemote,
            CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote,
            FlightScheduleEntitySessionBeanRemote flightScheduleEntitySessionBeanRemote) {
        this.reservationEntitySessionBeanRemote = reservationEntitySessionBeanRemote;
        this.customerEntitySessionBeanRemote = customerEntitySessionBeanRemote;
        this.flightScheduleEntitySessionBeanRemote = flightScheduleEntitySessionBeanRemote;
    }

    public void searchFlights(Scanner sc, CustomerEntity customer) {
        boolean validReserve = true;
        System.out.println("*** SEARCH FOR FLIGHTS ***");
        System.out.println("1: One-way Trip");
        System.out.println("2: Round Trip/Return");
        System.out.println("Enter trip type> ");
        int tripType = sc.nextInt();

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
        System.out.println("2: Connecting Flight");
        int preferenceFlight = sc.nextInt();

        int stopovers = 0;
        if (preferenceFlight == 2) {
            System.out.println("Enter number of stopovers> ");
            stopovers = sc.nextInt();
        }

        System.out.println("Do you have preference for Cabin Class?");
        System.out.println("1: First Class");
        System.out.println("2: Business Class");
        System.out.println("3: Premium Economy Class");
        System.out.println("4: Economy Class");
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
                preferenceClassEnum = CabinClassTypeEnum.ECONOMY_CLASS;
                break;
        }

        List<List<FlightScheduleEntity>> outboundFlightsSameDate;
        List<List<FlightScheduleEntity>> outboundFlightsBeforeDate;
        List<List<FlightScheduleEntity>> outboundFlightsAfterDate;
        if (preferenceFlight == 1) {
            outboundFlightsSameDate = flightScheduleEntitySessionBeanRemote.
                    searchDirectFlights(departureAirportCode, destinationAirportCode, departureDate, numOfPassengers, preferenceClassEnum);
            outboundFlightsBeforeDate = flightScheduleEntitySessionBeanRemote.
                    searchDirectFlightsBefore(departureAirportCode, destinationAirportCode, departureDate, numOfPassengers, preferenceClassEnum);
            outboundFlightsAfterDate = flightScheduleEntitySessionBeanRemote.
                    searchDirectFlightsAfter(departureAirportCode, destinationAirportCode, departureDate, numOfPassengers, preferenceClassEnum);
        } else {
            outboundFlightsSameDate = flightScheduleEntitySessionBeanRemote.
                    searchConnectingFlights(departureAirportCode, destinationAirportCode, departureDate, numOfPassengers, stopovers, preferenceClassEnum);
            outboundFlightsBeforeDate = flightScheduleEntitySessionBeanRemote.
                    searchConnectingFlightsBefore(departureAirportCode, destinationAirportCode, departureDate, numOfPassengers, stopovers, preferenceClassEnum);
            outboundFlightsAfterDate = flightScheduleEntitySessionBeanRemote.
                    searchConnectingFlightsAfter(departureAirportCode, destinationAirportCode, departureDate, numOfPassengers, stopovers, preferenceClassEnum);
        }
        List<List<FlightScheduleEntity>> returnFlightsSameDate = new ArrayList<>();
        List<List<FlightScheduleEntity>> returnFlightsBeforeDate = new ArrayList<>();
        List<List<FlightScheduleEntity>> returnFlightsAfterDate = new ArrayList<>();
        if (tripType == 2) {
            if (preferenceFlight == 1) {
                returnFlightsSameDate = flightScheduleEntitySessionBeanRemote.
                        searchDirectFlights(destinationAirportCode, departureAirportCode, returnDate, numOfPassengers, preferenceClassEnum);
                returnFlightsBeforeDate = flightScheduleEntitySessionBeanRemote.
                        searchDirectFlightsBefore(destinationAirportCode, departureAirportCode, returnDate, numOfPassengers, preferenceClassEnum);
                returnFlightsAfterDate = flightScheduleEntitySessionBeanRemote.
                        searchDirectFlightsAfter(destinationAirportCode, departureAirportCode, returnDate, numOfPassengers, preferenceClassEnum);
            } else {
                returnFlightsSameDate = flightScheduleEntitySessionBeanRemote.
                        searchConnectingFlights(destinationAirportCode, departureAirportCode, returnDate, numOfPassengers, stopovers, preferenceClassEnum);
                returnFlightsBeforeDate = flightScheduleEntitySessionBeanRemote.
                        searchConnectingFlightsBefore(destinationAirportCode, departureAirportCode, returnDate, numOfPassengers, stopovers, preferenceClassEnum);
                returnFlightsAfterDate = flightScheduleEntitySessionBeanRemote.
                        searchConnectingFlightsAfter(destinationAirportCode, departureAirportCode, returnDate, numOfPassengers, stopovers, preferenceClassEnum);
            }
        }

        System.out.println("*** SEARCH RESULTS: ***");
        System.out.println("OUTBOUND FLIGHTS: ");
        System.out.println("Departure Date " + departureDate + ":");

        int pricePerPassenger = 0;
        for (int i = 0; i < outboundFlightsSameDate.size(); i++) {
            System.out.println((i + 1) + ". FLIGHT(s) : ");
            List<FlightScheduleEntity> schedules = outboundFlightsSameDate.get(i);
            for (int j = 0; j < schedules.size(); j++) {
                System.out.println("\t -SCHEDULE ID: " + schedules.get(j).getScheduleId());
                System.out.println("\t  " + schedules.get(j).getPlan().getFlight().getRoute().getOriginAirport().getAirportCode()
                        + " - " + schedules.get(j).getPlan().getFlight().getRoute().getDestinationAirport().getAirportCode() + " ("
                        + schedules.get(j).getPlan().getFlight().getFlightCode() + ")");
                System.out.println("\t  Departure Time: " + schedules.get(j).getDepartureDateTime());
                System.out.println("\t  Arrival Time: " + schedules.get(j).getArrivalDateTime());
                pricePerPassenger += satsInventoryEntitySessionBeanRemote.retrieveLowestFare(schedules.get(j), preferenceClassEnum);
            }
            System.out.println();

            System.out.println("   Price: ");
            System.out.println("   \t Price per passenger for " + searchCabinType(preferenceClassEnum) + ": " + pricePerPassenger);
            System.out.println("   \t Total Price: " + pricePerPassenger * numOfPassengers);
        }

        System.out.println("***");
        System.out.println("Departure Date 3/2/1 Days before " + departureDate + ":");

        pricePerPassenger = 0;
        for (int i = 0; i < outboundFlightsBeforeDate.size(); i++) {
            System.out.println((i + 1) + ". FLIGHT(s) : ");
            List<FlightScheduleEntity> schedules = outboundFlightsBeforeDate.get(i);
            for (int j = 0; j < schedules.size(); j++) {
                System.out.println("\t -" + schedules.get(j).getPlan().getFlight().getRoute().getOriginAirport().getAirportCode()
                        + " - " + schedules.get(j).getPlan().getFlight().getRoute().getDestinationAirport().getAirportCode() + " ("
                        + schedules.get(j).getPlan().getFlight().getFlightCode() + ")");
                System.out.println("\t  Departure Time: " + schedules.get(j).getDepartureDateTime());
                System.out.println("\t  Arrival Time: " + schedules.get(j).getArrivalDateTime());
                pricePerPassenger += satsInventoryEntitySessionBeanRemote.retrieveLowestFare(schedules.get(j), preferenceClassEnum);
            }
            System.out.println();

            System.out.println("   Price: ");
            System.out.println("   \t Price per passenger for " + searchCabinType(preferenceClassEnum) + ": " + pricePerPassenger);
            System.out.println("   \t Total Price: " + pricePerPassenger * numOfPassengers);
        }

        System.out.println("***");

        System.out.println("Departure Date 3/2/1 Days after " + departureDate + ":");

        pricePerPassenger = 0;
        for (int i = 0; i < outboundFlightsAfterDate.size(); i++) {
            System.out.println((i + 1) + ". FLIGHT(s) : ");
            List<FlightScheduleEntity> schedules = outboundFlightsAfterDate.get(i);
            for (int j = 0; j < schedules.size(); j++) {
                System.out.println("\t -" + schedules.get(j).getPlan().getFlight().getRoute().getOriginAirport().getAirportCode()
                        + " - " + schedules.get(j).getPlan().getFlight().getRoute().getDestinationAirport().getAirportCode() + " ("
                        + schedules.get(j).getPlan().getFlight().getFlightCode() + ")");
                System.out.println("\t  Departure Time: " + schedules.get(j).getDepartureDateTime());
                System.out.println("\t  Arrival Time: " + schedules.get(j).getArrivalDateTime());
                pricePerPassenger += seatsInventoryEntitySessionBeanRemote.retrieveLowestFare(schedules.get(j), preferenceClassEnum);
            }
            System.out.println();

            System.out.println("   Price: ");
            System.out.println("   \t Price per passenger for " + searchCabinType(preferenceClassEnum) + ": " + pricePerPassenger);
            System.out.println("   \t Total Price: " + pricePerPassenger * numOfPassengers);
        }

        System.out.println("***********************************");
        System.out.println("RETURN FLIGHTS: ");

        System.out.println("Return Date " + returnDate + ":");

        pricePerPassenger = 0;
        for (int i = 0; i < returnFlightsSameDate.size(); i++) {
            System.out.println((i + 1) + ". FLIGHT(s) : ");
            List<FlightScheduleEntity> schedules = returnFlightsSameDate.get(i);
            for (int j = 0; j < schedules.size(); j++) {
                System.out.println("\t -" + schedules.get(j).getPlan().getFlight().getRoute().getOriginAirport().getAirportCode()
                        + " - " + schedules.get(j).getPlan().getFlight().getRoute().getDestinationAirport().getAirportCode() + " ("
                        + schedules.get(j).getPlan().getFlight().getFlightCode() + ")");
                System.out.println("\t  Departure Time: " + schedules.get(j).getDepartureDateTime());
                System.out.println("\t  Arrival Time: " + schedules.get(j).getArrivalDateTime());
                pricePerPassenger += seatsInventoryEntitySessionBeanRemote.retrieveLowestFare(schedules.get(j), preferenceClassEnum);
            }
            System.out.println();

            System.out.println("   Price: ");
            System.out.println("   \t Price per passenger for " + searchCabinType(preferenceClassEnum) + ": " + pricePerPassenger);
            System.out.println("   \t Total Price: " + pricePerPassenger * numOfPassengers);
        }

        System.out.println("***");
        System.out.println("Return Date 3/2/1 Days before " + returnDate + ":");

        pricePerPassenger = 0;
        for (int i = 0; i < returnFlightsBeforeDate.size(); i++) {
            System.out.println((i + 1) + ". FLIGHT(s) : ");
            List<FlightScheduleEntity> schedules = returnFlightsBeforeDate.get(i);
            for (int j = 0; j < schedules.size(); j++) {
                System.out.println("\t -" + schedules.get(j).getPlan().getFlight().getRoute().getOriginAirport().getAirportCode()
                        + " - " + schedules.get(j).getPlan().getFlight().getRoute().getDestinationAirport().getAirportCode() + " ("
                        + schedules.get(j).getPlan().getFlight().getFlightCode() + ")");
                System.out.println("\t  Departure Time: " + schedules.get(j).getDepartureDateTime());
                System.out.println("\t  Arrival Time: " + schedules.get(j).getArrivalDateTime());
                pricePerPassenger += Integer.parseInt(seatsInventoryEntitySessionBeanRemote.retrieveLowestFare(schedules.get(j), preferenceClassEnum).getAmount());
            }
            System.out.println();

            System.out.println("   Price: ");
            System.out.println("   \t Price per passenger for " + searchCabinType(preferenceClassEnum) + ": " + pricePerPassenger);
            System.out.println("   \t Total Price: " + pricePerPassenger * numOfPassengers);
        }

        System.out.println("***");

        System.out.println("Return Date 3/2/1 Days after " + returnDate + ":");

        pricePerPassenger = 0;
        for (int i = 0; i < returnFlightsAfterDate.size(); i++) {
            System.out.println((i + 1) + ". FLIGHT(s) : ");
            List<FlightScheduleEntity> schedules = returnFlightsAfterDate.get(i);
            for (int j = 0; j < schedules.size(); j++) {
                System.out.println("\t -SCHEDULE ID: " + schedules.get(j).getScheduleId());
                System.out.println("\t  " + schedules.get(j).getPlan().getFlight().getRoute().getOriginAirport().getAirportCode()
                        + " - " + schedules.get(j).getPlan().getFlight().getRoute().getDestinationAirport().getAirportCode() + " ("
                        + schedules.get(j).getPlan().getFlight().getFlightCode() + ")");
                System.out.println("\t  Departure Time: " + schedules.get(j).getDepartureDateTime());
                System.out.println("\t  Arrival Time: " + schedules.get(j).getArrivalDateTime());
                pricePerPassenger += seatsInventoryEntitySessionBeanRemote.retrieveLowestFare(schedules.get(j), preferenceClassEnum);
            }
            System.out.println();

            System.out.println("   Price: ");
            System.out.println("   \t Price per passenger for " + searchCabinType(preferenceClassEnum) + ": " + pricePerPassenger);
            System.out.println("   \t Total Price: " + pricePerPassenger * numOfPassengers);
        }
        if (customer == null) {
            System.out.println("Please sign in if you want to reserve a flight!");
            return;
        }
        System.out.println("Do you want to reserve flight? (Y/N)");
        String reserveFlight = sc.nextLine();

        if (reserveFlight.equals("N")) {
            return;
        } else {
            HashSet<SeatEntity> seatOutbound = new HashSet<>();
            HashSet<SeatEntity> seatReturn = new HashSet<>();

            System.out.println("Enter the Schedule ID for Outbound Flight> ");
            long outboundId = sc.nextLong();

            long returnId = 0L;
            if (tripType == 2) {
                System.out.println("Enter the Schedule ID for Return Flight> ");
                returnId = sc.nextLong();
            }
            FlightScheduleEntity outboundFlightSchedule = flightScheduleEntitySessionBeanRemote.retrieveFlightScheduleById(outboundId);
            FlightScheduleEntity returnFlightSchedule = flightScheduleEntitySessionBeanRemote.retrieveFlightScheduleById(returnId);

            List<BookingTicketEntity> tickets = new ArrayList<>();
            for (int i = 0; i < numOfPassengers; i++) {
                System.out.print((i + 1) + ". ");
                System.out.println("Enter the passenger name>");
                String passengerName = sc.nextLine();
                System.out.println("Enter the pasport number>");
                String passportNumber = sc.nextLine();
                PassengerEntity passenger = new PassengerEntity(passengerName, passportNumber);
                System.out.println("CHOOSE SEAT FOR OUTBOUND FLIGHT:");
                printAirplaneSeats(outboundFlightSchedule);
                System.out.print("Enter seat number (e.g. 12)> ");
                int seatNumber = sc.nextInt();

                sc.nextLine();
                System.out.print("Enter seat letter(e.g. A)> ");
                String seatLetter = sc.nextLine();

                SeatEntity seat = seatEntitySessionBeanRemote.retrieveSeat(seatNumber, seatLetter);

                if (seat.isBooked() || seatOutbound.contains(seat)) {
                    System.out.println("This seat has been booked!");
                    validReserve = false;
                    break;
                }
                FareEntity fare = flightScheduleEntitySessionBeanRemote.retrieveLowestFare(outboundFlightSchedule, preferenceClassEnum);
                //seatOutbound.add(seat);
                BookingTicketEntity ticket = new BookingTicketEntity(passenger, seat, fare, outboundFlightSchedule, FlightTypeEnum.OUTBOUND_FLIGHT);
                tickets.add(ticket);

                if (tripType == 2) {
                    System.out.println("CHOOSE SEAT FOR RETURN FLIGHT:");
                    //printAirplaneSeats(returnFlightSchedule);
                    System.out.print("Enter seat number (e.g. 12)> ");
                    seatNumber = sc.nextInt();

                    sc.nextLine();
                    System.out.print("Enter seat letter(e.g. A)> ");
                    seatLetter = sc.nextLine();

                    seat = seatEntitySessionBeanRemote.retrieveSeat(seatNumber, seatLetter);

                    if (seat.isBooked() || seatReturn.contains(seat)) {
                        System.out.println("This seat has been booked!");
                        validReserve = false;
                        break;
                   }

                    fare = flightScheduleEntitySessionBeanRemote.retrieveLowestFare(returnFlightSchedule, preferenceClassEnum);
                    seatReturn.add(seat);
                    ticket = new BookingTicketEntity(passenger, seat, fare, returnFlightSchedule, FlightTypeEnum.RETURN_FLIGHT);
                    tickets.add(ticket);
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
                reservationEntitySessionBeanRemote.createReservation(tickets, customer, card);
            }
        }
    }

    public void viewMyFlightReservations(CustomerEntity customer) {
        List<ReservationEntity> reservations = reservationEntitySessionBeanRemote.retrieveFlightReservationsByCustomer(customer);
        for (ReservationEntity reservation : reservations) {
            System.out.println("RESERVATION ID: " + reservation.getReservationId());
            System.out.println("\t Number of Passengers: " + reservation.getNumOfPassengers());
            System.out.println("\t Total Amount Paid: " + reservation.getTotalAmount());
        }
    }

    public void viewFlightReservationDetails(CustomerEntity customer, Scanner sc) {
        System.out.println("Enter reservation Id to view details> ");
        Long reservationId = sc.nextLong();
        ReservationEntity reservation = null;
        try {
            reservation = reservationEntitySessionBeanRemote.retrieveReservationByReservationId(reservationId, customer);
        } catch (InvalidReservationId ex) {
            System.out.println(ex.getMessage());
            return;
        } catch (NotMyReservationException ex) {
            System.out.println(ex.getMessage());
            return;
        }
        List<BookingTicketEntity> tickets = reservation.getTickets();
        FlightScheduleEntity outboundSchedule = null;
        FlightScheduleEntity returnSchedule = null;

        List<BookingTicketEntity> outboundTickets = new ArrayList<>();
        List<BookingTicketEntity> returnTickets = new ArrayList<>();

        for (BookingTicketEntity ticket : tickets) {
            if (ticket.getFlightType().equals(FlightTypeEnum.OUTBOUND_FLIGHT)) {
               outboundSchedule = ticket.getFlightSchedule();
                outboundTickets.add(ticket);
            } else {
                returnSchedule = ticket.getFlightSchedule();
                returnTickets.add(ticket);
            }
        }

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
            System.out.println("\t\t Passenger name: " + ticket.getPassenger().getPassengerName());
            System.out.println("\t\t Passenger's Passport number: " + ticket.getPassenger().getPassportNumber());
            System.out.println("\t\t Cabin Class: " + searchCabinType(ticket.getSeat().getSeatsInventory().getCabinClass().getType()));
            System.out.println("\t\t Seat Number: " + ticket.getSeat().getSeatNumber());
            System.out.println("\t\t Fare Basis Code: " + ticket.getFare().getFareBasisCode());
            System.out.println();
        }

        if (!returnTickets.isEmpty()) {
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
                System.out.println("\t\t Passenger name: " + ticket.getPassenger().getPassengerName());
                System.out.println("\t\t Passenger's Passport number: " + ticket.getPassenger().getPassportNumber());
                System.out.println("\t\t Cabin Class: " + searchCabinType(ticket.getSeat().getSeatsInventory().getCabinClass().getType()));
                System.out.println("\t\t Seat Number: " + ticket.getSeat().getSeatNumber());
                System.out.println("\t\t Fare Basis Code: " + ticket.getFare().getFareBasisCode());
                System.out.println();
            }
        }
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
}
