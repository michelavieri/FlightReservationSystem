/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AirportEntity;
import entity.FlightEntity;
import entity.FlightScheduleEntity;
import entity.FlightSchedulePlanEntity;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.ejb.Remote;
import util.enumeration.CabinClassTypeEnum;
import util.exception.FlightScheduleNotFoundException;
import util.exception.ScheduleIsUsedException;
import util.exception.ScheduleOverlapException;

/**
 *
 * @author Chrisya
 */
@Remote
public interface FlightScheduleEntitySessionBeanRemote {

    public FlightScheduleEntity createFlightScheduleEntity(FlightScheduleEntity newFlightSchedule);

    public void associateWithPlan(FlightScheduleEntity schedule, FlightSchedulePlanEntity schedulePlan);

    public void associateNewSeatsInventory(FlightSchedulePlanEntity plan);

    public void associateReturnSchedule(FlightScheduleEntity departure, FlightScheduleEntity returning);

    public void createRecurrentSchedule(String day, FlightSchedulePlanEntity schedule, String startDate, String endDate, int days, String departureTime, DateTimeFormatter dateFormat, String duration, int layoverDuration);

    public boolean checkOverlapping(FlightSchedulePlanEntity plan, FlightScheduleEntity schedule, DateTimeFormatter dateFormat) throws ScheduleOverlapException;

    public boolean checkOverlapSchedule(FlightSchedulePlanEntity plan, FlightScheduleEntity schedule, DateTimeFormatter dateFormat) throws ScheduleOverlapException;

    public FlightScheduleEntity overlapSchedule(FlightSchedulePlanEntity plan, FlightScheduleEntity schedule, DateTimeFormatter dateFormat);

    public FlightScheduleEntity checkOverlapPlan(FlightEntity flight, FlightSchedulePlanEntity schedulePlan, FlightScheduleEntity schedule, DateTimeFormatter dateFormat);

    public void replaceSchedule(FlightScheduleEntity oldSchedule, FlightScheduleEntity newSchedule);

    public void deleteSchedule(FlightScheduleEntity schedule) throws ScheduleIsUsedException;

    public FlightScheduleEntity retrieveFlightScheduleById(Long id) throws FlightScheduleNotFoundException;

    public FlightScheduleEntity retrieveReturnSchedule(FlightScheduleEntity schedule);

    public List<List<FlightScheduleEntity>> searchDirectFlights(String departureAirport, String destinationAirport, String departureDate, int numOfPassenger, CabinClassTypeEnum classType);

    public List<List<FlightScheduleEntity>> searchDirectFlightsBefore(String departureAirport, String destinationAirport, String departureDateTime, int numOfPassenger, CabinClassTypeEnum classType);

    public List<List<FlightScheduleEntity>> searchDirectFlightsAfter(String departureAirport, String destinationAirport, String departureDateTime, int numOfPassenger, CabinClassTypeEnum classType);

//    public List<List<FlightScheduleEntity>> searchConnectingFlights(String departureAirport, String destinationAirport, String departureDateTime, int numOfPassenger, int stopovers, CabinClassTypeEnum classType);
//
//    public List<List<FlightScheduleEntity>> searchConnectingFlightsBefore(String departureAirport, String destinationAirport, String departureDateTime, int numOfPassenger, int stopovers, CabinClassTypeEnum classType);
//
//    public List<List<FlightScheduleEntity>> searchConnectingFlightsAfter(String departureAirport, String destinationAirport, String departureDateTime, int numOfPassenger, int stopovers, CabinClassTypeEnum classType);
//
//    public void recurseTransit(List<FlightScheduleEntity> allSchedules, AirportEntity departureAirport, AirportEntity destinationAirport, int stopovers, List<FlightScheduleEntity> availableSchedule, List<List<FlightScheduleEntity>> finalSchedule, CabinClassTypeEnum classType, int numOfPassenger);
    public void setLayover(FlightSchedulePlanEntity plan, int layover);
}
