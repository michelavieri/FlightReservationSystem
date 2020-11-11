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
import javax.ejb.Remote;
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

    public boolean checkOverlapSchedule(FlightSchedulePlanEntity plan, FlightScheduleEntity schedule, DateTimeFormatter dateFormat) throws ScheduleOverlapException;

    public FlightScheduleEntity checkOverlapPlan(FlightEntity flight, FlightSchedulePlanEntity schedulePlan, FlightScheduleEntity schedule, DateTimeFormatter dateFormat);

    public FlightScheduleEntity overlapSchedule(FlightSchedulePlanEntity plan, FlightScheduleEntity schedule, DateTimeFormatter dateFormat);

    public void replaceSchedule(FlightScheduleEntity oldSchedule, FlightScheduleEntity newSchedule);

    public void deleteSchedule(FlightScheduleEntity schedule) throws ScheduleIsUsedException;

    public FlightScheduleEntity retrieveFlightScheduleById(Long id);

    public FlightScheduleEntity retrieveReturnSchedule(FlightScheduleEntity schedule);

}
