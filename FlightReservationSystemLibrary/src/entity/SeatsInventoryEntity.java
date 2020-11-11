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

/**
 *
 * @author miche
 */
@Entity
public class SeatsInventoryEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inventoryId;
    private int availableSeatsSize;
    private int reservedSeatsSize;
    private int balanceSeatsSize;
    @OneToMany(mappedBy = "seatsInventory")
    private List<SeatEntity> seats = new ArrayList<>();

    @ManyToOne
    private FlightScheduleEntity flightSchedule;

    @ManyToOne
    private CabinClassConfigurationEntity cabinClass;

    public SeatsInventoryEntity() {
    }

    public SeatsInventoryEntity(int availableSeatsSize, int reservedSeatsSize, int balanceSeatsSize) {
        this.availableSeatsSize = availableSeatsSize;
        this.reservedSeatsSize = reservedSeatsSize;
        this.balanceSeatsSize = balanceSeatsSize;
    }

    public Long getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Long inventoryId) {
        this.inventoryId = inventoryId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (inventoryId != null ? inventoryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the inventoryId fields are not set
        if (!(object instanceof SeatsInventoryEntity)) {
            return false;
        }
        SeatsInventoryEntity other = (SeatsInventoryEntity) object;
        if ((this.inventoryId == null && other.inventoryId != null) || (this.inventoryId != null && !this.inventoryId.equals(other.inventoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.SeatsInventoryEntity[ id=" + inventoryId + " ]";
    }

    /**
     * @return the availableSeatsSize
     */
    public int getAvailableSeatsSize() {
        return availableSeatsSize;
    }

    /**
     * @param availableSeatsSize the availableSeatsSize to set
     */
    public void setAvailableSeatsSize(int availableSeatsSize) {
        this.availableSeatsSize = availableSeatsSize;
    }

    /**
     * @return the reservedSeatsSize
     */
    public int getReservedSeatsSize() {
        return reservedSeatsSize;
    }

    /**
     * @param reservedSeatsSize the reservedSeatsSize to set
     */
    public void setReservedSeatsSize(int reservedSeatsSize) {
        this.reservedSeatsSize = reservedSeatsSize;
    }

    /**
     * @return the balanceSeatsSize
     */
    public int getBalanceSeatsSize() {
        return balanceSeatsSize;
    }

    /**
     * @param balanceSeatsSize the balanceSeatsSize to set
     */
    public void setBalanceSeatsSize(int balanceSeatsSize) {
        this.balanceSeatsSize = balanceSeatsSize;
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
     * @return the cabinClass
     */
    public CabinClassConfigurationEntity getCabinClass() {
        return cabinClass;
    }

    /**
     * @param cabinClass the cabinClass to set
     */
    public void setCabinClass(CabinClassConfigurationEntity cabinClass) {
        this.cabinClass = cabinClass;
    }

    /**
     * @return the seats
     */
    public List<SeatEntity> getSeats() {
        return seats;
    }

    /**
     * @param seats the seats to set
     */
    public void setSeats(List<SeatEntity> seats) {
        this.seats = seats;
    }

}
