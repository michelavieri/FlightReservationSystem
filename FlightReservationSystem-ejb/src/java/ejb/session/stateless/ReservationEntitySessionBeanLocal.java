/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AirportEntity;
import entity.BookingTicketEntity;
import entity.CreditCardEntity;
import entity.CustomerEntity;
import entity.FlightScheduleEntity;
import entity.ReservationEntity;
import java.util.List;
import javax.ejb.Local;
import util.enumeration.CabinClassTypeEnum;
import util.exception.InvalidReservationId;
import util.exception.NoTicketException;
import util.exception.NotMyReservationException;

/**
 *
 * @author Chrisya
 */
@Local
public interface ReservationEntitySessionBeanLocal {

    public List<ReservationEntity> retrieveFlightReservationsByCustomer(CustomerEntity cust);

    public List<BookingTicketEntity> retrieveTickets(long reservationId) throws NoTicketException;

    public ReservationEntity retrieveReservationByReservationId(long reservationId, CustomerEntity customer) throws InvalidReservationId, NotMyReservationException;

    public ReservationEntity createNewReservation(CustomerEntity customer, CreditCardEntity card, ReservationEntity newReservation);

    public List<List<FlightScheduleEntity>> searchConnectingFlightsAfter(String departureAirport, String destinationAirport, String departureDateTime, int numOfPassenger, int stopovers, CabinClassTypeEnum classType);

    public List<List<FlightScheduleEntity>> searchConnectingFlightsBefore(String departureAirport, String destinationAirport, String departureDateTime, int numOfPassenger, int stopovers, CabinClassTypeEnum classType);

    public List<List<FlightScheduleEntity>> searchConnectingFlights(String departureAirport, String destinationAirport, String departureDateTime, int numOfPassenger, int stopovers, CabinClassTypeEnum classType);

    public List<List<FlightScheduleEntity>> recurseTransit(List<FlightScheduleEntity> allSchedules, AirportEntity departureAirport, AirportEntity destinationAirport,
            int stopovers, List<FlightScheduleEntity> availableSchedule,
            List<List<FlightScheduleEntity>> finalSchedule, CabinClassTypeEnum classType, int numOfPassenger);

    public FlightScheduleEntity retrieveFlightScheduleById(Long id);

}
