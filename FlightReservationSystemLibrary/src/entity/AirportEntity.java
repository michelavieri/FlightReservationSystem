/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author miche
 */
@Entity
public class AirportEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long airportId;
    private String airportCode;
    private String airportName;
    private String airportCity;
    private String airportState;
    private String airportCountry;
    private String timeZone;

    @OneToMany(mappedBy = "destinationAirport")
    private List<FlightRouteEntity> arrivalRoutes;

    @OneToMany(mappedBy = "originAirport")
    private List<FlightRouteEntity> departureRoutes;

    public AirportEntity() {
    }

    public AirportEntity(String airportCode, String airportName, String airportCity, String airportState, String airportCountry, String timeZone) {
        this.airportName = airportName;
        this.airportCity = airportCity;
        this.airportCode = airportCode;
        this.airportState = airportState;
        this.airportCountry = airportCountry;
        this.timeZone = timeZone;
        this.arrivalRoutes = new ArrayList<>();
        this.departureRoutes = new ArrayList<>();
    }

    public Long getAirportId() {
        return airportId;
    }

    public void setAirportId(Long airportId) {
        this.airportId = airportId;
    }

    public String getAirportCode() {
        return airportCode;
    }

    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (airportCode != null ? airportCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the airportCode fields are not set
        if (!(object instanceof AirportEntity)) {
            return false;
        }
        AirportEntity other = (AirportEntity) object;
        if ((this.airportCode == null && other.airportCode != null) || (this.airportCode != null && !this.airportCode.equals(other.airportCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AirportEntity[ id=" + airportCode + " ]";
    }

    /**
     * @return the airportName
     */
    public String getAirportName() {
        return airportName;
    }

    /**
     * @param airportName the airportName to set
     */
    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    /**
     * @return the airportCity
     */
    public String getAirportCity() {
        return airportCity;
    }

    /**
     * @param airportCity the airportCity to set
     */
    public void setAirportCity(String airportCity) {
        this.airportCity = airportCity;
    }

    /**
     * @return the airportState
     */
    public String getAirportState() {
        return airportState;
    }

    /**
     * @param airportState the airportState to set
     */
    public void setAirportState(String airportState) {
        this.airportState = airportState;
    }

    /**
     * @return the airportCountry
     */
    public String getAirportCountry() {
        return airportCountry;
    }

    /**
     * @param airportCountry the airportCountry to set
     */
    public void setAirportCountry(String airportCountry) {
        this.airportCountry = airportCountry;
    }

    /**
     * @return the timeZone
     */
    public String getTimeZone() {
        return timeZone;
    }

    /**
     * @param timeZone the timeZone to set
     */
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    /**
     * @return the arrivalRoutes
     */
    public List<FlightRouteEntity> getArrivalRoutes() {
        return arrivalRoutes;
    }

    /**
     * @param arrivalRoutes the arrivalRoutes to set
     */
    public void setArrivalRoutes(List<FlightRouteEntity> arrivalRoutes) {
        this.arrivalRoutes = arrivalRoutes;
    }

    /**
     * @return the departureRoutes
     */
    public List<FlightRouteEntity> getDepartureRoutes() {
        return departureRoutes;
    }

    /**
     * @param departureRoutes the departureRoutes to set
     */
    public void setDepartureRoutes(List<FlightRouteEntity> departureRoutes) {
        this.departureRoutes = departureRoutes;
    }

}
