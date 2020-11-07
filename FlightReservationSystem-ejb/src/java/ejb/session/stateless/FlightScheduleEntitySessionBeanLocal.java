/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FlightScheduleEntity;
import entity.FlightSchedulePlanEntity;
import java.time.format.DateTimeFormatter;
import javax.ejb.Local;

/**
 *
 * @author Chrisya
 */
@Local
public interface FlightScheduleEntitySessionBeanLocal {

    public FlightScheduleEntity createFlightScheduleEntity(FlightScheduleEntity newFlightSchedule);

    public void associateWithPlan(FlightScheduleEntity schedule, FlightSchedulePlanEntity schedulePlan);

    public void associateNewSeatsInventory(FlightScheduleEntity schedule);

    public void associateReturnSchedule(FlightScheduleEntity departure, FlightScheduleEntity returning);

    public void createRecurrentSchedule(FlightSchedulePlanEntity schedule, String startDate, String endDate, int days, String departureTime, DateTimeFormatter dateFormat, String duration, boolean returning, int layoverDuration);
    
}
