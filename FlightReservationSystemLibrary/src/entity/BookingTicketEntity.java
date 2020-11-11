/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import util.enumeration.FlightTypeEnum;

/**
 *
 * @author miche
 */
@Entity
public class BookingTicketEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingTicketId;

    @ManyToOne
    private ReservationEntity reservationEntity;

    @ManyToOne
    private PassengerEntity passenger;
    
    private String fareBasisCode;

    @OneToOne
    private SeatEntity seat;
    
    @ManyToOne
    private FareEntity fare;
    
    @ManyToOne
    private FlightScheduleEntity flightSchedule;
    
    private FlightTypeEnum flightType; 

    public Long getBookingTicketId() {
        return bookingTicketId;
    }

    public void setBookingTicketId(Long bookingTicketId) {
        this.bookingTicketId = bookingTicketId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bookingTicketId != null ? bookingTicketId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the bookingTicketId fields are not set
        if (!(object instanceof BookingTicketEntity)) {
            return false;
        }
        BookingTicketEntity other = (BookingTicketEntity) object;
        if ((this.bookingTicketId == null && other.bookingTicketId != null) || (this.bookingTicketId != null && !this.bookingTicketId.equals(other.bookingTicketId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.BookingTicketEntity[ id=" + bookingTicketId + " ]";
    }

    /**
     * @return the reservationEntity
     */
    public ReservationEntity getReservationEntity() {
        return reservationEntity;
    }

    /**
     * @param reservationEntity the reservationEntity to set
     */
    public void setReservationEntity(ReservationEntity reservationEntity) {
        this.reservationEntity = reservationEntity;
    }

    /**
     * @return the passenger
     */
    public PassengerEntity getPassenger() {
        return passenger;
    }

    /**
     * @param passenger the passenger to set
     */
    public void setPassenger(PassengerEntity passenger) {
        this.passenger = passenger;
    }

    /**
     * @return the seat
     */
    public SeatEntity getSeat() {
        return seat;
    }

    /**
     * @param seat the seat to set
     */
    public void setSeat(SeatEntity seat) {
        this.seat = seat;
    }

    /**
     * @return the flightSchedule
     */
    public FlightScheduleEntity getFlightSchedule() {
        return flightSchedule;
    }

    /**
     * @param flightSchedule the flightSchedule to set
     */
    public void setFlightSchedule(FlightScheduleEntity flightSchedule) {
        this.flightSchedule = flightSchedule;
    }

    /**
     * @return the fareBasisCode
     */
    public String getFareBasisCode() {
        return fareBasisCode;
    }

    /**
     * @param fareBasisCode the fareBasisCode to set
     */
    public void setFareBasisCode(String fareBasisCode) {
        this.fareBasisCode = fareBasisCode;
    }

    /**
     * @return the fare
     */
    public FareEntity getFare() {
        return fare;
    }

    /**
     * @param fare the fare to set
     */
    public void setFare(FareEntity fare) {
        this.fare = fare;
    }

    /**
     * @return the flightType
     */
    public FlightTypeEnum getFlightType() {
        return flightType;
    }

    /**
     * @param flightType the flightType to set
     */
    public void setFlightType(FlightTypeEnum flightType) {
        this.flightType = flightType;
    }

}
