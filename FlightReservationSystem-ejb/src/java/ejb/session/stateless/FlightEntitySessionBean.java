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
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.FlightSchedulePlanTypeEnum;
import util.exception.FlightDisabledException;
import util.exception.FlightNotFoundException;

/**
 *
 * @author miche
 */
@Stateless
public class FlightEntitySessionBean implements FlightEntitySessionBeanRemote, FlightEntitySessionBeanLocal {

    @EJB
    private FlightScheduleEntitySessionBeanLocal flightScheduleEntitySessionBean;

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;

    public FlightEntitySessionBean() {
    }

    @Override
    public FlightEntity createFlightEntity(FlightEntity newFlight) {
        entityManager.persist(newFlight);

        AircraftConfigurationEntity config = entityManager.find(AircraftConfigurationEntity.class, newFlight.getAircraftConfigurationEntity().getId());
        FlightRouteEntity route = entityManager.find(FlightRouteEntity.class, newFlight.getRoute().getRouteId());

        List<FlightEntity> flights = config.getFlightEntitys();
        flights.add(newFlight);
        config.setFlightEntitys(flights);

        flights = route.getFlights();
        flights.add(newFlight);
        route.setFlights(flights);
        entityManager.flush();
        return newFlight;
    }

    @Override
    public List<FlightEntity> retrieveAllFlights() {

        Query query = entityManager.createQuery("SELECT f FROM FlightEntity f");
        List<FlightEntity> flights = query.getResultList();

        for (FlightEntity flight : flights) {
            flight.getFlightSchedulePlans().size();
            flight.getReturnFlight();
        }

        return flights;
    }

    @Override
    public List<FlightSchedulePlanEntity> retrieveSchedulePlans(FlightEntity flight) {
        flight = entityManager.find(FlightEntity.class, flight.getFlightId());

        flight.getFlightSchedulePlans().size();

        return flight.getFlightSchedulePlans();
    }

    @Override
    public FlightEntity retrieveFlightByCode(String code) throws FlightNotFoundException {

        Query query = entityManager.createQuery("SELECT f FROM FlightEntity f WHERE f.flightCode = :inCode");
        query.setParameter("inCode", code);

        try {
            return (FlightEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new FlightNotFoundException("Flight with code " + code + " does not exist!");
        }
    }

    @Override
    public void setReturnFlight(FlightEntity departureFlight, FlightEntity returnFlight) {
        FlightEntity departure = entityManager.find(FlightEntity.class, departureFlight.getFlightId());

        departure.setReturnFlight(returnFlight);
    }

    @Override
    public void setDepartureFlight(FlightEntity returnFlight, FlightEntity departureFlight) {
        FlightEntity returning = entityManager.find(FlightEntity.class, returnFlight.getFlightId());

        returning.setDepartureFlight(departureFlight);
    }

    @Override
    public List<CabinClassConfigurationEntity> retrieveCabinClassByFlight(FlightEntity flight) {
        flight = entityManager.find(FlightEntity.class, flight.getFlightId());

        List<CabinClassConfigurationEntity> cabinClass = flight.getAircraftConfigurationEntity().getCabinClassConfigurationEntitys();
        cabinClass.size();

        return cabinClass;
    }

    @Override
    public String retrieveTimeZoneByFlight(FlightEntity flight) {
        flight = entityManager.find(FlightEntity.class, flight.getFlightId());
        FlightRouteEntity route = flight.getRoute();

        route = entityManager.find(FlightRouteEntity.class, route.getRouteId());
        AirportEntity airport = route.getOriginAirport();

        airport = entityManager.find(AirportEntity.class, airport.getAirportId());
        airport.getArrivalRoutes().size();
        String zone = airport.getTimeZone();

        return zone;
    }

    @Override
    public void deleteFlight(FlightEntity flight) {
        flight = entityManager.find(FlightEntity.class, flight.getFlightId());
        FlightEntity returnFlight = entityManager.find(FlightEntity.class, flight.getReturnFlight().getFlightId());
        FlightEntity departureFlight = entityManager.find(FlightEntity.class, flight.getDepartureFlight().getFlightId());

        if (flight.getFlightSchedulePlans().isEmpty()) {
            AircraftConfigurationEntity aircraftConfig = flight.getAircraftConfigurationEntity();
            List<FlightEntity> flights = aircraftConfig.getFlightEntitys();
            flights.remove(flight);
            aircraftConfig.setFlightEntitys(flights);

            FlightRouteEntity route = flight.getRoute();
            flights = route.getFlights();
            flights.remove(flight);
            route.setFlights(flights);

            //return flight
            if (departureFlight != null) {
                departureFlight.setReturnFlight(null);
            } else if (returnFlight != null) { //departureflight with return
                returnFlight.setDepartureFlight(null);
            }

            entityManager.remove(flight);
        } else {
            flight.setDisabled(true);
        }
    }

    @Override
    public void updateAircraftConfiguration(AircraftConfigurationEntity newConfig, FlightEntity flight) {
        flight = entityManager.find(FlightEntity.class, flight.getFlightId());
        newConfig = entityManager.find(AircraftConfigurationEntity.class, newConfig.getId());
       // FlightEntity returnFlight = entityManager.find(FlightEntity.class, flight.getReturnFlight());

        AircraftConfigurationEntity oldConfig = flight.getAircraftConfigurationEntity();
        oldConfig = entityManager.find(AircraftConfigurationEntity.class, oldConfig.getId());
        oldConfig.getFlightEntitys().size();
        List<FlightEntity> flights = oldConfig.getFlightEntitys();
        flights.remove(flight);
       // flights.remove(returnFlight);
        oldConfig.setFlightEntitys(flights);

        flights = newConfig.getFlightEntitys();
        flights.add(flight);
       // flights.add(flight);
        newConfig.setFlightEntitys(flights);

        flight.setAircraftConfigurationEntity(newConfig);
       // returnFlight.setAircraftConfigurationEntity(newConfig);
    }

    @Override
    public void updateFlightRoute(FlightRouteEntity newRoute, FlightEntity flight) {
        flight = entityManager.find(FlightEntity.class, flight.getFlightId());
        newRoute = entityManager.find(FlightRouteEntity.class, newRoute.getRouteId());
        FlightRouteEntity oldRoute = entityManager.find(FlightRouteEntity.class, flight.getRoute().getRouteId());

        List<FlightEntity> flights = oldRoute.getFlights();
        flights.remove(flight);
        oldRoute.setFlights(flights);

        flights = newRoute.getFlights();
        flights.add(flight);
        newRoute.setFlights(flights);

        flight.setRoute(newRoute);

        FlightEntity returnFlight = flight.getReturnFlight();

        if (returnFlight != null) {
            FlightRouteEntity returnRoute = newRoute.getReturnFlightRoute();
            returnRoute = entityManager.find(FlightRouteEntity.class, returnRoute.getRouteId());

            if (returnRoute != null) {
                oldRoute = returnFlight.getRoute();
                flights = oldRoute.getFlights();
                flights.remove(returnFlight);
                oldRoute.setFlights(flights);

                flights = returnRoute.getFlights();
                flights.add(returnFlight);
                returnRoute.setFlights(flights);

                returnFlight.setRoute(returnRoute);
            } else {
                returnFlight = entityManager.find(FlightEntity.class, returnFlight.getFlightId());
                returnFlight.setDepartureFlight(null);
                flight.setReturnFlight(null);
                entityManager.remove(returnFlight);
            }
        }
    }

    @Override
    public boolean isReturnFlight(FlightEntity flight) {
        flight = entityManager.find(FlightEntity.class, flight.getFlightId());
        if (flight.getDepartureFlight() != null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean hasReturnFlight(FlightEntity flight) {
        flight = entityManager.find(FlightEntity.class, flight.getFlightId());
        if (flight.getReturnFlight() != null) {
            return true;
        }
        return false;
    }

    @Override
    public void removeReturnFlight(FlightEntity flight) {
        flight = entityManager.find(FlightEntity.class, flight.getFlightId());
        FlightEntity returnFlight = flight.getReturnFlight();

        returnFlight.setDepartureFlight(null);
        flight.setReturnFlight(null);

        entityManager.remove(returnFlight);
    }

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
