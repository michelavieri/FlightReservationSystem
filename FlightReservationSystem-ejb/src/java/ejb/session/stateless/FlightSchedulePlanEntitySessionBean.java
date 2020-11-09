/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FlightEntity;
import entity.FlightScheduleEntity;
import entity.FlightSchedulePlanEntity;
import entity.SeatsInventoryEntity;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.ScheduleIsUsedException;

/**
 *
 * @author Chrisya
 */
@Stateless
public class FlightSchedulePlanEntitySessionBean implements FlightSchedulePlanEntitySessionBeanRemote, FlightSchedulePlanEntitySessionBeanLocal {

    @EJB
    private FlightScheduleEntitySessionBeanLocal flightScheduleEntitySessionBean;

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;

    public FlightSchedulePlanEntitySessionBean() {
    }

   @Override
    public FlightSchedulePlanEntity createFlightSchedulePlanEntity(FlightSchedulePlanEntity newFlightSchedulePlan) {
        entityManager.persist(newFlightSchedulePlan);
        entityManager.flush();

        return newFlightSchedulePlan;
    }
    
    @Override
    public void associatePlanWithFlight(FlightSchedulePlanEntity plan, FlightEntity flight) {
        plan = entityManager.find(FlightSchedulePlanEntity.class, plan.getSchedulePlanId());
        flight = entityManager.find(FlightEntity.class, flight.getFlightId());
        
        plan.setFlight(flight);
        flight.getFlightSchedulePlans().size();
        flight.getFlightSchedulePlans().add(plan);
    }
    
    @Override
    public void deleteSchedulePlan(long schedulePlanId) throws ScheduleIsUsedException {
        
        Query query = entityManager.createQuery("SELECT p FROM FlightSchedulePlanEntity p WHERE p.schedulePlanId = :inPlanId");
        query.setParameter("inPlanId", schedulePlanId);
        
        FlightSchedulePlanEntity plan = (FlightSchedulePlanEntity) query.getSingleResult();
        
        List<FlightScheduleEntity> flightSchedules = plan.getFlightSchedules();
        flightSchedules.size();
        
        for(FlightScheduleEntity schedule: flightSchedules) {
            if(schedule.getReservations().size() == 0) {
                throw new ScheduleIsUsedException();
            }
            
            List<SeatsInventoryEntity> seats = schedule.getSeatsInventoryEntitys();
            seats.size();
            for(SeatsInventoryEntity seat:seats) {
                seat.setFlightSchedule(null);
                schedule.getSeatsInventoryEntitys().remove(seat);
                entityManager.remove(seat);
            }
            
            schedule.setPlan(null);
            plan.getFlightSchedules().remove(schedule);
            entityManager.remove(schedule);
        }
    }
    
    @Override
    public FlightSchedulePlanEntity retrieveSchedulePlanById(long planId) {
        Query query = entityManager.createQuery("SELECT p FROM FlightSchedulePlanEntity p WHERE p.schedulePlanId = :inPlanId");
        query.setParameter("inPlanId", planId);
        
        FlightSchedulePlanEntity plan = (FlightSchedulePlanEntity) query.getSingleResult();
        
        return plan;
    }
    
    @Override
    public FlightScheduleEntity retrieveScheduleSinglePlan(FlightSchedulePlanEntity plan) {
        plan = entityManager.find(FlightSchedulePlanEntity.class, plan.getSchedulePlanId());
        
        List<FlightScheduleEntity> schedules = plan.getFlightSchedules();
        schedules.size();
        
        return schedules.get(0);
    }
    
    @Override
    public FlightEntity retrieveFlightFromPlan (FlightSchedulePlanEntity plan) {
        plan = entityManager.find(FlightSchedulePlanEntity.class, plan.getSchedulePlanId());
        
        FlightEntity flight = plan.getFlight();
        
        return flight;
    }
}
