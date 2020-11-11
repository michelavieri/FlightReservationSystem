
package entity;

import java.io.Serializable;
import java.util.List;
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
    private Long fairId;
    private String fareBasisCode;
    private String amount;
    
    @ManyToOne
    private FlightSchedulePlanEntity flightSchedulePlan;
    
    @ManyToOne
    private CabinClassConfigurationEntity cabinClass;
    @OneToMany(mappedBy = "fare")
    private List<BookingTicketEntity> bookingTicketEntitys;

    
    public FareEntity() {
    }
    
 
    public Long getFairId() {
        return fairId;
    }

    public void setFairId(Long fairId) {
        this.fairId = fairId;
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
        hash += (fairId != null ? fairId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the fairId fields are not set
        if (!(object instanceof FareEntity)) {
            return false;
        }
        FareEntity other = (FareEntity) object;
        if ((this.fairId == null && other.fairId != null) || (this.fairId != null && !this.fairId.equals(other.fairId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FareEntity[ id=" + fairId + " ]";
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
