/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FlightEntity;
import entity.FlightScheduleEntity;
import entity.FlightSchedulePlanEntity;
import javax.ejb.Local;
import util.exception.ScheduleIsUsedException;

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

}
