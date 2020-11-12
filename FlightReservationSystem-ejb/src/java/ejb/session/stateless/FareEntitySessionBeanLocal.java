/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CabinClassConfigurationEntity;
import entity.FareEntity;
import entity.FlightSchedulePlanEntity;
import javax.ejb.Local;

/**
 *
 * @author Chrisya
 */
@Local
public interface FareEntitySessionBeanLocal {

    public FareEntity createFlightSchedulePlanEntity(FareEntity newFare);

    public void associateFareWithCabinClass(CabinClassConfigurationEntity cabinClass, FareEntity fare);

    public void associateFareWithPlan(FlightSchedulePlanEntity plan, FareEntity fare);

    public String retrieveFareAmount(String fareCode);
    
}
