/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FlightScheduleEntity;
import entity.SeatsInventoryEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.FlightScheduleNotFoundException;

/**
 *
 * @author Chrisya
 */
@Stateless
public class SeatsInventoryEntitySessionBean implements SeatsInventoryEntitySessionBeanRemote, SeatsInventoryEntitySessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;

    public SeatsInventoryEntitySessionBean() {
    }

    @Override
    public SeatsInventoryEntity createSeatsInventoryEntity(SeatsInventoryEntity newSeatsInventory) {
        entityManager.persist(newSeatsInventory);
        entityManager.flush();
        
        
        return newSeatsInventory;
    }
    
    @Override
    public List<SeatsInventoryEntity> retrieveSeatsInventoryByScheduleId(Long scheduleId) throws FlightScheduleNotFoundException {
        
        Query query = entityManager.createQuery("SELECT f FROM FlightScheduleEntity f WHERE f.scheduleId = :inScheduleId");
        query.setParameter("inScheduleId", scheduleId);
        
        FlightScheduleEntity schedule = null;
        
        try
        {
            schedule =  (FlightScheduleEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new FlightScheduleNotFoundException("Flight Schedule id " + scheduleId + " does not exist!");
        }
        
        return schedule.getSeatsInventoryEntitys();
        
    }
}
