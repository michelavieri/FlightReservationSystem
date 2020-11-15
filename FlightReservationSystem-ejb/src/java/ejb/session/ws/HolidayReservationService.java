/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.CustomerEntitySessionBeanLocal;
import ejb.session.stateless.FareEntitySessionBeanLocal;
import ejb.session.stateless.FlightScheduleEntitySessionBeanLocal;
import ejb.session.stateless.PartnerEntitySessionBeanLocal;
import ejb.session.stateless.ReservationEntitySessionBeanLocal;
import entity.BookingTicketEntity;
import entity.CustomerEntity;
import entity.FareEntity;
import entity.FlightScheduleEntity;
import entity.PartnerEntity;
import entity.ReservationEntity;
import entity.SeatsInventoryEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import util.enumeration.CabinClassTypeEnum;
import util.exception.InvalidEmailException;
import util.exception.InvalidReservationId;
import util.exception.InvalidUsernameException;
import util.exception.NoTicketException;
import util.exception.NotMyReservationException;
import util.exception.PartnerNotFoundException;
import util.exception.WrongPasswordException;

/**
 *
 * @author Chrisya
 */
@WebService(serviceName = "HolidayReservationService")
@Stateless
public class HolidayReservationService {

    @EJB
    private FareEntitySessionBeanLocal fareEntitySessionBeanLocal;

    @EJB
    private PartnerEntitySessionBeanLocal partnerEntitySessionBeanLocal;

    @EJB
    private FlightScheduleEntitySessionBeanLocal flightScheduleEntitySessionBeanLocal;

    @EJB
    private ReservationEntitySessionBeanLocal reservationEntitySessionBeanLocal;

    @EJB
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "partnerLogin")
    public PartnerEntity partnerLogin(String username, String password) {

        PartnerEntity partner = null;
        try {
            partner = partnerEntitySessionBeanLocal.partnerLoginUnmanaged(username, password);

            List<ReservationEntity> reservations = partner.getReservationsEntitys();

            for (ReservationEntity reservation : reservations) {
                reservation.setCustomer(null);
            }

        } catch (InvalidUsernameException ex) {
            System.out.println(ex.getMessage());
        } catch (WrongPasswordException ex) {
            System.out.println(ex.getMessage());
        }

        return partner;
    }

    @WebMethod(operationName = "retrieveFlightReservationsByPartner")
    public List<ReservationEntity> retrieveFlightReservationsByPartner(String username) {

        PartnerEntity partner = null;

        try {
            partner = partnerEntitySessionBeanLocal.retrievePartnerByUsername(username);
        } catch (PartnerNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        List<ReservationEntity> reservations = reservationEntitySessionBeanLocal.retrieveFlightReservationsByPartnerUnmanaged(partner);

        for (ReservationEntity reservation : reservations) {

            for (BookingTicketEntity seat : reservation.getTickets()) {
                seat.setReservationEntity(null);
            }

            reservation.getCreditCardEntity().setReservation(null);
            reservation.getPartner().getReservationsEntitys().remove(reservation);
            reservation.getCustomer().getReservationsEntitys().remove(reservation);
        }

        return reservations;
    }

    @WebMethod(operationName = "retrieveReservationByReservationIdPartner")
    public ReservationEntity retrieveReservationByReservationIdPartner(long reservationId, PartnerEntity partner) {

        ReservationEntity reservation = null;

        try {
            reservation = reservationEntitySessionBeanLocal.retrieveReservationByReservationIdPartnerUnmanaged(reservationId, partner);
        } catch (InvalidReservationId ex) {
            System.out.println(ex.getMessage());
        } catch (NotMyReservationException ex) {
            System.out.println(ex.getMessage());
        }

        for (BookingTicketEntity seat : reservation.getTickets()) {
            seat.setReservationEntity(null);
        }

        reservation.getCreditCardEntity().setReservation(null);
        reservation.getPartner().getReservationsEntitys().remove(reservation);
        reservation.getCustomer().getReservationsEntitys().remove(reservation);

        return reservation;
    }

    @WebMethod(operationName = "retrieveTickets")
    public List<BookingTicketEntity> retrieveTickets(long reservationId) {

        List<BookingTicketEntity> bookings = new ArrayList<>();

        try {
            bookings = reservationEntitySessionBeanLocal.retrieveTicketsUnmanaged(reservationId);
        } catch (NoTicketException ex) {
            System.out.println(ex.getMessage());
        }

        for (BookingTicketEntity booking : bookings) {

            booking.getFare().getBookingTicketEntitys().remove(booking);
            booking.getFlightSchedule().getBookingTicketEntitys().remove(booking);
            booking.getPassenger().getBookingTicketEntitys().remove(booking);
            booking.getReservationEntity().getTickets().remove(booking);
            booking.getSeat().setBookingTicketEntity(null);
        }

        return bookings;
    }
    
    @WebMethod(operationName = "retrieveHighestFareUnmanaged")
    public FareEntity retrieveHighestFareUnmanaged(FlightScheduleEntity schedule, CabinClassTypeEnum type) {

        FareEntity fare = fareEntitySessionBeanLocal.retrieveHighestFareUnmanaged(schedule, type);
        
        List<BookingTicketEntity> tickets = fare.getBookingTicketEntitys();
        fare.getCabinClass().getFareEntitys().remove(fare);
        fare.getFlightSchedulePlan().getFareEntitys().remove(fare);
        
        for(BookingTicketEntity ticket:tickets) {
            ticket.setFare(null);
        }
        
        return fare;
    }

    @WebMethod(operationName = "searchConnectingFlights")
    public List<NestedList> searchConnectingFlights(String departureAirport,
            String destinationAirport, String departureDateTime, int numOfPassenger, int stopovers, CabinClassTypeEnum classType) {

        List<NestedList> result = new ArrayList<>();
        result = reservationEntitySessionBeanLocal.searchConnectingFlightsUnmanaged(departureAirport, destinationAirport, departureDateTime, numOfPassenger, stopovers, classType);

        List<FlightScheduleEntity> innerList = result.getInnerList();

        for (FlightScheduleEntity schedule : innerList) {
            schedule.getPlan().getFlightSchedules().remove(schedule);

            List<BookingTicketEntity> tickets = schedule.getBookingTicketEntitys();

            for (BookingTicketEntity ticket : tickets) {
                ticket.setFlightSchedule(null);
            }

            if (schedule.getDepartureSchedule() != null) {
                schedule.getDepartureSchedule().setReturnSchedule(null);
            }

            if (schedule.getDepartureSchedule() != null) {
                schedule.getReturnSchedule().setDepartureSchedule(null);
            }
        }
        return result;
    }

    @WebMethod(operationName = "searchConnectingFlightsBefore")
    public List<NestedList> searchConnectingFlightsBefore(String departureAirport,
            String destinationAirport, String departureDateTime, int numOfPassenger, int stopovers, CabinClassTypeEnum classType) {

        List<NestedList> result = new ArrayList<>();
        result = reservationEntitySessionBeanLocal.searchConnectingFlightsBeforeUnmanaged(departureAirport, destinationAirport, departureDateTime, numOfPassenger, stopovers, classType);

        List<FlightScheduleEntity> innerList = result.getInnerList();

        for (FlightScheduleEntity schedule : innerList) {
            schedule.getPlan().getFlightSchedules().remove(schedule);

            List<BookingTicketEntity> tickets = schedule.getBookingTicketEntitys();

            for (BookingTicketEntity ticket : tickets) {
                ticket.setFlightSchedule(null);
            }

            if (schedule.getDepartureSchedule() != null) {
                schedule.getDepartureSchedule().setReturnSchedule(null);
            }

            if (schedule.getDepartureSchedule() != null) {
                schedule.getReturnSchedule().setDepartureSchedule(null);
            }
        }

        return result;
    }

    @WebMethod(operationName = "searchConnectingFlightsAfter")
    public List<NestedList> searchConnectingFlightsAfter(String departureAirport,
            String destinationAirport, String departureDateTime, int numOfPassenger, int stopovers, CabinClassTypeEnum classType) {

        List<NestedList> result = new ArrayList<>();
        result = reservationEntitySessionBeanLocal.searchConnectingFlightsAfterUnmanaged(departureAirport, destinationAirport, departureDateTime, numOfPassenger, stopovers, classType);

        List<FlightScheduleEntity> innerList = result.getInnerList();

        for (FlightScheduleEntity schedule : innerList) {
            schedule.getPlan().getFlightSchedules().remove(schedule);

            List<BookingTicketEntity> tickets = schedule.getBookingTicketEntitys();

            for (BookingTicketEntity ticket : tickets) {
                ticket.setFlightSchedule(null);
            }

            if (schedule.getDepartureSchedule() != null) {
                schedule.getDepartureSchedule().setReturnSchedule(null);
            }

            if (schedule.getDepartureSchedule() != null) {
                schedule.getReturnSchedule().setDepartureSchedule(null);
            }
        }

        return result;
    }

    @WebMethod(operationName = "searchDirectFlights")
    public List<NestedList> searchDirectFlights(String departureAirport, String destinationAirport, String departureDate, int numOfPassenger, CabinClassTypeEnum classType) {

        List<NestedList> result = new ArrayList<>();
        result = flightScheduleEntitySessionBeanLocal.searchDirectFlightsUnmanaged(departureAirport, destinationAirport, departureDate, numOfPassenger, classType);

        List<FlightScheduleEntity> innerList = result.getInnerList();

        for (FlightScheduleEntity schedule : innerList) {
            schedule.getPlan().getFlightSchedules().remove(schedule);

            List<BookingTicketEntity> tickets = schedule.getBookingTicketEntitys();

            for (BookingTicketEntity ticket : tickets) {
                ticket.setFlightSchedule(null);
            }

            if (schedule.getDepartureSchedule() != null) {
                schedule.getDepartureSchedule().setReturnSchedule(null);
            }

            if (schedule.getDepartureSchedule() != null) {
                schedule.getReturnSchedule().setDepartureSchedule(null);
            }
        }

        return result;
    }
    
    @WebMethod(operationName = "searchDirectFlightsBefore")
    public List<NestedList> searchDirectFlightsBefore(String departureAirport,
            String destinationAirport, String departureDateTime, int numOfPassenger, int stopovers, CabinClassTypeEnum classType) {

        List<NestedList> result = new ArrayList<>();
        result = flightScheduleEntitySessionBeanLocal.searchDirectFlightsBeforeUnmanaged(departureAirport, destinationAirport, departureDateTime, numOfPassenger, classType);

        List<FlightScheduleEntity> innerList = result.getInnerList();

        for (FlightScheduleEntity schedule : innerList) {
            schedule.getPlan().getFlightSchedules().remove(schedule);

            List<BookingTicketEntity> tickets = schedule.getBookingTicketEntitys();

            for (BookingTicketEntity ticket : tickets) {
                ticket.setFlightSchedule(null);
            }

            if (schedule.getDepartureSchedule() != null) {
                schedule.getDepartureSchedule().setReturnSchedule(null);
            }

            if (schedule.getDepartureSchedule() != null) {
                schedule.getReturnSchedule().setDepartureSchedule(null);
            }
        }

        return result;
    }
    
    @WebMethod(operationName = "searchDirectFlightsAfter")
    public List<NestedList> searchDirectFlightsAfter(String departureAirport,
            String destinationAirport, String departureDateTime, int numOfPassenger, int stopovers, CabinClassTypeEnum classType) {

        List<NestedList> result = new ArrayList<>();
        result = flightScheduleEntitySessionBeanLocal.searchDirectFlightsAfterUnmanaged(departureAirport, destinationAirport, departureDateTime, numOfPassenger, classType);

        List<FlightScheduleEntity> innerList = result.getInnerList();

        for (FlightScheduleEntity schedule : innerList) {
            schedule.getPlan().getFlightSchedules().remove(schedule);

            List<BookingTicketEntity> tickets = schedule.getBookingTicketEntitys();

            for (BookingTicketEntity ticket : tickets) {
                ticket.setFlightSchedule(null);
            }

            if (schedule.getDepartureSchedule() != null) {
                schedule.getDepartureSchedule().setReturnSchedule(null);
            }

            if (schedule.getDepartureSchedule() != null) {
                schedule.getReturnSchedule().setDepartureSchedule(null);
            }
        }

        return result;
    }
}
