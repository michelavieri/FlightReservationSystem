/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FlightScheduleEntity;
import entity.FlightSchedulePlanEntity;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
}
