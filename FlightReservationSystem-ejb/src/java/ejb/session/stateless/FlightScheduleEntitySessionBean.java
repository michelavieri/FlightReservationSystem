/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FlightScheduleEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Chrisya
 */
@Stateless
public class FlightScheduleEntitySessionBean implements FlightScheduleEntitySessionBeanRemote, FlightScheduleEntitySessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;

    public FlightScheduleEntitySessionBean() {
    }

    @Override
    public FlightScheduleEntity createFlightScheduleEntity(FlightScheduleEntity newFlightSchedule) {
        entityManager.persist(newFlightSchedule);
        entityManager.flush();

        return newFlightSchedule;
    }
    
}
