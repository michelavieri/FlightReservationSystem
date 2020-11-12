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
import javax.persistence.OneToOne;

/**
 *
 * @author miche
 */
@Entity
public class CreditCardEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;
    private String cardNumber;
    private String cardName;
    private String expiryDate;
    private String cvv;
    @OneToOne
    private ReservationEntity reservation;

    public CreditCardEntity() {
    }

    public CreditCardEntity(String cardNumber, String cardName, String expiryDate, String cvv) {
        this.cardNumber = cardNumber;
        this.cardName = cardName;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }
    
    

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cardId != null ? cardId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the cardId fields are not set
        if (!(object instanceof CreditCardEntity)) {
            return false;
        }
        CreditCardEntity other = (CreditCardEntity) object;
        if ((this.cardId == null && other.cardId != null) || (this.cardId != null && !this.cardId.equals(other.cardId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CreditCardEntity[ id=" + cardId + " ]";
    }

    /**
     * @return the reservation
     */
    public ReservationEntity getReservation() {
        return reservation;
    }

    /**
     * @param reservation the reservation to set
     */
    public void setReservation(ReservationEntity reservation) {
        this.reservation = reservation;
    }

    /**
     * @return the cardNumber
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * @param cardNumber the cardNumber to set
     */
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * @return the cardName
     */
    public String getCardName() {
        return cardName;
    }

    /**
     * @param cardName the cardName to set
     */
    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    /**
     * @return the expiryDate
     */
    public String getExpiryDate() {
        return expiryDate;
    }

    /**
     * @param expiryDate the expiryDate to set
     */
    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    /**
     * @return the cvv
     */
    public String getCvv() {
        return cvv;
    }

    /**
     * @param cvv the cvv to set
     */
    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
    
}
