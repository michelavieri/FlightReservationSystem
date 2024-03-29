/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FareEntity;
import entity.FlightEntity;
import entity.FlightScheduleEntity;
import entity.FlightSchedulePlanEntity;
import entity.SeatEntity;
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
import util.enumeration.FlightSchedulePlanTypeEnum;
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
        List<FareEntity> fares = plan.getFareEntitys();
        fares.size();

        if (plan.getReturnSchedulePlan() != null) {
            FlightSchedulePlanEntity returnPlan = plan.getReturnSchedulePlan();

            try {
                deleteSchedulePlan(returnPlan.getSchedulePlanId());
            } catch (ScheduleIsUsedException ex) {
                throw new ScheduleIsUsedException();
            }
        }

        for (FlightScheduleEntity schedule : flightSchedules) {
            if (schedule.getBookingTicketEntitys().size() > 0) {
                plan.setDisabled(true);
                throw new ScheduleIsUsedException();
            }

            List<SeatsInventoryEntity> seats = schedule.getSeatsInventoryEntitys();
            seats.size();
            for (SeatsInventoryEntity seat : seats) {
                List<SeatEntity> individualSeats = seat.getSeats();

                for (SeatEntity individualSeat : individualSeats) {
                    individualSeat.setSeatsInventory(null);
                    entityManager.remove(individualSeat);
                }

                seat.getSeats().clear();
                seat.setFlightSchedule(null);
                entityManager.remove(seat);
            }

            schedule.getSeatsInventoryEntitys().clear();
            schedule.setPlan(null);
            entityManager.remove(schedule);
        }

        for (FareEntity fare : fares) {
            fare.setFlightSchedulePlan(null);
            entityManager.remove(fare);
        }

        plan.getFareEntitys().clear();
        plan.getFlightSchedules().clear();
        entityManager.remove(plan);
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

    @Override
    public String retrieveDepartureDateTime(FlightSchedulePlanEntity plan) {
        plan = entityManager.find(FlightSchedulePlanEntity.class, plan.getSchedulePlanId());

        plan.getFlightSchedules().size();

        return plan.getFlightSchedules().get(0).getDepartureDateTime();
    }

    @Override
    public List<FlightSchedulePlanEntity> retrievePlanByFlight(FlightEntity flight) {
        flight = entityManager.find(FlightEntity.class, flight.getFlightId());

        List<FlightSchedulePlanEntity> plans = flight.getFlightSchedulePlans();
        plans.size();

        return plans;
    }

    @Override
    public List<FlightScheduleEntity> retrieveSchedulesByPlan(FlightSchedulePlanEntity plan) {
        plan = entityManager.find(FlightSchedulePlanEntity.class, plan.getSchedulePlanId());

        List<FlightScheduleEntity> schedules = plan.getFlightSchedules();
        schedules.size();

        return schedules;
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

            FlightScheduleEntity departure = flightScheduleEntitySessionBean.createFlightScheduleEntity(new FlightScheduleEntity(startingDateTime, totalDuration, arrDateTime));
            flightScheduleEntitySessionBean.associateWithPlan(departure, schedule);

            startingDate = startingDate.plusDays(days);
            arrivalDateTime = arrivalDateTime.plusDays(days);
        }
    }
}
