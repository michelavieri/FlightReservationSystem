/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FlightEntity;
import entity.FlightScheduleEntity;
import entity.FlightSchedulePlanEntity;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.ejb.Local;
import util.exception.ScheduleIsUsedException;
import util.exception.ScheduleOverlapException;

/**
 *
 * @author Chrisya
 */
@Local
public interface FlightSchedulePlanEntitySessionBeanLocal {

    public FlightSchedulePlanEntity createFlightSchedulePlanEntity(FlightSchedulePlanEntity newFlightSchedulePlan);

    public void associatePlanWithFlight(FlightSchedulePlanEntity plan, FlightEntity flight);

    public void deleteSchedulePlan(long schedulePlanId) throws ScheduleIsUsedException;

    public FlightSchedulePlanEntity retrieveSchedulePlanById(long planId);

    public FlightScheduleEntity retrieveScheduleSinglePlan(FlightSchedulePlanEntity plan);

    public FlightEntity retrieveFlightFromPlan(FlightSchedulePlanEntity plan);

    public void replaceRecurrentSchedulePlan(FlightSchedulePlanEntity oldSchedulePlan, FlightSchedulePlanEntity newSchedulePlan, DateTimeFormatter dateFormat, String departureTime, String duration, int days, int layover) throws ScheduleIsUsedException, ScheduleOverlapException;

    public List<FlightSchedulePlanEntity> retrieveAllSchedulePlan();

    public void associateReturnSchedulePlan(FlightSchedulePlanEntity departure, FlightSchedulePlanEntity returning);

    public String retrieveDepartureDateTime(FlightSchedulePlanEntity plan);

    public List<FlightSchedulePlanEntity> retrievePlanByFlight(FlightEntity flight);

    public List<FlightScheduleEntity> retrieveSchedulesByPlan(FlightSchedulePlanEntity plan);

    public void createRecurrentSchedule(String day, FlightSchedulePlanEntity schedule, String startDate, String endDate, int days, String departureTime, DateTimeFormatter dateFormat, String duration, int layoverDuration);

}
