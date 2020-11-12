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
import javax.persistence.OneToMany;

/**
 *
 * @author miche
 */
@Entity
public class PassengerEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long passengerId;
    private String passengerName;
    private String passportNumber;

    @OneToMany(mappedBy = "passenger")
    private List<BookingTicketEntity> bookingTicketEntitys;

    public PassengerEntity() {
    }

    public PassengerEntity(String passengerName, String passportNumber) {
        this.passengerName = passengerName;
        this.passportNumber = passportNumber;
    }

    public Long getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Long passengerId) {
        this.passengerId = passengerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (passengerId != null ? passengerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the passengerId fields are not set
        if (!(object instanceof PassengerEntity)) {
            return false;
        }
        PassengerEntity other = (PassengerEntity) object;
        if ((this.passengerId == null && other.passengerId != null) || (this.passengerId != null && !this.passengerId.equals(other.passengerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.PassengerEntity[ id=" + passengerId + " ]";
    }

    /**
     * @return the passengerName
     */
    public String getPassengerName() {
        return passengerName;
    }

    /**
     * @param passengerName the passengerName to set
     */
    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    /**
     * @return the passportNumber
     */
    public String getPassportNumber() {
        return passportNumber;
    }

    /**
     * @param passportNumber the passportNumber to set
     */
    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
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
