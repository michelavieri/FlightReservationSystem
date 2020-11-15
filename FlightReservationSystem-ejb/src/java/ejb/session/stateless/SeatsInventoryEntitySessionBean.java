/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.BookingTicketEntity;
import entity.FlightScheduleEntity;
import entity.PassengerEntity;
import entity.SeatEntity;
import entity.SeatsInventoryEntity;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.CabinClassTypeEnum;
import util.exception.FlightScheduleNotFoundException;
import util.exception.NoCabinClassException;

/**
 *
 * @author Chrisya
 */
@Stateless
public class SeatsInventoryEntitySessionBean implements SeatsInventoryEntitySessionBeanRemote, SeatsInventoryEntitySessionBeanLocal {

    @EJB
    private SeatEntitySessionBeanLocal seatEntitySessionBean;

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

        try {
            schedule = (FlightScheduleEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new FlightScheduleNotFoundException("Flight Schedule id " + scheduleId + " does not exist!");
        }
        schedule.getSeatsInventoryEntitys().size();
        return schedule.getSeatsInventoryEntitys();

    }
    
    @Override
    public SeatsInventoryEntity retrieveSeatsInventoryByScheduleIdClass(Long scheduleId, CabinClassTypeEnum cabinClass) throws NoCabinClassException, FlightScheduleNotFoundException {

        Query query = entityManager.createQuery("SELECT f FROM FlightScheduleEntity f WHERE f.scheduleId = :inScheduleId");
        query.setParameter("inScheduleId", scheduleId);

        FlightScheduleEntity schedule = null;

        try {
            schedule = (FlightScheduleEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new FlightScheduleNotFoundException("Flight Schedule id " + scheduleId + " does not exist!");
        }
        schedule.getSeatsInventoryEntitys().size();
        List<SeatsInventoryEntity> seats = schedule.getSeatsInventoryEntitys();
        
        SeatsInventoryEntity seatsInventory = null;
        for (SeatsInventoryEntity seat: seats) {
            if (seat.getCabinClass().getType().equals(cabinClass)) {
                seatsInventory = seat;
            }
        }
        if (seatsInventory == null) {
            throw new NoCabinClassException("This cabin class is not offered in this flight!");
        }
        return seatsInventory;

    }

    @Override
    public int createSeatsFromSeatInventory(SeatsInventoryEntity seatsInventory, int startNumber) {
        seatsInventory = entityManager.find(SeatsInventoryEntity.class, seatsInventory.getInventoryId());
        seatsInventory.getSeats().size();

        int numAisle = seatsInventory.getCabinClass().getNumAisle();
        int numRow = seatsInventory.getCabinClass().getNumRow();
        int numAbreast = seatsInventory.getCabinClass().getNumSeatAbreast();
        int tempAbreast = 0;
        int endNumSeat = 0;

        int totalCapacity = seatsInventory.getCabinClass().getMaxCapacity();

        for (int i = startNumber + 1; i <= startNumber + numRow; i++) {
            char initial = 'A';
           for (int j = 0; j < numAbreast; j++) {

                SeatEntity seat = new SeatEntity(i, Character.toString(initial), false);
                seat = seatEntitySessionBean.createNewSeat(seat);
                seat.setSeatsInventory(seatsInventory);
                seatsInventory.getSeats().add(seat);

                initial++;
            }
            endNumSeat = i;
        }

        return endNumSeat;
    }
    
    @Override
    public List<SeatEntity> retrieveSeats (SeatsInventoryEntity seatsInventory) {
        seatsInventory = entityManager.find(SeatsInventoryEntity.class, seatsInventory.getInventoryId());
        seatsInventory.getSeats().size();
        return seatsInventory.getSeats();
    }
}
