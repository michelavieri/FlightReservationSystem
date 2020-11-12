/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.SeatEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Chrisya
 */
@Stateless
public class SeatEntitySessionBean implements SeatEntitySessionBeanRemote, SeatEntitySessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;

    public SeatEntitySessionBean() {
    }

    public SeatEntity createNewSeat(SeatEntity newSeat) {
        entityManager.persist(newSeat);
        entityManager.flush();

        return newSeat;
    }
}
