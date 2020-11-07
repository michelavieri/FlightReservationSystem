/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FlightEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.FlightNotFoundException;

/**
 *
 * @author miche
 */
@Local
public interface FlightEntitySessionBeanLocal {

    public FlightEntity createFlightEntity(FlightEntity newFlight);

    public List<FlightEntity> retrieveAllFlights();

    public void setReturnFlight(FlightEntity departureFlight, FlightEntity returnFlight);

    public void setDepartureFlight(FlightEntity returnFlight, FlightEntity departureFlight);

    public FlightEntity retrieveFlightByCode(String code) throws FlightNotFoundException;
    
}
