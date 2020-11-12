/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CabinClassConfigurationEntity;
import entity.FareEntity;
import entity.FlightScheduleEntity;
import entity.FlightSchedulePlanEntity;
import entity.SeatsInventoryEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.CabinClassTypeEnum;

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
    
    @Override
    public FareEntity retrieveLowestFare(FlightScheduleEntity schedule, CabinClassTypeEnum type) {
        schedule = entityManager.find(FlightScheduleEntity.class, schedule.getScheduleId());
        
        List<SeatsInventoryEntity> seats = schedule.getSeatsInventoryEntitys();
        FareEntity lowestFare = null;
        
        for(SeatsInventoryEntity seat:seats) {
            if(seat.getCabinClass().getType().equals(type)) {
                List<FareEntity> fares = seat.getCabinClass().getFareEntitys();
                
                for(int i = 0; i < fares.size() - 1; i++) {
                    
                    if(Long.parseLong(fares.get(i).getAmount()) <= Long.parseLong(fares.get(i + 1).getAmount())) {
                        lowestFare = fares.get(i);
                    } else {
                        lowestFare = fares.get(i+1);
                    }
                }
            }
        }
        
        return lowestFare;
    }
}
