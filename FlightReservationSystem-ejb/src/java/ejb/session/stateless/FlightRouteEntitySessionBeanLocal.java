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
import javax.ejb.Local;
import util.exception.FlightRouteNotFoundException;

/**
 *
 * @author miche
 */
@Local
public interface FlightRouteEntitySessionBeanLocal {

    FlightRouteEntity createFlightRouteEntity(FlightRouteEntity newRoute);

    void setReturnFlightRoute(FlightRouteEntity departureFlightRoute, FlightRouteEntity returnFlightRoute);

    public void setDepartureFlightRoute(FlightRouteEntity departureFlightRoute, FlightRouteEntity returnFlightRoute);

    List<FlightRouteEntity> retrieveAllRoutes();

    List<FlightEntity> retrieveAllFlights(FlightRouteEntity route);

    public FlightRouteEntity retrieveRouteByAirport(AirportEntity originAirport, AirportEntity destinationAirport) throws FlightRouteNotFoundException;

    void deleteRoute(FlightRouteEntity route);

    void disable(FlightRouteEntity route);
}