/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CabinClassConfigurationEntity;
import entity.FareEntity;
import entity.FlightSchedulePlanEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Chrisya
 */
@Stateless
public class FareEntitySessionBean implements FareEntitySessionBeanRemote, FareEntitySessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;

    public FareEntitySessionBean() {
    }

    @Override
    public FareEntity createFlightSchedulePlanEntity(FareEntity newFare) {
        entityManager.persist(newFare);
        entityManager.flush();

        return newFare;
    }
    
    @Override
    public void associateFareWithCabinClass(CabinClassConfigurationEntity cabinClass, FareEntity fare) {
        cabinClass = entityManager.find(CabinClassConfigurationEntity.class, cabinClass.getId());
        fare = entityManager.find(FareEntity.class, fare.getFairId());
        
        cabinClass.getFareEntitys().add(fare);
        fare.setCabinClass(cabinClass);
    }
    
    @Override
    public void associateFareWithPlan(FlightSchedulePlanEntity plan, FareEntity fare) {
        plan = entityManager.find(FlightSchedulePlanEntity.class, plan.getSchedulePlanId());
        fare = entityManager.find(FareEntity.class, fare.getFairId());
        
        plan.getFareEntitys().add(fare);
        fare.setFlightSchedulePlan(plan);
    }
    
    @Override
    public String retrieveFareAmount(FareEntity fare) {
        fare = entityManager.find(FareEntity.class, fare.getFairId());
        
        return fare.getAmount();
    }
}
