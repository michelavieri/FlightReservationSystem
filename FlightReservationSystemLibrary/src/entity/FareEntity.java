
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


@Entity
public class FareEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fareId;
    private String fareBasisCode;
    private String amount;
    
    @ManyToOne(cascade=CascadeType.DETACH)
    private FlightSchedulePlanEntity flightSchedulePlan;
    
    @ManyToOne(cascade=CascadeType.DETACH)
    private CabinClassConfigurationEntity cabinClass;
    @OneToMany(mappedBy = "fare", cascade=CascadeType.DETACH)
    private List<BookingTicketEntity> bookingTicketEntitys;

    
    public FareEntity() {
    }

    public FareEntity(String fareBasisCode, String amount) {
        this.fareBasisCode = fareBasisCode;
        this.amount = amount;
    }
    
 
    public Long getFareId() {
        return fareId;
    }

    public void setFareId(Long fareId) {
        this.fareId = fareId;
    }

    public String getFareBasisCode() {
        return fareBasisCode;
    }

    public void setFareBasisCode(String fareBasisCode) {
        this.fareBasisCode = fareBasisCode;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fareId != null ? fareId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the fareId fields are not set
        if (!(object instanceof FareEntity)) {
            return false;
        }
        FareEntity other = (FareEntity) object;
        if ((this.fareId == null && other.fareId != null) || (this.fareId != null && !this.fareId.equals(other.fareId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FareEntity[ id=" + fareId + " ]";
    }

    /**
     * @return the flightSchedulePlan
     */
    public FlightSchedulePlanEntity getFlightSchedulePlan() {
        return flightSchedulePlan;
    }

    /**
     * @param flightSchedulePlan the flightSchedulePlan to set
     */
    public void setFlightSchedulePlan(FlightSchedulePlanEntity flightSchedulePlan) {
        this.flightSchedulePlan = flightSchedulePlan;
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
