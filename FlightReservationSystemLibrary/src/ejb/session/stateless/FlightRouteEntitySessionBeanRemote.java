/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AirportEntity;
import entity.FlightEntity;
import entity.FlightRouteEntity;
import java.util.List;
import javax.ejb.Remote;
import util.exception.FlightRouteDisabled;
import util.exception.FlightRouteNotFoundException;

/**
 *
 * @author miche
 */
@Remote
public interface FlightRouteEntitySessionBeanRemote {

    FlightRouteEntity createFlightRouteEntity(FlightRouteEntity newRoute);

    public boolean isReturnRoute(FlightRouteEntity route);

    public List<FlightRouteEntity> retrieveAllAvailableRoutesNotReturn();

    public List<FlightRouteEntity> retrieveAllRoutesNotReturn();

    void setReturnFlightRoute(FlightRouteEntity departureFlightRoute, FlightRouteEntity returnFlightRoute);

    public void setDepartureFlightRoute(FlightRouteEntity departureFlightRoute, FlightRouteEntity returnFlightRoute);

    List<FlightRouteEntity> retrieveAllRoutes();

    List<FlightEntity> retrieveAllFlights(FlightRouteEntity route);

    public FlightRouteEntity retrieveRouteByAirport(AirportEntity originAirport, AirportEntity destinationAirport) throws FlightRouteNotFoundException;

    void deleteRoute(FlightRouteEntity route);

    void disable(FlightRouteEntity route);

    public boolean isDisabled(FlightRouteEntity route) throws FlightRouteDisabled;

    public boolean checkReturnRouteAvailability(FlightRouteEntity route);

    public List<FlightRouteEntity> retrieveAllAvailableRoutes();

    public FlightRouteEntity retrieveRouteById(Long newRouteId) throws FlightRouteNotFoundException;
}
