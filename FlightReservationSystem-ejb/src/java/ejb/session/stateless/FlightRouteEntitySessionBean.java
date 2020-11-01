/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FlightEntity;
import entity.FlightRouteEntity;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
    
    

}
