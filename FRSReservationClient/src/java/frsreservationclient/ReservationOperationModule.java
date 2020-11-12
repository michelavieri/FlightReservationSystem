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
import entity.CustomerEntity;
import entity.FlightEntity;
import entity.FlightScheduleEntity;
import entity.ReservationEntity;
import java.util.ArrayList;
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

    public void searchFlights(Scanner sc) {
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

        if (tripType == 2) {
            List<List<FlightScheduleEntity>> returnFlightsSameDate;
            List<List<FlightScheduleEntity>> returnFlightsBeforeDate;
            List<List<FlightScheduleEntity>> returnFlightsAfterDate;
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
        List<CabinClassTypeEnum> cabinClassesAvailable = new ArrayList<>();
        cabinClassesAvailable.add(CabinClassTypeEnum.FIRST_CLASS);
        cabinClassesAvailable.add(CabinClassTypeEnum.BUSINESS_CLASS);
        cabinClassesAvailable.add(CabinClassTypeEnum.PREMIUM_ECONOMY_CLASS);
        cabinClassesAvailable.add(CabinClassTypeEnum.ECONOMY_CLASS);

        for (List<FlightScheduleEntity> schedules : outboundFlightsSameDate) {
            for (FlightScheduleEntity schedule : schedules) {

            }
        }
        for (int i = 0; i < outboundFlightsSameDate.size(); i++) {
            System.out.println((i + 1) + ". FLIGHT(s) : ");
            List<FlightScheduleEntity> schedules = outboundFlightsSameDate.get(i);
            for (int j = 0; j < schedules.size(); j++) {
                System.out.println("\t -" + schedules.get(j).getPlan().getFlight().getRoute().getOriginAirport().getAirportCode()
                        + " - " + schedules.get(j).getPlan().getFlight().getRoute().getDestinationAirport().getAirportCode() + " ("
                        + schedules.get(j).getPlan().getFlight().getFlightCode() + ")");
                System.out.println("\t  Departure Time: " + schedules.get(j).getDepartureDateTime());
                System.out.println("\t  Arrival Time: " + schedules.get(j).getArrivalDateTime());
                System.out.println("\t  Cabin Classes Available: ");
                System.out.println("\t\t ");
            }
            System.out.println();
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
            System.out.println("\t\t Fare Basis Code: " + ticket.getFareBasisCode());
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
                System.out.println("\t\t Fare Basis Code: " + ticket.getFareBasisCode());
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
