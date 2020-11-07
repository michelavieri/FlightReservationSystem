/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftConfigurationEntity;
import entity.CabinClassConfigurationEntity;
import entity.FlightEntity;
import entity.FlightScheduleEntity;
import entity.FlightSchedulePlanEntity;
import entity.SeatsInventoryEntity;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Chrisya
 */
@Stateless
public class FlightScheduleEntitySessionBean implements FlightScheduleEntitySessionBeanRemote, FlightScheduleEntitySessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;

    public FlightScheduleEntitySessionBean() {
    }

    @Override
    public FlightScheduleEntity createFlightScheduleEntity(FlightScheduleEntity newFlightSchedule) {
        entityManager.persist(newFlightSchedule);
        entityManager.flush();

        return newFlightSchedule;
    }

    @Override
    public void associateWithPlan(FlightScheduleEntity schedule, FlightSchedulePlanEntity schedulePlan) {

        schedule = entityManager.find(FlightScheduleEntity.class, schedule.getScheduleId());
        schedulePlan = entityManager.find(FlightSchedulePlanEntity.class, schedulePlan.getSchedulePlanId());

        schedule.setPlan(schedulePlan);

        schedulePlan.getFlightSchedules().add(schedule);
    }

    @Override
    public void associateNewSeatsInventory(FlightScheduleEntity schedule) {

        schedule = entityManager.find(FlightScheduleEntity.class, schedule.getScheduleId());
        FlightSchedulePlanEntity plan = schedule.getPlan();
        
        plan = entityManager.find(FlightSchedulePlanEntity.class, plan.getSchedulePlanId());
        FlightEntity flight = plan.getFlight();
        
        flight = entityManager.find(FlightEntity.class, flight.getFlightId());
        AircraftConfigurationEntity aircraftConfig = flight.getAircraftConfigurationEntity();
        
        aircraftConfig = entityManager.find(AircraftConfigurationEntity.class, aircraftConfig.getId());
        
        List<CabinClassConfigurationEntity> cabinClasses = aircraftConfig.getCabinClassConfigurationEntitys();

        for (CabinClassConfigurationEntity cabinClass : cabinClasses) {

            int maxCapacity = cabinClass.getMaxCapacity();

            SeatsInventoryEntity seatsInventory = new SeatsInventoryEntity(maxCapacity, 0, 0);

            entityManager.persist(seatsInventory);
            entityManager.flush();

        }
    }
    
    @Override
    public void associateReturnSchedule(FlightScheduleEntity departure, FlightScheduleEntity returning) {
        departure = entityManager.find(FlightScheduleEntity.class, departure.getScheduleId());
        returning = entityManager.find(FlightScheduleEntity.class, returning.getScheduleId());
        
        departure.setReturnSchedule(returning);
        returning.setDepartureSchedule(departure);
    }

    @Override
    public void createRecurrentSchedule(FlightSchedulePlanEntity schedule, String startDate, String endDate, int days, String departureTime, DateTimeFormatter dateFormat, String duration, boolean returning, int layoverDuration) {

        ZonedDateTime startingDate = ZonedDateTime.parse((startDate + departureTime), dateFormat);
        ZonedDateTime endingDate = ZonedDateTime.parse(endDate, dateFormat);

        String durationHour = duration.substring(0, 1);
        String durationMin = duration.substring(3, 4);

        int durationHourInt = Integer.parseInt(durationHour);
        int durationMinInt = Integer.parseInt(durationMin);
        int totalDuration = durationMinInt + (60 * durationHourInt);
        
        ZonedDateTime arrivalDateTime = startingDate.plusMinutes(totalDuration);
        String arrDateTime = arrivalDateTime.format(dateFormat);
        
        while (startingDate.isBefore(endingDate)) {
            FlightScheduleEntity departure = this.createFlightScheduleEntity(new FlightScheduleEntity(startDate, totalDuration, arrDateTime));
            this.associateWithPlan(departure, schedule);
            
            if(returning) {
                ZonedDateTime returnDateTime = arrivalDateTime.plusMinutes(layoverDuration);
                String returnDateTimeFormat = returnDateTime.format(dateFormat);
                String arrReturnDateTime = returnDateTime.plusMinutes(totalDuration).format(dateFormat);
                
                FlightScheduleEntity returnSchedule = this.createFlightScheduleEntity(new FlightScheduleEntity(returnDateTimeFormat, totalDuration, arrReturnDateTime));
                this.associateWithPlan(returnSchedule, schedule);
                this.associateReturnSchedule(departure, returnSchedule);
            }
            
            startingDate = startingDate.plusDays(days);
        }
    }

}
