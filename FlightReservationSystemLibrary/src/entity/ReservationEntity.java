/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
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
public class ReservationEntity implements Serializable {

    @OneToOne(mappedBy = "reservation", cascade=CascadeType.DETACH)
    private CreditCardEntity creditCardEntity;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;
    private int numOfPassengers;
    private String totalAmount;

    public ReservationEntity(int numOfPassengers, String totalAmount, List<BookingTicketEntity> tickets) {
        this.numOfPassengers = numOfPassengers;
        this.totalAmount = totalAmount;
        this.tickets = tickets;
    }

    public ReservationEntity() {
    }

    @OneToMany(mappedBy = "reservationEntity", cascade=CascadeType.DETACH)
    private List<BookingTicketEntity> tickets;
    
    @ManyToOne(cascade=CascadeType.DETACH)
    private CustomerEntity customer;
    
    @ManyToOne(cascade=CascadeType.DETACH)
    private PartnerEntity partner;

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservationId != null ? reservationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the reservationId fields are not set
        if (!(object instanceof ReservationEntity)) {
            return false;
        }
        ReservationEntity other = (ReservationEntity) object;
        if ((this.reservationId == null && other.reservationId != null) || (this.reservationId != null && !this.reservationId.equals(other.reservationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ReservationEntity[ id=" + reservationId + " ]";
    }

    /**
     * @return the numOfPassengers
     */
    public int getNumOfPassengers() {
        return numOfPassengers;
    }

    /**
     * @param numOfPassengers the numOfPassengers to set
     */
    public void setNumOfPassengers(int numOfPassengers) {
        this.numOfPassengers = numOfPassengers;
    }

    /**
     * @return the totalAmount
     */
    public String getTotalAmount() {
        return totalAmount;
    }

    /**
     * @param totalAmount the totalAmount to set
     */
    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * @return the tickets
     */
    public List<BookingTicketEntity> getTickets() {
        return tickets;
    }

    /**
     * @param tickets the tickets to set
     */
    public void setTickets(List<BookingTicketEntity> tickets) {
        this.tickets = tickets;
    }

    /**
     * @return the creditCardEntity
     */
    public CreditCardEntity getCreditCardEntity() {
        return creditCardEntity;
    }

    /**
     * @param creditCardEntity the creditCardEntity to set
     */
    public void setCreditCardEntity(CreditCardEntity creditCardEntity) {
        this.creditCardEntity = creditCardEntity;
    }

    /**
     * @return the customer
     */
    public CustomerEntity getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    /**
     * @return the partner
     */
    public PartnerEntity getPartner() {
        return partner;
    }

    /**
     * @param partner the partner to set
     */
    public void setPartner(PartnerEntity partner) {
        this.partner = partner;
    }

}
