 
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


@Entity
public class AircraftConfigurationEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String name;
    private int numCabinClass;
    
    
    @ManyToOne
    private AircraftTypeEntity type;
    
    @OneToMany(mappedBy = "aircraftConfig")
    private List<CabinClassConfigurationEntity> cabinClassConfigurationEntitys;
    
    @OneToMany(mappedBy = "aircraftConfigurationEntity")
    private List<FlightEntity> flightEntitys;

    
    public AircraftConfigurationEntity() {
    }

    public AircraftConfigurationEntity(String code, String name, 
            int numCabinClass, AircraftTypeEntity type, 
            List<CabinClassConfigurationEntity> cabinClassConfigurationEntitys) {
        this.code = code;
        this.name = name;
        this.numCabinClass = numCabinClass;
        this.type = type;
        this.cabinClassConfigurationEntitys = cabinClassConfigurationEntitys;
        this.flightEntitys = new ArrayList<>();
    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumCabinClass() {
        return numCabinClass;
    }

    public void setNumCabinClass(int numCabinClass) {
        this.numCabinClass = numCabinClass;
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
        if (!(object instanceof AircraftConfigurationEntity)) {
            return false;
        }
        AircraftConfigurationEntity other = (AircraftConfigurationEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AircraftConfigurationEntity[ id=" + id + " ]";
    }

    /**
     * @return the type
     */
    public AircraftTypeEntity getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(AircraftTypeEntity type) {
        this.type = type;
    }

    /**
     * @return the cabinClassConfigurationEntitys
     */
    public List<CabinClassConfigurationEntity> getCabinClassConfigurationEntitys() {
        return cabinClassConfigurationEntitys;
    }

    /**
     * @param cabinClassConfigurationEntitys the cabinClassConfigurationEntitys to set
     */
    public void setCabinClassConfigurationEntitys(List<CabinClassConfigurationEntity> cabinClassConfigurationEntitys) {
        this.cabinClassConfigurationEntitys = cabinClassConfigurationEntitys;
    }

    /**
     * @return the flightEntitys
     */
    public List<FlightEntity> getFlightEntitys() {
        return flightEntitys;
    }

    /**
     * @param flightEntitys the flightEntitys to set
     */
    public void setFlightEntitys(List<FlightEntity> flightEntitys) {
        this.flightEntitys = flightEntitys;
    }
    
}
