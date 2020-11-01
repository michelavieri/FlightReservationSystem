/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import util.enumeration.FlightSchedulePlanTypeEnum;

/**
 *
 * @author miche
 */
@Entity
public class FlightSchedulePlanEntity implements Serializable {

    @OneToMany(mappedBy = "flightSchedulePlan")
    private List<FareEntity> fareEntitys;

    @OneToMany(mappedBy = "plan")
    private List<FlightScheduleEntity> flightSchedules;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long schedulePlanId;
    private FlightSchedulePlanTypeEnum type;
    private Date startDate;
    private Date endDate;
    private boolean disabled;
    private int layoverDuration;
    
    @ManyToOne
    private FlightEntity flight;
    

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
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
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
