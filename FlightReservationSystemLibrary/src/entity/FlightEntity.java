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
public class FlightEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightId;
    private String flightCode;
    private boolean disabled;
    
    @ManyToOne
    private AircraftConfigurationEntity aircraftConfigurationEntity;

    @OneToMany(mappedBy = "flight")
    private List<FlightSchedulePlanEntity> flightSchedulePlans;
    
    @ManyToOne
    private FlightRouteEntity route;

    @OneToOne(mappedBy = "departureFlight")
    private FlightEntity returnFlight;
    
    @OneToOne
    private FlightEntity departureFlight;
    

    public FlightEntity() {
    }

    public FlightEntity(int flightNumber, AircraftConfigurationEntity aircraftConfigurationEntity, FlightRouteEntity route) {
        this.flightCode = "ML " + flightNumber;
        this.aircraftConfigurationEntity = aircraftConfigurationEntity;
        this.route = route;
    }

    
    public AircraftConfigurationEntity getAircraftConfigurationEntity() {
        return aircraftConfigurationEntity;
    }

    
    public void setAircraftConfigurationEntity(AircraftConfigurationEntity aircraftConfigurationEntity) {
        this.aircraftConfigurationEntity = aircraftConfigurationEntity;
    }

    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }
    
    public String getFlightCode() {
        return flightCode;
    }

    public void setFlightCode(String flightCode) {
        this.flightCode = flightCode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flightCode != null ? flightCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the flightCode fields are not set
        if (!(object instanceof FlightEntity)) {
            return false;
        }
        FlightEntity other = (FlightEntity) object;
        if ((this.flightCode == null && other.flightCode != null) || (this.flightCode != null && !this.flightCode.equals(other.flightCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FlightEntity[ id=" + flightCode + " ]";
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
     * @return the returnFlight
     */
    public FlightEntity getReturnFlight() {
        return returnFlight;
    }

    /**
     * @param returnFlight the returnFlight to set
     */
    public void setReturnFlight(FlightEntity returnFlight) {
        this.returnFlight = returnFlight;
    }

    /**
     * @return the departureFlight
     */
    public FlightEntity getDepartureFlight() {
        return departureFlight;
    }

    /**
     * @param departureFlight the departureFlight to set
     */
    public void setDepartureFlight(FlightEntity departureFlight) {
        this.departureFlight = departureFlight;
    }

    /**
     * @return the route
     */
    public FlightRouteEntity getRoute() {
        return route;
    }

    /**
     * @param route the route to set
     */
    public void setRoute(FlightRouteEntity route) {
        this.route = route;
    }

    /**
     * @return the flightSchedulePlans
     */
    public List<FlightSchedulePlanEntity> getFlightSchedulePlans() {
        return flightSchedulePlans;
    }

    /**
     * @param flightSchedulePlans the flightSchedulePlans to set
     */
    public void setFlightSchedulePlans(List<FlightSchedulePlanEntity> flightSchedulePlans) {
        this.flightSchedulePlans = flightSchedulePlans;
    }

}
