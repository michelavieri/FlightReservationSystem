/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftConfigurationEntity;
import entity.AirportEntity;
import entity.CabinClassConfigurationEntity;
import entity.FlightEntity;
import entity.FlightRouteEntity;
import entity.FlightScheduleEntity;
import entity.FlightSchedulePlanEntity;
import entity.SeatsInventoryEntity;
import java.time.LocalDate;
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
import util.enumeration.CabinClassTypeEnum;
import util.enumeration.FlightSchedulePlanTypeEnum;
import util.exception.FlightScheduleNotFoundException;
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

        int startSeatNumber = 0;

        for (FlightScheduleEntity schedule : schedules) {
            for (CabinClassConfigurationEntity cabinClass : cabinClasses) {

                int maxCapacity = cabinClass.getMaxCapacity();

                SeatsInventoryEntity seatsInventory = new SeatsInventoryEntity(maxCapacity, 0, maxCapacity);

                seatsInventory = seatsInventoryEntitySessionBean.createSeatsInventoryEntity(seatsInventory);
                seatsInventory.setFlightSchedule(schedule);
                seatsInventory.setCabinClass(cabinClass);
                schedule.getSeatsInventoryEntitys().add(seatsInventory);

                startSeatNumber = seatsInventoryEntitySessionBean.createSeatsFromSeatInventory(seatsInventory, startSeatNumber);
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
        schedule = entityManager.find(FlightSchedulePlanEntity.class, schedule.getSchedulePlanId());
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
    public boolean checkOverlapping(FlightSchedulePlanEntity plan, FlightScheduleEntity schedule, DateTimeFormatter dateFormat) throws ScheduleOverlapException {
        plan = entityManager.find(FlightSchedulePlanEntity.class, plan.getSchedulePlanId());
        schedule = entityManager.find(FlightScheduleEntity.class, schedule.getScheduleId());
        boolean overlap = false;

        FlightEntity flight = plan.getFlight();
        flight.getFlightSchedulePlans().size();
        List<FlightSchedulePlanEntity> plans = flight.getFlightSchedulePlans();

        for (FlightSchedulePlanEntity schedulePlan : plans) {
            if (!schedulePlan.equals(plan)) {

                List<FlightScheduleEntity> schedules = schedulePlan.getFlightSchedules();
                schedules.size();

                for (FlightScheduleEntity otherSchedule : schedules) {
                    ZonedDateTime oldScheduleDep = ZonedDateTime.parse(otherSchedule.getDepartureDateTime(), dateFormat);
                    ZonedDateTime oldScheduleArr = ZonedDateTime.parse(otherSchedule.getArrivalDateTime(), dateFormat);

                    ZonedDateTime newScheduleDep = ZonedDateTime.parse(schedule.getDepartureDateTime(), dateFormat);
                    ZonedDateTime newScheduleArr = ZonedDateTime.parse(schedule.getArrivalDateTime(), dateFormat);

                    if (newScheduleDep.isAfter(oldScheduleArr) || newScheduleArr.isBefore(oldScheduleDep)) {
                        overlap = false;
                    } else {
                        throw new ScheduleOverlapException("Overlap!");
                    }
                }
            }
        }

        return false;
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

        if (schedule.getBookingTicketEntitys().size() > 0) {
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
    public FlightScheduleEntity retrieveFlightScheduleById(Long id) throws FlightScheduleNotFoundException {
        Query query = entityManager.createQuery("SELECT s FROM FlightScheduleEntity s WHERE s.scheduleId = :id");
        query.setParameter("id", id);
        FlightScheduleEntity schedule = null;
        try {
            schedule = (FlightScheduleEntity) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new FlightScheduleNotFoundException("This flight schedule does not exist!");
        }

        return schedule;
    }

    @Override
    public FlightScheduleEntity retrieveReturnSchedule(FlightScheduleEntity schedule
    ) {
        schedule = entityManager.find(FlightScheduleEntity.class, schedule.getScheduleId());

        FlightScheduleEntity returnSchedule = schedule.getReturnSchedule();
//        returnSchedule.getSeatsInventoryEntitys().size();

        return returnSchedule;
    }

    @Override
    public List<List<FlightScheduleEntity>> searchDirectFlights(String departureAirport, String destinationAirport,
             String departureDate, int numOfPassenger, CabinClassTypeEnum classType
    ) {
        List<List<FlightScheduleEntity>> finalSchedule = new ArrayList<>();

        Query query = entityManager.createQuery("SELECT a FROM AirportEntity a WHERE a.airportCode = :depAirportCode");
        query.setParameter("depAirportCode", departureAirport);
        AirportEntity departure = (AirportEntity) query.getSingleResult();

        query = entityManager.createQuery("SELECT a FROM AirportEntity a WHERE a.airportCode = :destAirportCode");
        query.setParameter("destAirportCode", destinationAirport);
        AirportEntity destination = (AirportEntity) query.getSingleResult();

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        query = entityManager.createQuery("SELECT a FROM FlightScheduleEntity a");
        List<FlightScheduleEntity> allSchedules = query.getResultList();

        for (FlightScheduleEntity schedule : allSchedules) {
            FlightRouteEntity route = schedule.getPlan().getFlight().getRoute();
            if (route.getOriginAirport().equals(departure) && route.getDestinationAirport().equals(destination)) {

                List<SeatsInventoryEntity> seats = schedule.getSeatsInventoryEntitys();

                if (classType != null) {
                    for (SeatsInventoryEntity seat : seats) {
                        if (seat.getCabinClass().getType().equals(classType)) {
                            List<FlightScheduleEntity> availableSchedule = new ArrayList<>();
                            availableSchedule.add(schedule);
                            finalSchedule.add(availableSchedule);
                        }
                    }
                } else {
                    List<FlightScheduleEntity> availableSchedule = new ArrayList<>();
                    availableSchedule.add(schedule);
                    finalSchedule.add(availableSchedule);
                }

            }
        }
        return finalSchedule;
    }

    @Override
    public List<List<FlightScheduleEntity>> searchDirectFlightsUnmanaged(String departureAirport, String destinationAirport,
             String departureDate, int numOfPassenger, CabinClassTypeEnum classType) {
        List<List<FlightScheduleEntity>> finalSchedule = searchDirectFlights(departureAirport, destinationAirport, departureDate, numOfPassenger, classType);
        
        for(List<FlightScheduleEntity> list:finalSchedule) {
            for(FlightScheduleEntity schedule:list) {
                s
            }
        }
        return finalSchedule;
    }
    
    @Override
    public List<List<FlightScheduleEntity>> searchDirectFlightsBefore(String departureAirport, String destinationAirport,
             String departureDateTime, int numOfPassenger, CabinClassTypeEnum classType
    ) {
        List<List<FlightScheduleEntity>> finalSchedule = new ArrayList<>();

        Query query = entityManager.createQuery("SELECT a FROM AirportEntity a WHERE a.airportCode = :depAirportCode");
        query.setParameter("depAirportCode", departureAirport);
        AirportEntity departure = (AirportEntity) query.getSingleResult();

        query = entityManager.createQuery("SELECT a FROM AirportEntity a WHERE a.airportCode = :destAirportCode");
        query.setParameter("destAirportCode", destinationAirport);
        AirportEntity destination = (AirportEntity) query.getSingleResult();

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        query = entityManager.createQuery("SELECT a FROM FlightScheduleEntity a");
        List<FlightScheduleEntity> allSchedules = query.getResultList();

        for (FlightScheduleEntity schedule : allSchedules) {
            FlightRouteEntity route = schedule.getPlan().getFlight().getRoute();
            if (route.getOriginAirport().equals(departure) && route.getDestinationAirport().equals(destination)) {
                String depDateTime = schedule.getDepartureDateTime();
                depDateTime = depDateTime.substring(0, 10);
                LocalDate depDate = LocalDate.parse(depDateTime, dateFormat);
                LocalDate departureDate = LocalDate.parse(departureDateTime, dateFormat);

                if (depDate.equals(departureDate.minusDays(1)) || depDate.equals(departureDate.minusDays(2)) || depDate.equals(departureDate.minusDays(3))) {
                    List<SeatsInventoryEntity> seats = schedule.getSeatsInventoryEntitys();

                    if (classType != null) {
                        for (SeatsInventoryEntity seat : seats) {
                            if (seat.getCabinClass().getType().equals(classType)) {
                                List<FlightScheduleEntity> availableSchedule = new ArrayList<>();
                                availableSchedule.add(schedule);
                                finalSchedule.add(availableSchedule);
                            }
                        }
                    } else {
                        List<FlightScheduleEntity> availableSchedule = new ArrayList<>();
                        availableSchedule.add(schedule);
                        finalSchedule.add(availableSchedule);
                    }
                }

            }
        }

        return finalSchedule;
    }

    @Override
    public List<List<FlightScheduleEntity>> searchDirectFlightsAfter(String departureAirport, String destinationAirport,
             String departureDateTime, int numOfPassenger, CabinClassTypeEnum classType
    ) {
        List<List<FlightScheduleEntity>> finalSchedule = new ArrayList<>();

        Query query = entityManager.createQuery("SELECT a FROM AirportEntity a WHERE a.airportCode = :depAirportCode");
        query.setParameter("depAirportCode", departureAirport);
        AirportEntity departure = (AirportEntity) query.getSingleResult();

        query = entityManager.createQuery("SELECT a FROM AirportEntity a WHERE a.airportCode = :destAirportCode");
        query.setParameter("destAirportCode", destinationAirport);
        AirportEntity destination = (AirportEntity) query.getSingleResult();

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        query = entityManager.createQuery("SELECT a FROM FlightScheduleEntity a");
        List<FlightScheduleEntity> allSchedules = query.getResultList();

        for (FlightScheduleEntity schedule : allSchedules) {
            FlightRouteEntity route = schedule.getPlan().getFlight().getRoute();
            if (route.getOriginAirport().equals(departure) && route.getDestinationAirport().equals(destination)) {
                String depDateTime = schedule.getDepartureDateTime();
                depDateTime = depDateTime.substring(0, 10);
                LocalDate depDate = LocalDate.parse(depDateTime, dateFormat);
                LocalDate departureDate = LocalDate.parse(departureDateTime, dateFormat);

                if (depDate.equals(departureDate.plusDays(1)) || depDate.equals(departureDate.plusDays(2)) || depDate.equals(departureDate.plusDays(3))) {
                    List<SeatsInventoryEntity> seats = schedule.getSeatsInventoryEntitys();

                    if (classType != null) {
                        for (SeatsInventoryEntity seat : seats) {
                            if (seat.getCabinClass().getType().equals(classType)) {
                                List<FlightScheduleEntity> availableSchedule = new ArrayList<>();
                                availableSchedule.add(schedule);
                                finalSchedule.add(availableSchedule);
                            }
                        }
                    } else {
                        List<FlightScheduleEntity> availableSchedule = new ArrayList<>();
                        availableSchedule.add(schedule);
                        finalSchedule.add(availableSchedule);
                    }
                }

            }
        }
        return finalSchedule;
    }

    @Override
    public void setLayover(FlightSchedulePlanEntity plan, int layover
    ) {
        plan = entityManager.find(FlightSchedulePlanEntity.class, plan.getSchedulePlanId());

        plan.setLayoverDuration(layover);
    }
}
