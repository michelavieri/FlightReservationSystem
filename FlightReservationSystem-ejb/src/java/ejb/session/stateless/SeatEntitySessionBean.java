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
import javax.persistence.Query;

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

    @Override
    public SeatEntity createNewSeat(SeatEntity newSeat) {
        entityManager.persist(newSeat);
        entityManager.flush();

        return newSeat;
    }
    
    @Override
    public SeatEntity retrieveSeat(int seatNumber, String seatLetter) {
        Query query = entityManager.createQuery("SELECT s FROM SeatEntity s WHERE s.seatNumber = :seatNum AND s.seatLetter = :seatLetter");
        query.setParameter("seatNum", seatNumber);
        query.setParameter("seatLetter", seatLetter);
        
        return (SeatEntity) query.getSingleResult();
    }
}
