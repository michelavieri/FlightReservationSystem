/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftConfigurationEntity;
import entity.CabinClassConfigurationEntity;
import entity.FlightEntity;
import entity.FlightRouteEntity;
import entity.FlightSchedulePlanEntity;
import java.util.List;
import javax.ejb.Remote;
import util.exception.FlightNotFoundException;

/**
 *
 * @author miche
 */
@Remote
public interface FlightEntitySessionBeanRemote {

    public FlightEntity createFlightEntity(FlightEntity newFlight);
    
    public List<FlightSchedulePlanEntity> retrieveSchedulePlans (FlightEntity flight);

    public List<FlightEntity> retrieveAllFlights();
   
    public void setReturnFlight(FlightEntity departureFlight, FlightEntity returnFlight);

    public void setDepartureFlight(FlightEntity returnFlight, FlightEntity departureFlight);
    
    public boolean hasReturnFlight(FlightEntity flight);
    
    public FlightEntity retrieveFlightByCode(String code) throws FlightNotFoundException;
    
    public List<CabinClassConfigurationEntity> retrieveCabinClassByFlight(FlightEntity flight);
    
    public String retrieveTimeZoneByFlight(FlightEntity flight);
    
    public void deleteFlight(FlightEntity flight);
    
    public void updateAircraftConfiguration(AircraftConfigurationEntity newConfig, FlightEntity flight);
    
    public void updateFlightRoute(FlightRouteEntity newRoute, FlightEntity flight);
    
    public boolean isReturnFlight(FlightEntity flight);

    public void removeReturnFlight(FlightEntity flight);
    
}
