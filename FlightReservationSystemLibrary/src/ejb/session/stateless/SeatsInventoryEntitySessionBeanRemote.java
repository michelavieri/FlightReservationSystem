/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.SeatsInventoryEntity;
import java.util.List;
import javax.ejb.Remote;
import util.exception.FlightScheduleNotFoundException;

/**
 *
 * @author Chrisya
 */
@Remote
public interface SeatsInventoryEntitySessionBeanRemote {
    
    public SeatsInventoryEntity createSeatsInventoryEntity(SeatsInventoryEntity newSeatsInventory);
    
     public List<SeatsInventoryEntity> retrieveSeatsInventoryByScheduleId(Long scheduleId) throws FlightScheduleNotFoundException;
    
}
