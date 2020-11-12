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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author Chrisya
 */
@Entity
public class FlightScheduleEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;
    
    private String departureDateTime;
    private int duration;
    private String arrivalDateTime;

    @OneToMany(mappedBy = "flightSchedule")
    private List<SeatsInventoryEntity> seatsInventoryEntitys = new ArrayList<>();

    @OneToOne(mappedBy = "departureSchedule")
    private FlightScheduleEntity returnSchedule;

    @OneToOne
    private FlightScheduleEntity departureSchedule;

    @ManyToOne
    private FlightSchedulePlanEntity plan;
    
    @OneToMany(mappedBy = "flightSchedule")
    private List<BookingTicketEntity> bookingTicketEntitys = new ArrayList<>();

    public FlightScheduleEntity() {
    }

    public FlightScheduleEntity(String departureDateTime, int duration, String arrivalDateTime) {
        this.departureDateTime = departureDateTime;
        this.duration = duration;
        this.arrivalDateTime = arrivalDateTime;
    }

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
     * @return the departureDate
     */
    public String getDepartureDateTime() {
        return departureDateTime;
    }

    /**
     * @param departureDate the departureDate to set
     */
    public void setDepartureDateTime(String departureDateTime) {
        this.departureDateTime = departureDateTime;
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
     * @return the arrivalDate
     */
    public String getArrivalDateTime() {
        return arrivalDateTime;
    }

    /**
     * @param arrivalDate the arrivalDate to set
     */
    public void setArrivalDate(String arrivalDateTime) {
        this.arrivalDateTime = arrivalDateTime;
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

    /**
     * @param plan the plan to set
     */
    public void setPlan(FlightSchedulePlanEntity plan) {
        this.plan = plan;
    }

    /**
     * @return the seatsInventoryEntitys
     */
    public List<SeatsInventoryEntity> getSeatsInventoryEntitys() {
        return seatsInventoryEntitys;
    }

    /**
     * @param seatsInventoryEntitys the seatsInventoryEntitys to set
     */
    public void setSeatsInventoryEntitys(List<SeatsInventoryEntity> seatsInventoryEntitys) {
        this.seatsInventoryEntitys = seatsInventoryEntitys;
    }

    /**
     * @return the bookingTicketEntitys
     */
    public List<BookingTicketEntity> getBookingTicketEntitys() {
        return bookingTicketEntitys;
    }

    /**
     * @param bookingTicketEntitys the bookingTicketEntitys to set
     */
    public void setBookingTicketEntitys(List<BookingTicketEntity> bookingTicketEntitys) {
        this.bookingTicketEntitys = bookingTicketEntitys;
    }
}
