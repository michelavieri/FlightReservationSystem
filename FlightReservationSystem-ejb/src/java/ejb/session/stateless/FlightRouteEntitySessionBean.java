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
    public List<FlightEntity> retrieveAllFlights(FlightRouteEntity route) {
        route.getFlights().size();
        return route.getFlights();
    }
    
    @Override
    public FlightRouteEntity findFlightRoute(AirportEntity origin, AirportEntity destination) throws FlightRouteNotFoundException {
        Query query = entityManager.createQuery("SELECT r from FlightRouteEntity r WHERE r.originAirport = :inOrigin AND r.destinationAirport = :inDestination");
        query.setParameter("inOrigin", origin);
        query.setParameter("inDestination", destination);
        
        try
        {
            return (FlightRouteEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new FlightRouteNotFoundException("Flight route from " + origin + " to " + destination + " does not exist!");
        }
    }

    @Override
    public boolean isDisabled(FlightRouteEntity route) throws FlightRouteDisabled {
        if(route.isDisabled()) {
            throw new FlightRouteDisabled("This flight route is disabled!");
        }
        return false;
    }
    
    @Override
    public boolean checkReturnRouteAvailability(FlightRouteEntity route) {
        if(route.getReturnFlightRoute() != null || route.getDepartureFlightRoute() != null) {
            return true;
        }
        
        return false;
    }
}
