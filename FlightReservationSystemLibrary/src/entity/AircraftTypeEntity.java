
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity
public class AircraftTypeEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int maxCapacity;
    
    @OneToMany(mappedBy = "type", cascade=CascadeType.DETACH)
    private List<AircraftConfigurationEntity> aircraftConfigurationEntitys;

    
    public AircraftTypeEntity() {
    }

    public AircraftTypeEntity(String name, int maxCapacity) {
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.aircraftConfigurationEntitys = new ArrayList<>();
    }

    public List<AircraftConfigurationEntity> getAircraftConfigurationEntitys() {
        return aircraftConfigurationEntitys;
    }

    public void setAircraftConfigurationEntitys(List<AircraftConfigurationEntity> aircraftConfigurationEntitys) {
        this.aircraftConfigurationEntitys = aircraftConfigurationEntitys;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        if (!(object instanceof AircraftTypeEntity)) {
            return false;
        }
        AircraftTypeEntity other = (AircraftTypeEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AircraftTypeEntity[ id=" + id + " ]";
    }
    
}
