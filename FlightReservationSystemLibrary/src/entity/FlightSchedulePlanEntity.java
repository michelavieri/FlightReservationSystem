/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import util.enumeration.FlightSchedulePlanTypeEnum;

/**
 *
 * @author miche
 */
@Entity
public class FlightSchedulePlanEntity implements Serializable {

    /**
     * @return the fareEntitys
     */
    public List<FareEntity> getFareEntitys() {
        return fareEntitys;
    }

    /**
     * @param fareEntitys the fareEntitys to set
     */
    public void setFareEntitys(List<FareEntity> fareEntitys) {
        this.fareEntitys = fareEntitys;
    }

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long schedulePlanId;
    private FlightSchedulePlanTypeEnum type;
    private String startDate;
    private String endDate;
    private boolean disabled;
    private int layoverDuration;
    
    @OneToOne(mappedBy = "departureSchedulePlan", cascade = CascadeType.REMOVE)
    private FlightSchedulePlanEntity returnSchedulePlan;
    
    @OneToOne (cascade = CascadeType.REMOVE)
    private FlightSchedulePlanEntity departureSchedulePlan;

    @OneToMany(mappedBy = "flightSchedulePlan")
    private List<FareEntity> fareEntitys;

    @OneToMany(mappedBy = "plan")
    private List<FlightScheduleEntity> flightSchedules;

    @ManyToOne
    private FlightEntity flight;

    public FlightSchedulePlanEntity() {
    }

    public FlightSchedulePlanEntity(FlightSchedulePlanTypeEnum type, String startDate, int layoverDuration, FlightEntity flight) {
        this.type = type;
        this.startDate = startDate;
        this.layoverDuration = layoverDuration;
        this.flight = flight;
        this.disabled = false;
    }

    public FlightSchedulePlanEntity(FlightSchedulePlanTypeEnum type, String startDate, String endDate, int layoverDuration, FlightEntity flight) {
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.layoverDuration = layoverDuration;
        this.flight = flight;
        this.disabled = false;
    }

    public Long getSchedulePlanId() {
        return schedulePlanId;
    }

    public void setSchedulePlanId(Long schedulePlanId) {
        this.schedulePlanId = schedulePlanId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (schedulePlanId != null ? schedulePlanId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the schedulePlanId fields are not set
        if (!(object instanceof FlightSchedulePlanEntity)) {
            return false;
        }
        FlightSchedulePlanEntity other = (FlightSchedulePlanEntity) object;
        if ((this.schedulePlanId == null && other.schedulePlanId != null) || (this.schedulePlanId != null && !this.schedulePlanId.equals(other.schedulePlanId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FlightSchedulePlanEntity[ id=" + schedulePlanId + " ]";
    }

    /**
     * @return the type
     */
    public FlightSchedulePlanTypeEnum getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(FlightSchedulePlanTypeEnum type) {
        this.type = type;
    }

    /**
     * @return the startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
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
     * @return the layoverDuration
     */
    public int getLayoverDuration() {
        return layoverDuration;
    }

    /**
     * @param layoverDuration the layoverDuration to set
     */
    public void setLayoverDuration(int layoverDuration) {
        this.layoverDuration = layoverDuration;
    }

    /**
     * @return the flightSchedules
     */
    public List<FlightScheduleEntity> getFlightSchedules() {
        return flightSchedules;
    }

    /**
     * @param flightSchedules the flightSchedules to set
     */
    public void setFlightSchedules(List<FlightScheduleEntity> flightSchedules) {
        this.flightSchedules = flightSchedules;
    }

    /**
     * @return the flight
     */
    public FlightEntity getFlight() {
        return flight;
    }

    /**
     * @param flight the flight to set
     */
    public void setFlight(FlightEntity flight) {
        this.flight = flight;
    }

}
