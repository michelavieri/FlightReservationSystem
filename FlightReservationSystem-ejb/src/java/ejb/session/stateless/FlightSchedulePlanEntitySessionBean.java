/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

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
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.ScheduleIsUsedException;
import util.exception.ScheduleOverlapException;

/**
 *
 * @author Chrisya
 */
@Stateless
public class FlightSchedulePlanEntitySessionBean implements FlightSchedulePlanEntitySessionBeanRemote, FlightSchedulePlanEntitySessionBeanLocal {

    @EJB
    private FlightScheduleEntitySessionBeanLocal flightScheduleEntitySessionBean;

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;

    public FlightSchedulePlanEntitySessionBean() {
    }

    @Override
    public FlightSchedulePlanEntity createFlightSchedulePlanEntity(FlightSchedulePlanEntity newFlightSchedulePlan) {
        entityManager.persist(newFlightSchedulePlan);
        entityManager.flush();

        return newFlightSchedulePlan;
    }

    @Override
    public List<FlightSchedulePlanEntity> retrieveAllSchedulePlan() {
        Query query = entityManager.createQuery("SELECT p FROM FlightSchedulePlanEntity p ORDER BY p.schedulePlanId ASC, p.startDate DESC");
        
        List<FlightSchedulePlanEntity> plans = new ArrayList<>();
        try {
            plans = query.getResultList();
        } catch (NoResultException ex) {
            return plans;
        }
        return plans;
    }
      
    @Override
    public void associatePlanWithFlight(FlightSchedulePlanEntity plan, FlightEntity flight) {
        plan = entityManager.find(FlightSchedulePlanEntity.class, plan.getSchedulePlanId());
        flight = entityManager.find(FlightEntity.class, flight.getFlightId());

        plan.setFlight(flight);
        flight.getFlightSchedulePlans().size();
        flight.getFlightSchedulePlans().add(plan);
    }

    @Override
    public void deleteSchedulePlan(long schedulePlanId) throws ScheduleIsUsedException {

        Query query = entityManager.createQuery("SELECT p FROM FlightSchedulePlanEntity p WHERE p.schedulePlanId = :inPlanId");
        query.setParameter("inPlanId", schedulePlanId);

        FlightSchedulePlanEntity plan = (FlightSchedulePlanEntity) query.getSingleResult();

        List<FlightScheduleEntity> flightSchedules = plan.getFlightSchedules();
        flightSchedules.size();

        for (FlightScheduleEntity schedule : flightSchedules) {
            if (schedule.getReservations().size() == 0) {
                throw new ScheduleIsUsedException();
            }

            List<SeatsInventoryEntity> seats = schedule.getSeatsInventoryEntitys();
            seats.size();
            for (SeatsInventoryEntity seat : seats) {
                seat.setFlightSchedule(null);
                schedule.getSeatsInventoryEntitys().remove(seat);
                entityManager.remove(seat);
            }

            schedule.setPlan(null);
            plan.getFlightSchedules().remove(schedule);
            entityManager.remove(schedule);
        }
    }

    @Override
    public FlightSchedulePlanEntity retrieveSchedulePlanById(long planId) {
        Query query = entityManager.createQuery("SELECT p FROM FlightSchedulePlanEntity p WHERE p.schedulePlanId = :inPlanId");
        query.setParameter("inPlanId", planId);

        FlightSchedulePlanEntity plan = (FlightSchedulePlanEntity) query.getSingleResult();

        return plan;
    }

    @Override
    public FlightScheduleEntity retrieveScheduleSinglePlan(FlightSchedulePlanEntity plan) {
        plan = entityManager.find(FlightSchedulePlanEntity.class, plan.getSchedulePlanId());

        List<FlightScheduleEntity> schedules = plan.getFlightSchedules();
        schedules.size();

        return schedules.get(0);
    }

    @Override
    public FlightEntity retrieveFlightFromPlan(FlightSchedulePlanEntity plan) {
        plan = entityManager.find(FlightSchedulePlanEntity.class, plan.getSchedulePlanId());

        FlightEntity flight = plan.getFlight();

        return flight;
    }

    @Override
    public void replaceRecurrentSchedulePlan(FlightSchedulePlanEntity oldSchedulePlan, FlightSchedulePlanEntity newSchedulePlan, DateTimeFormatter dateFormat, String departureTime, String duration, int days, int layover) throws ScheduleIsUsedException, ScheduleOverlapException {
        oldSchedulePlan = entityManager.find(FlightSchedulePlanEntity.class, oldSchedulePlan.getSchedulePlanId());

        FlightEntity flight = oldSchedulePlan.getFlight();
        flight = entityManager.find(FlightEntity.class, flight.getFlightId());

        List<FlightScheduleEntity> schedules = oldSchedulePlan.getFlightSchedules();
        schedules.size();

        String newDepDate = newSchedulePlan.getStartDate();
        ZonedDateTime startingDate = ZonedDateTime.parse((newDepDate + departureTime), dateFormat);
        String startingTimeDate = startingDate.format(dateFormat);

        String newReturnDate = newSchedulePlan.getEndDate();
        ZonedDateTime endDate = ZonedDateTime.parse(newReturnDate, dateFormat);

        String oldDepDate = oldSchedulePlan.getStartDate();
        ZonedDateTime oldStartDate = ZonedDateTime.parse(oldDepDate, dateFormat);

        String oldReturnDate = newSchedulePlan.getEndDate();
        ZonedDateTime oldEndDate = ZonedDateTime.parse(oldReturnDate, dateFormat);

        String durationHour = duration.substring(0, 1);
        String durationMin = duration.substring(3, 4);

        int durationHourInt = Integer.parseInt(durationHour);
        int durationMinInt = Integer.parseInt(durationMin);
        int totalDuration = durationMinInt + (60 * durationHourInt);

        ZonedDateTime arrivalDateTime = startingDate.plusMinutes(totalDuration);
        String arrDateTime = arrivalDateTime.format(dateFormat);

        FlightScheduleEntity overlap = null;

        ZonedDateTime tempStart = startingDate;
        ZonedDateTime tempArrival = arrivalDateTime;

        String tempStartString = "";
        String tempArrString = "";

        while (startingDate.isBefore(endDate)) {
            startingTimeDate = startingDate.format(dateFormat);

            arrivalDateTime = startingDate.plusMinutes(totalDuration);
            arrDateTime = arrivalDateTime.format(dateFormat);

            FlightScheduleEntity schedule = new FlightScheduleEntity(startingTimeDate, totalDuration, arrDateTime);
            overlap = flightScheduleEntitySessionBean.checkOverlapPlan(flight, newSchedulePlan, schedule, dateFormat);

            if (overlap != null) {
                throw new ScheduleOverlapException();
            }

            startingDate = startingDate.plusDays(days);
            arrivalDateTime = arrivalDateTime.plusDays(days);
        }

        if (overlap == null) {

            tempStart = ZonedDateTime.parse((tempStart + departureTime), dateFormat);

            for (FlightScheduleEntity schedule : schedules) {
                tempStartString = tempStart.format(dateFormat);
                
                tempArrival = tempStart.plusMinutes(totalDuration);
                tempArrString = tempArrival.format(dateFormat);

                schedule.setDepartureDateTime(tempStartString);
                schedule.setArrivalDate(tempArrString);
                schedule.setDuration(totalDuration);

                if (schedule.getDepartureSchedule() != null && schedule.getReturnSchedule() == null) {
                    FlightScheduleEntity departureSchedule = schedule.getDepartureSchedule();
                    departureSchedule = entityManager.find(FlightScheduleEntity.class, departureSchedule.getScheduleId());

                    ZonedDateTime returnDepartureDateTime = ZonedDateTime.parse(departureSchedule.getArrivalDateTime(), dateFormat);
                    returnDepartureDateTime.plusMinutes(layover);
                    String returningDepartureDateTime = returnDepartureDateTime.format(dateFormat);

                    schedule.setDepartureDateTime(returningDepartureDateTime);

                    ZonedDateTime returnArrivalDateTime = returnDepartureDateTime.plusMinutes(totalDuration);
                    String returningArrivalDateTime = returnArrivalDateTime.format(dateFormat);

                    schedule.setArrivalDate(returningArrivalDateTime);
                    schedule.setDuration(totalDuration);
                }

                if (ZonedDateTime.parse(schedule.getDepartureDateTime(), dateFormat).isBefore(ZonedDateTime.parse(newDepDate, dateFormat))
                        || ZonedDateTime.parse(schedule.getDepartureDateTime(), dateFormat).isAfter(ZonedDateTime.parse(newReturnDate, dateFormat))) {
                    try {
                        flightScheduleEntitySessionBean.deleteSchedule(schedule);
                    } catch (ScheduleIsUsedException ex) {
                        throw new ScheduleIsUsedException();
                    }
                }

                tempStart = tempStart.plusDays(days);
                tempArrival = tempArrival.plusDays(days);
            }

            oldSchedulePlan.setStartDate(newDepDate);
            oldSchedulePlan.setEndDate(newReturnDate);

        }
    }
    
    @Override
    public void associateReturnSchedulePlan(FlightSchedulePlanEntity departure, FlightSchedulePlanEntity returning) {
        departure = entityManager.find(FlightSchedulePlanEntity.class, departure.getSchedulePlanId());
        returning = entityManager.find(FlightSchedulePlanEntity.class, returning.getSchedulePlanId());

        departure.setReturnSchedulePlan(returning);
        returning.setDepartureSchedulePlan(departure);
    }
    
    public String retrieveDepartureDateTime(FlightSchedulePlanEntity plan) {
        plan = entityManager.find(FlightSchedulePlanEntity.class, plan.getSchedulePlanId());
        
        plan.getFlightSchedules().size();
        
        return plan.getFlightSchedules().get(0).getDepartureDateTime();
    }
}
