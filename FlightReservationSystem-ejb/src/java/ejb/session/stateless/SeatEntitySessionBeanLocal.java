/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.SeatEntity;
import java.util.HashSet;
import javax.ejb.Local;
import util.enumeration.CabinClassTypeEnum;
import util.exception.InvalidClassException;

/**
 *
 * @author Chrisya
 */
@Local
public interface SeatEntitySessionBeanLocal {

    public SeatEntity createNewSeat(SeatEntity newSeat);

    public SeatEntity retrieveSeat(int seatNumber, String seatLetter, long seatsInventoryId);

    public SeatEntity randomAvailableSeat(long scheduleId, CabinClassTypeEnum classType, HashSet<SeatEntity> bookedSeats) throws InvalidClassException;

}
