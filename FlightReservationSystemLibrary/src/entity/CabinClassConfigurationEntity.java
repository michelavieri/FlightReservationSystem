
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import util.enumeration.CabinClassTypeEnum;


@Entity
public class CabinClassConfigurationEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int numAisle;
    private int numRow;
    private int numSeatAbreast;
    private int maxCapacity;
    private CabinClassTypeEnum type;
    private int[] arrOfConfig;
    
    @ManyToOne
    private AircraftConfigurationEntity aircraftConfig;
    
    @OneToMany(mappedBy = "cabinClass")
    private List<FareEntity> fareEntitys;

    
    public CabinClassConfigurationEntity() {
    }

    public CabinClassConfigurationEntity(int numAisle, int numRow, 
            int numSeatAbreast, int maxCapacity, CabinClassTypeEnum type,
            int[] arrOfConfig) {
        this.numAisle = numAisle;
        this.numRow = numRow;
        this.numSeatAbreast = numSeatAbreast;
        this.maxCapacity = maxCapacity;
        this.type = type;
        this.arrOfConfig = arrOfConfig;
    }

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumAisle() {
        return numAisle;
    }

    public void setNumAisle(int numAisle) {
        this.numAisle = numAisle;
    }

    public int getNumRow() {
        return numRow;
    }

    public void setNumRow(int numRow) {
        this.numRow = numRow;
    }

    public int getNumSeatAbreast() {
        return numSeatAbreast;
    }

    public void setNumSeatAbreast(int numSeatAbreast) {
        this.numSeatAbreast = numSeatAbreast;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CabinClassConfigurationEntity)) {
            return false;
        }
        CabinClassConfigurationEntity other = (CabinClassConfigurationEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CabinClassConfigurationEntity[ id=" + id + " ]";
    }

    /**
     * @return the type
     */
    public CabinClassTypeEnum getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(CabinClassTypeEnum type) {
        this.type = type;
    }

    /**
     * @return the arrOfConfig
     */
    public int[] getArrOfConfig() {
        return arrOfConfig;
    }

    /**
     * @param arrOfConfig the arrOfConfig to set
     */
    public void setArrOfConfig(int[] arrOfConfig) {
        this.arrOfConfig = arrOfConfig;
    }

    /**
     * @return the aircraftConfig
     */
    public AircraftConfigurationEntity getAircraftConfig() {
        return aircraftConfig;
    }

    /**
     * @param aircraftConfig the aircraftConfig to set
     */
    public void setAircraftConfig(AircraftConfigurationEntity aircraftConfig) {
        this.aircraftConfig = aircraftConfig;
    }

    /**
     * @return the fareEntitys
     */
    public List<FareEntity> getFareEntitys() {
        return fareEntitys;
    }

    /**
     * @param fareEntitys the fareEntitys to set
     */
    public void setFareEntitys(List<FareEntity> fareEntitys) {
        this.fareEntitys = fareEntitys;
    }
    
}
