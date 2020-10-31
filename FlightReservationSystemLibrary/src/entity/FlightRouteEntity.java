/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author miche
 */
@Entity
public class FlightRouteEntity implements Serializable {

    @OneToMany(mappedBy = "route")
    private List<FlightEntity> flights;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long routeId;
    private boolean disabled;
    @OneToOne(mappedBy = "departureFlightRoute")
    private FlightRouteEntity returnFlightRoute;
    @OneToOne
    private FlightRouteEntity departureFlightRoute;
    
    @ManyToOne
    private AirportEntity departureAirport;
    
    @ManyToOne
    private AirportEntity arrivalAirport;

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (routeId != null ? routeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the routeId fields are not set
        if (!(object instanceof FlightRouteEntity)) {
            return false;
        }
        FlightRouteEntity other = (FlightRouteEntity) object;
        if ((this.routeId == null && other.routeId != null) || (this.routeId != null && !this.routeId.equals(other.routeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FlightRouteEntity[ id=" + routeId + " ]";
    }

    /**
     * @return the disabled
     */
    public boolean isDisabled() {
        return disabled;
    }

    /**
     * @param disabled the disabled to set
     */
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    /**
     * @return the returnFlightRoute
     */
    public FlightRouteEntity getReturnFlightRoute() {
        return returnFlightRoute;
    }

    /**
     * @param returnFlightRoute the returnFlightRoute to set
     */
    public void setReturnFlightRoute(FlightRouteEntity returnFlightRoute) {
        this.returnFlightRoute = returnFlightRoute;
    }

    /**
     * @return the departureFlightRoute
     */
    public FlightRouteEntity getDepartureFlightRoute() {
        return departureFlightRoute;
    }

    /**
     * @param departureFlightRoute the departureFlightRoute to set
     */
    public void setDepartureFlightRoute(FlightRouteEntity departureFlightRoute) {
        this.departureFlightRoute = departureFlightRoute;
    }

    /**
     * @return the flights
     */
    public List<FlightEntity> getFlights() {
        return flights;
    }

    /**
     * @param flights the flights to set
     */
    public void setFlights(List<FlightEntity> flights) {
        this.flights = flights;
    }

    /**
     * @return the departureAirport
     */
    public AirportEntity getDepartureAirport() {
        return departureAirport;
    }

    /**
     * @param departureAirport the departureAirport to set
     */
    public void setDepartureAirport(AirportEntity departureAirport) {
        this.departureAirport = departureAirport;
    }

    /**
     * @return the arrivalAirport
     */
    public AirportEntity getArrivalAirport() {
        return arrivalAirport;
    }

    /**
     * @param arrivalAirport the arrivalAirport to set
     */
    public void setArrivalAirport(AirportEntity arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }
    
}
