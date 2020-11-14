/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FlightSchedulePlanEntity;
import java.time.format.DateTimeFormatter;
import javax.ejb.Local;
import util.enumeration.FlightSchedulePlanTypeEnum;

/**
 *
 * @author Chrisya
 */
@Local
public interface InitializationSessionBeanLocal {

    public void initializeAirCraftConfiguration();

    public void initializeFlightRoute();

    public void initializeFlight();

    public void initializeSchedulePlan();

    public FlightSchedulePlanEntity initializeRecurringSchedulePlan(FlightSchedulePlanTypeEnum type, int dayRecurring, String flightCode, String day, String startDate, String endDate, String departureTime, String duration, int layover);

    public String createRecurrentSchedule(String day, FlightSchedulePlanEntity schedule, String startDate, String endDate, int days, String departureTime, DateTimeFormatter dateFormat, String duration, int layoverDuration);

    public FlightSchedulePlanEntity initializeMultipleSchedulePlan();
    
}