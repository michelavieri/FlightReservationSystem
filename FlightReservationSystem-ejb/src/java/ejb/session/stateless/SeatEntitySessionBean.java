/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FlightScheduleEntity;
import entity.SeatEntity;
import entity.SeatsInventoryEntity;
import java.util.HashSet;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.CabinClassTypeEnum;
import util.exception.InvalidClassException;

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
    public SeatEntity retrieveSeat(int seatNumber, String seatLetter, long seatsInventoryId) {
        Query query = entityManager.createQuery("SELECT s FROM SeatEntity s WHERE s.seatNumber = :seatNum AND s.seatLetter = :seatLetter"
                + " AND s.seatsInventory.inventoryId = :inventoryId");
        query.setParameter("seatNum", seatNumber);
        query.setParameter("seatLetter", seatLetter);
        query.setParameter("inventoryId", seatsInventoryId);
        return (SeatEntity) query.getSingleResult();
    }

    @Override
    public SeatEntity randomAvailableSeat(long scheduleId, CabinClassTypeEnum classType, HashSet<SeatEntity> bookedSeats) throws InvalidClassException {
        FlightScheduleEntity schedule = entityManager.find(FlightScheduleEntity.class, scheduleId);

        schedule.getSeatsInventoryEntitys().size();

        List<SeatsInventoryEntity> seatsInventories = schedule.getSeatsInventoryEntitys();
        SeatsInventoryEntity currentInventory = null;
        for (SeatsInventoryEntity seatsInventory : seatsInventories) {
            if (seatsInventory.getCabinClass().getType().equals(classType)) {
                currentInventory = seatsInventory;
                break;
            }
        }

        if (currentInventory == null) {
            throw new InvalidClassException("This flight does not have the class type preference you picked!");
        }
        
        currentInventory = entityManager.find(SeatsInventoryEntity.class, currentInventory.getInventoryId());
        currentInventory.getSeats().size();
        List<SeatEntity> seats = currentInventory.getSeats();
        for (SeatEntity seat : seats) {
            if (!seat.isBooked() && !bookedSeats.contains(seat)) {
                SeatEntity pickedSeat = entityManager.find(SeatEntity.class, seat.getSeatId());
                return pickedSeat;
            }
        }
        throw new InvalidClassException("All of the seats for this class has been booked!!!");
    }
}
