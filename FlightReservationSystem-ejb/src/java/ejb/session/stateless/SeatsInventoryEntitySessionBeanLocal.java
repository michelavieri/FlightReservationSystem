/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.SeatEntity;
import entity.SeatsInventoryEntity;
import java.util.List;
import javax.ejb.Local;
import util.enumeration.CabinClassTypeEnum;
import util.exception.FlightScheduleNotFoundException;
import util.exception.NoCabinClassException;

/**
 *
 * @author Chrisya
 */
@Local
public interface SeatsInventoryEntitySessionBeanLocal {

    public SeatsInventoryEntity createSeatsInventoryEntity(SeatsInventoryEntity newSeatsInventory);

    public List<SeatsInventoryEntity> retrieveSeatsInventoryByScheduleId(Long scheduleId) throws FlightScheduleNotFoundException;

    public List<SeatEntity> retrieveSeats(SeatsInventoryEntity seatsInventory);

    public int createSeatsFromSeatInventory(SeatsInventoryEntity seatsInventory, int startNumber);

    public SeatsInventoryEntity retrieveSeatsInventoryByScheduleIdClass(Long scheduleId, CabinClassTypeEnum cabinClass) throws NoCabinClassException, FlightScheduleNotFoundException;

    public SeatsInventoryEntity retrieveSeatsInventoryByScheduleIdClassUnmanaged(Long scheduleId, CabinClassTypeEnum cabinClass) throws NoCabinClassException, FlightScheduleNotFoundException;
}
