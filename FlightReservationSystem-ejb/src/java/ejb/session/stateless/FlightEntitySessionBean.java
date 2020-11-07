/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AirportEntity;
import entity.CabinClassConfigurationEntity;
import entity.FlightEntity;
import entity.FlightRouteEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.FlightNotFoundException;

/**
 *
 * @author miche
 */
@Stateless
public class FlightEntitySessionBean implements FlightEntitySessionBeanRemote, FlightEntitySessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;

    public FlightEntitySessionBean() {
    }

    @Override
    public FlightEntity createFlightEntity(FlightEntity newFlight) {
        entityManager.persist(newFlight);
        entityManager.flush();

        return newFlight;
    }

    @Override
    public List<FlightEntity> retrieveAllFlights() {
        
        Query query = entityManager.createQuery("SELECT f FROM FlightEntity f");
        List<FlightEntity> flights = query.getResultList();
        
        for(FlightEntity flight:flights) {
            flight.getFlightSchedulePlans().size();
            flight.getReturnFlight();
        }
        
        return flights;
    }
    
    @Override
    public FlightEntity retrieveFlightByCode(String code) throws FlightNotFoundException{
        
        Query query = entityManager.createQuery("SELECT f FROM FlightEntity f WHERE f.flightCode = :inCode");
        query.setParameter("inCode", code);
        
        try
        {
            return (FlightEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
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
}
