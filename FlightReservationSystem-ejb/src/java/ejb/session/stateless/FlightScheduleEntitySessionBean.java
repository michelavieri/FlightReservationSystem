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
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.FlightSchedulePlanTypeEnum;
import util.exception.ScheduleIsUsedException;
import util.exception.ScheduleOverlapException;

/**
 *
 * @author Chrisya
 */
@Stateless
public class FlightScheduleEntitySessionBean implements FlightScheduleEntitySessionBeanRemote, FlightScheduleEntitySessionBeanLocal {

    @EJB
    private SeatsInventoryEntitySessionBeanLocal seatsInventoryEntitySessionBean;

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
    public void associateNewSeatsInventory(FlightSchedulePlanEntity plan) {

        plan = entityManager.find(FlightSchedulePlanEntity.class, plan.getSchedulePlanId());
        List<FlightScheduleEntity> schedules = plan.getFlightSchedules();;
        schedules.size();

        FlightEntity flight = plan.getFlight();

        flight = entityManager.find(FlightEntity.class, flight.getFlightId());
        AircraftConfigurationEntity aircraftConfig = flight.getAircraftConfigurationEntity();

        aircraftConfig = entityManager.find(AircraftConfigurationEntity.class, aircraftConfig.getId());

        List<CabinClassConfigurationEntity> cabinClasses = aircraftConfig.getCabinClassConfigurationEntitys();

        for (FlightScheduleEntity schedule : schedules) {
            for (CabinClassConfigurationEntity cabinClass : cabinClasses) {

                int maxCapacity = cabinClass.getMaxCapacity();

                SeatsInventoryEntity seatsInventory = new SeatsInventoryEntity(maxCapacity, 0, 0);

                seatsInventory = seatsInventoryEntitySessionBean.createSeatsInventoryEntity(seatsInventory);
                seatsInventory.setFlightSchedule(schedule);
                schedule.getSeatsInventoryEntitys().add(seatsInventory);
            }
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
    public void createRecurrentSchedule(String day, FlightSchedulePlanEntity schedule, String startDate, String endDate, int days, String departureTime, DateTimeFormatter dateFormat, String duration, int layoverDuration) {

        ZonedDateTime startingDate = ZonedDateTime.parse((startDate + departureTime), dateFormat);

        if (schedule.getType().equals(FlightSchedulePlanTypeEnum.RECURRENT_WEEK)) {
            while (!startingDate.getDayOfWeek().toString().equals(day)) {
                startingDate = startingDate.plusDays(1);
            }
        }

        String startingDateTime = startingDate.format(dateFormat);
        ZonedDateTime endingDate = ZonedDateTime.parse(endDate, dateFormat);

        String durationHour = duration.substring(0, 1);
        String durationMin = duration.substring(3, 4);

        int durationHourInt = Integer.parseInt(durationHour);
        int durationMinInt = Integer.parseInt(durationMin);
        int totalDuration = durationMinInt + (60 * durationHourInt);

        ZonedDateTime arrivalDateTime = startingDate.plusMinutes(totalDuration);
        String arrDateTime = arrivalDateTime.format(dateFormat);

        while (startingDate.isBefore(endingDate)) {
            startingDate = ZonedDateTime.parse((startDate + departureTime), dateFormat);
            startingDateTime = startingDate.format(dateFormat);

            arrivalDateTime = startingDate.plusMinutes(totalDuration);
            arrDateTime = arrivalDateTime.format(dateFormat);

            FlightScheduleEntity departure = this.createFlightScheduleEntity(new FlightScheduleEntity(startingDateTime, totalDuration, arrDateTime));
            this.associateWithPlan(departure, schedule);

            startingDate = startingDate.plusDays(days);
            arrivalDateTime = arrivalDateTime.plusDays(days);
        }
    }

    @Override
    public boolean checkOverlapSchedule(FlightSchedulePlanEntity plan, FlightScheduleEntity schedule, DateTimeFormatter dateFormat) throws ScheduleOverlapException {
        plan = entityManager.find(FlightSchedulePlanEntity.class, plan.getSchedulePlanId());
        schedule = entityManager.find(FlightScheduleEntity.class, schedule.getScheduleId());

        FlightScheduleEntity overlapSchedule = this.overlapSchedule(plan, schedule, dateFormat);

        if (overlapSchedule == null) {
            return false;
        } else {
            throw new ScheduleOverlapException();
        }
    }

    @Override
    public FlightScheduleEntity overlapSchedule(FlightSchedulePlanEntity plan, FlightScheduleEntity schedule, DateTimeFormatter dateFormat) {
        FlightScheduleEntity overlap = null;
        plan = entityManager.find(FlightSchedulePlanEntity.class, plan.getSchedulePlanId());
        schedule = entityManager.find(FlightScheduleEntity.class, schedule.getScheduleId());

        List<FlightScheduleEntity> schedules = plan.getFlightSchedules();
        schedules.size();

        for (FlightScheduleEntity planSchedule : schedules) {
            ZonedDateTime planScheduleDepTime = ZonedDateTime.parse(planSchedule.getDepartureDateTime(), dateFormat);
            ZonedDateTime planScheduleArrTime = ZonedDateTime.parse(planSchedule.getArrivalDateTime(), dateFormat);
            ZonedDateTime scheduleDepTime = ZonedDateTime.parse(schedule.getDepartureDateTime());
            ZonedDateTime scheduleArrTime = ZonedDateTime.parse(schedule.getArrivalDateTime());

            if (scheduleDepTime.isAfter(planScheduleArrTime) || scheduleArrTime.isBefore(planScheduleDepTime)) {

            } else {
                overlap = planSchedule;
            }
        }

        return overlap;
    }

    @Override
    public FlightScheduleEntity checkOverlapPlan(FlightEntity flight, FlightSchedulePlanEntity schedulePlan, FlightScheduleEntity schedule, DateTimeFormatter dateFormat) {
        FlightScheduleEntity overlap = null;
        flight = entityManager.find(FlightEntity.class, flight.getFlightId());
        schedule = entityManager.find(FlightScheduleEntity.class, schedule.getScheduleId());
        schedulePlan = entityManager.find(FlightSchedulePlanEntity.class, schedulePlan.getSchedulePlanId());

        List<FlightSchedulePlanEntity> plans = flight.getFlightSchedulePlans();

        for (FlightSchedulePlanEntity plan : plans) {
            if (!plan.equals(schedulePlan)) {
                overlap = overlapSchedule(plan, schedule, dateFormat);

                if (overlap != null) {
                    return overlap;
                }

            }
        }

        return null;
    }

    @Override
    public void replaceSchedule(FlightScheduleEntity oldSchedule, FlightScheduleEntity newSchedule) {
        oldSchedule = entityManager.find(FlightScheduleEntity.class, oldSchedule.getScheduleId());
        newSchedule = entityManager.find(FlightScheduleEntity.class, newSchedule.getScheduleId());

        oldSchedule.setDepartureDateTime(newSchedule.getDepartureDateTime());
        oldSchedule.setArrivalDate(newSchedule.getArrivalDateTime());
        oldSchedule.setDuration(newSchedule.getDuration());
    }

    @Override
    public void deleteSchedule(FlightScheduleEntity schedule) throws ScheduleIsUsedException {
        schedule = entityManager.find(FlightScheduleEntity.class, schedule.getScheduleId());
        
        if(schedule.getBookingTicketEntitys().size() > 0) {
            throw new ScheduleIsUsedException();
        }

        FlightSchedulePlanEntity plan = schedule.getPlan();
        plan.getFlightSchedules().remove(schedule);

        FlightScheduleEntity returnSchedule = schedule.getReturnSchedule();
        returnSchedule.setDepartureSchedule(null);
        schedule.setReturnSchedule(null);
        plan.getFlightSchedules().remove(returnSchedule);
        entityManager.remove(returnSchedule);
        entityManager.remove(schedule);
    }

    @Override
    public FlightScheduleEntity retrieveFlightScheduleById(Long id) {
        Query query = entityManager.createQuery("SELECT s FROM FlightScheduleEntity s WHERE s.scheduleId = :id");
        query.setParameter("id", id);

        FlightScheduleEntity schedule = (FlightScheduleEntity) query.getSingleResult();

        return schedule;
    }

    @Override
    public FlightScheduleEntity retrieveReturnSchedule(FlightScheduleEntity schedule) {
        schedule = entityManager.find(FlightScheduleEntity.class, schedule.getScheduleId());

        FlightScheduleEntity returnSchedule = schedule.getReturnSchedule();
        returnSchedule.getSeatsInventoryEntitys().size();

        return returnSchedule;
    }
}
