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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author miche
 */
@Entity
public class FlightScheduleEntity implements Serializable {

    @OneToMany(mappedBy = "flightSchedule")
    private List<SeatsInventoryEntity> seatsInventoryEntitys;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long scheduleId;
    private String departureTime;
    private Date departureDate;
    private int duration;
    private String arrTime;
    private Date arrivalDate;
    
    @OneToOne(mappedBy = "departureSchedule")
    private FlightScheduleEntity returnSchedule;
    @OneToOne
    private FlightScheduleEntity departureSchedule;
    
    @ManyToOne
    private FlightSchedulePlanEntity plan;

    @ManyToMany(mappedBy = "flightSchedules")
    private List<ReservationsEntity> reservations;
    
    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (scheduleId != null ? scheduleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the scheduleId fields are not set
        if (!(object instanceof FlightScheduleEntity)) {
            return false;
        }
        FlightScheduleEntity other = (FlightScheduleEntity) object;
        if ((this.scheduleId == null && other.scheduleId != null) || (this.scheduleId != null && !this.scheduleId.equals(other.scheduleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FlightScheduleEntity[ id=" + scheduleId + " ]";
    }

    /**
     * @return the departureTime
     */
    public String getDepartureTime() {
        return departureTime;
    }

    /**
     * @param departureTime the departureTime to set
     */
    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    /**
     * @return the departureDate
     */
    public Date getDepartureDate() {
        return departureDate;
    }

    /**
     * @param departureDate the departureDate to set
     */
    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    /**
     * @return the duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * @return the arrTime
     */
    public String getArrTime() {
        return arrTime;
    }

    /**
     * @param arrTime the arrTime to set
     */
    public void setArrTime(String arrTime) {
        this.arrTime = arrTime;
    }

    /**
     * @return the arrivalDate
     */
    public Date getArrivalDate() {
        return arrivalDate;
    }

    /**
     * @param arrivalDate the arrivalDate to set
     */
    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    /**
     * @return the returnSchedule
     */
    public FlightScheduleEntity getReturnSchedule() {
        return returnSchedule;
    }

    /**
     * @param returnSchedule the returnSchedule to set
     */
    public void setReturnSchedule(FlightScheduleEntity returnSchedule) {
        this.returnSchedule = returnSchedule;
    }

    /**
     * @return the departureSchedule
     */
    public FlightScheduleEntity getDepartureSchedule() {
        return departureSchedule;
    }

    /**
     * @param departureSchedule the departureSchedule to set
     */
    public void setDepartureSchedule(FlightScheduleEntity departureSchedule) {
        this.departureSchedule = departureSchedule;
    }

    /**
     * @return the plan
     */
    public FlightSchedulePlanEntity getPlan() {
        return plan;
    }
    
}
