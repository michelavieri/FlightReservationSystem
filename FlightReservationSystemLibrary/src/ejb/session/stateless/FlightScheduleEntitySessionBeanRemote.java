/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FlightScheduleEntity;
import javax.ejb.Remote;

/**
 *
 * @author Chrisya
 */
@Remote
public interface FlightScheduleEntitySessionBeanRemote {
    
     public FlightScheduleEntity createFlightScheduleEntity(FlightScheduleEntity newFlightSchedule);
    
}
