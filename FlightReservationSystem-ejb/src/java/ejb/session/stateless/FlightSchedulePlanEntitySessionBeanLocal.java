/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FlightSchedulePlanEntity;
import javax.ejb.Local;

/**
 *
 * @author Chrisya
 */
@Local
public interface FlightSchedulePlanEntitySessionBeanLocal {

    public FlightSchedulePlanEntity createFlightSchedulePlanEntity(FlightSchedulePlanEntity newFlightSchedulePlan);
    
}
