/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftConfigurationEntity;
import entity.AirportEntity;
import entity.BookingTicketEntity;
import entity.CabinClassConfigurationEntity;
import entity.FareEntity;
import entity.FlightEntity;
import entity.FlightRouteEntity;
import entity.FlightScheduleEntity;
import entity.FlightSchedulePlanEntity;
import entity.PassengerEntity;
import entity.SeatEntity;
import entity.SeatsInventoryEntity;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.ejb.Local;
import util.exception.FlightDisabledException;
import util.exception.FlightNotFoundException;

/**
 *
 * @author miche
 */
@Local
public interface FlightEntitySessionBeanLocal {

    public FlightEntity createFlightEntity(FlightEntity newFlight);

    public List<FlightEntity> retrieveAllFlights();

    public List<FlightSchedulePlanEntity> retrieveSchedulePlans(FlightEntity flight);

    public void setReturnFlight(FlightEntity departureFlight, FlightEntity returnFlight);

    public void setDepartureFlight(FlightEntity returnFlight, FlightEntity departureFlight);

    public FlightEntity retrieveFlightByCode(String code) throws FlightNotFoundException;

    public List<CabinClassConfigurationEntity> retrieveCabinClassByFlight(FlightEntity flight);

    public String retrieveTimeZoneByFlight(FlightEntity flight);

    public void deleteFlight(FlightEntity flight);

    public void updateAircraftConfiguration(AircraftConfigurationEntity newConfig, FlightEntity flight);

    public void updateFlightRoute(FlightRouteEntity newRoute, FlightEntity flight);

    public boolean isReturnFlight(FlightEntity flight);

    public boolean hasReturnFlight(FlightEntity flight);

    public void removeReturnFlight(FlightEntity flight);

    public void createRecurrentSchedule(String day, FlightSchedulePlanEntity schedule, String startDate, String endDate, int days, String departureTime, DateTimeFormatter dateFormat, String duration, int layoverDuration);

    public FlightScheduleEntity getFlightScheduleByTicketUnmanaged(BookingTicketEntity inTicket);

    public FlightSchedulePlanEntity getPlanByScheduleUnmanaged(FlightScheduleEntity schedule);

    public FlightEntity getFlightByPlanUnmanaged(FlightSchedulePlanEntity plan);

    public FlightRouteEntity getRouteByFlightUnmanaged(FlightEntity flight);

    public AirportEntity getOriginAirportByRouteUnmanaged(FlightRouteEntity route);

    public AirportEntity getDestinationAirportByRouteUnmanaged(FlightRouteEntity route);

    public PassengerEntity getPassengerByTicketUnmanaged(BookingTicketEntity ticket);

    public SeatEntity getSeatByTicketUnmanaged(BookingTicketEntity ticket);

    public SeatsInventoryEntity getSeatsInventoryBySeatUnmanaged(SeatEntity seat);

    public CabinClassConfigurationEntity getCabinClassBySeatsInventoryUnmanaged(SeatsInventoryEntity seatsInventoryEntity);

    public FareEntity getFareByTicketUnmanaged(BookingTicketEntity ticket);
}
