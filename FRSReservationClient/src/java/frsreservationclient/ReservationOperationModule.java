/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frsreservationclient;

import ejb.session.stateless.CustomerEntitySessionBeanRemote;
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

    public ReservationOperationModule(ReservationEntitySessionBeanRemote reservationEntitySessionBeanRemote,
            CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote) {
        this.reservationEntitySessionBeanRemote = reservationEntitySessionBeanRemote;
        this.customerEntitySessionBeanRemote = customerEntitySessionBeanRemote;
    }

    public void searchFlights(Scanner sc) {
        
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
