/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AirportEntity;
import entity.FlightEntity;
import entity.FlightRouteEntity;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.FlightRouteDisabled;
import util.exception.FlightRouteNotFoundException;

/**
 *
 * @author miche
 */
@Stateless
public class FlightRouteEntitySessionBean implements FlightRouteEntitySessionBeanRemote, FlightRouteEntitySessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;

    public FlightRouteEntitySessionBean() {
    }

    @Override
    public FlightRouteEntity createFlightRouteEntity(FlightRouteEntity newRoute) {
        entityManager.persist(newRoute);
        entityManager.flush();

        return newRoute;
    }

    @Override
    public void setReturnFlightRoute(FlightRouteEntity departureFlightRoute, FlightRouteEntity returnFlightRoute) {
        FlightRouteEntity route = entityManager.find(FlightRouteEntity.class, departureFlightRoute.getRouteId());

        route.setReturnFlightRoute(returnFlightRoute);
    }

    @Override
    public void setDepartureFlightRoute(FlightRouteEntity departureFlightRoute, FlightRouteEntity returnFlightRoute) {
        FlightRouteEntity route = entityManager.find(FlightRouteEntity.class, returnFlightRoute.getRouteId());

        route.setDepartureFlightRoute(departureFlightRoute);
    }

    @Override
    public List<FlightRouteEntity> retrieveAllRoutes() {
        Query query = entityManager.createQuery("SELECT r FROM FlightRouteEntity r");
        List<FlightRouteEntity> routes = new ArrayList<>();
        try {
            routes = query.getResultList();
        } catch (NoResultException ex) {
            return routes;
        }
        return routes;
    }
    
    @Override
    public List<FlightRouteEntity> retrieveAllAvailableRoutes() {
        Query query = entityManager.createQuery("SELECT r FROM FlightRouteEntity r WHERE r.disabled = FALSE");
        List<FlightRouteEntity> routes = new ArrayList<>();
        try {
            routes = query.getResultList();
        } catch (NoResultException ex) {
            return routes;
        }
        return routes;
    }
    
    @Override
    public List<FlightRouteEntity> retrieveAllAvailableRoutesNotReturn() {
        Query query = entityManager.createQuery("SELECT r FROM FlightRouteEntity r WHERE r.disabled = FALSE AND "
                + "r.departureFlightRoute IS NULL ORDER BY r.originAirport.airportCode ASC");
        List<FlightRouteEntity> routes = new ArrayList<>();
        try {
            routes = query.getResultList();
        } catch (NoResultException ex) {
            return routes;
        }
        return routes;
    }
    
    @Override
    public List<FlightRouteEntity> retrieveAllRoutesNotReturn() {
        Query query = entityManager.createQuery("SELECT r FROM FlightRouteEntity r WHERE r.departureFlightRoute IS NULL"
                + " ORDER BY r.originAirport.airportCode ASC ");
        List<FlightRouteEntity> routes = new ArrayList<>();
        try {
            routes = query.getResultList();
        } catch (NoResultException ex) {
            return routes;
        }
        return routes;
    }

    @Override
    public List<FlightEntity> retrieveAllFlights(FlightRouteEntity route) {
        route = entityManager.find(FlightRouteEntity.class, route.getRouteId());
        route.getFlights().size();
        return route.getFlights();
    }

    @Override
    public FlightRouteEntity retrieveRouteByAirport(AirportEntity originAirport, AirportEntity destinationAirport) throws FlightRouteNotFoundException {
        Query query = entityManager.createQuery("SELECT r FROM FlightRouteEntity r WHERE r.originAirport = :inOriginAirport AND "
                + "r.destinationAirport = :inDestinationAirport");
        query.setParameter("inOriginAirport", originAirport);
        query.setParameter("inDestinationAirport", destinationAirport);

        try {
            return (FlightRouteEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new FlightRouteNotFoundException("Flight Route " + originAirport.getAirportCode()
                    + " - " + destinationAirport.getAirportCode() + " does not exist!");
        }
    }
    
    @Override
    public FlightRouteEntity retrieveRouteById(Long newRouteId) throws FlightRouteNotFoundException {
        Query query = entityManager.createQuery("SELECT r FROM FlightRouteEntity r WHERE r.routeId = :inId");
        query.setParameter("inId", newRouteId);

        try {
            return (FlightRouteEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new FlightRouteNotFoundException("Flight Route with ID " + newRouteId + " does not exist!");
        }
    }

    @Override
    public void deleteRoute(FlightRouteEntity route) {
        route = entityManager.find(FlightRouteEntity.class, route.getRouteId());
        AirportEntity originAirport = route.getOriginAirport();
        originAirport.getDepartureRoutes().size();
        List<FlightRouteEntity> departureRoutes = originAirport.getDepartureRoutes();

        AirportEntity destinationAirport = route.getDestinationAirport();
        originAirport.getArrivalRoutes().size();
        List<FlightRouteEntity> arrivalRoutes = destinationAirport.getArrivalRoutes();

        departureRoutes.remove(route);
        arrivalRoutes.remove(route);

        originAirport.setDepartureRoutes(departureRoutes);
        destinationAirport.setArrivalRoutes(arrivalRoutes);
        entityManager.remove(route);
    }

    @Override
    public void disable(FlightRouteEntity route) {
        entityManager.find(FlightRouteEntity.class, route.getRouteId());
        route.setDisabled(true);
    }

    @Override
    public boolean isDisabled(FlightRouteEntity route) throws FlightRouteDisabled {
        if (route.isDisabled()) {
            throw new FlightRouteDisabled("This flight route is disabled!");
        }
        return false;
    }

    @Override
    public boolean checkReturnRouteAvailability(FlightRouteEntity route) {
        route = entityManager.find(FlightRouteEntity.class, route.getRouteId());
        if (route.getReturnFlightRoute() != null) {
            return true;
        }

        return false;
    }
    
    @Override
    public boolean isReturnRoute(FlightRouteEntity route) {
        route = entityManager.find(FlightRouteEntity.class, route.getRouteId());
        if (route.getDepartureFlightRoute() != null) {
            return true;
        }
        return false;
    }
}
