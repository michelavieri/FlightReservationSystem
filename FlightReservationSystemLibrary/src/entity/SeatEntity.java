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

/**
 *
 * @author miche
 */
@Entity
public class SeatEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatId;
    private String seatNumber;
    private boolean booked;
    
    @ManyToOne
    private SeatsInventoryEntity seatsInventory;

    @OneToOne(mappedBy = "seat")
    private BookingTicketEntity bookingTicketEntity;

    public SeatEntity(String seatNumber, boolean booked) {
        this.seatNumber = seatNumber;
        this.booked = booked;
    }

    public SeatEntity() {
    }

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (seatId != null ? seatId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the seatId fields are not set
        if (!(object instanceof SeatEntity)) {
            return false;
        }
        SeatEntity other = (SeatEntity) object;
        if ((this.seatId == null && other.seatId != null) || (this.seatId != null && !this.seatId.equals(other.seatId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.SeatEntity[ id=" + seatId + " ]";
    }

    /**
     * @return the bookingTicketEntity
     */
    public BookingTicketEntity getBookingTicketEntity() {
        return bookingTicketEntity;
    }

    /**
     * @param bookingTicketEntity the bookingTicketEntity to set
     */
    public void setBookingTicketEntity(BookingTicketEntity bookingTicketEntity) {
        this.bookingTicketEntity = bookingTicketEntity;
    }

    /**
     * @return the seatsInventory
     */
    public SeatsInventoryEntity getSeatsInventory() {
        return seatsInventory;
    }

    /**
     * @param seatsInventory the seatsInventory to set
     */
    public void setSeatsInventory(SeatsInventoryEntity seatsInventory) {
        this.seatsInventory = seatsInventory;
    }

    /**
     * @return the seatNumber
     */
    public String getSeatNumber() {
        return seatNumber;
    }

    /**
     * @param seatNumber the seatNumber to set
     */
    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    /**
     * @return the booked
     */
    public boolean isBooked() {
        return booked;
    }

    /**
     * @param booked the booked to set
     */
    public void setBooked(boolean booked) {
        this.booked = booked;
    }

}
