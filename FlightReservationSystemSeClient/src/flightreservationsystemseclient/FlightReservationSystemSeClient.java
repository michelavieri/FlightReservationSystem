/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flightreservationsystemseclient;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import ws.client.holidayReservation.BookingTicketEntity;
import ws.client.holidayReservation.CabinClassTypeEnum;
import ws.client.holidayReservation.CustomerEntity;
import ws.client.holidayReservation.FlightEntity;
import ws.client.holidayReservation.FlightScheduleEntity;
import ws.client.holidayReservation.FlightTypeEnum;
import ws.client.holidayReservation.PartnerEntity;
import ws.client.holidayReservation.ReservationEntity;

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

        while (true) {
            System.out.println("*** Welcome, this is Holiday Reservation System Application ***");
            System.out.println("1: Partner Login");
            System.out.println("2: Search Flight");
            System.out.println("3: View All Reservations");
            System.out.println("4: View Reservation Details");
            System.out.println("5: Exit");
            int response = 0;
            PartnerEntity partner = null;

            while (response < 1 || response > 5) {
                System.out.print("<");
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
       // List<ReservationEntity> reservations = reservationEntitySessionBeanRemote.retrieveFlightReservationsByCustomer(customer);
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
//        try {
//            reservation = reservationEntitySessionBeanRemote.retrieveReservationByReservationId(reservationId, customer);
//        } catch (InvalidReservationId ex) {
//            System.out.println(ex.getMessage());
//            return;
//        } catch (NotMyReservationException ex) {
//            System.out.println(ex.getMessage());
//            return;
//        }
        List<BookingTicketEntity> tickets = null;
//        try {
//            tickets = reservationEntitySessionBeanRemote.retrieveTickets(reservationId);
//        } catch (NoTicketException ex) {
//            System.out.println(ex.getMessage());
//            return;
//        }
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
}
