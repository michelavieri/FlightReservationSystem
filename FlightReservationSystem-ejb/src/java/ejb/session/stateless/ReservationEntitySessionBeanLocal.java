/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import entity.ReservationEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.InvalidReservationId;
import util.exception.NotMyReservationException;

/**
 *
 * @author Chrisya
 */
@Local
public interface ReservationEntitySessionBeanLocal {

    public List<ReservationEntity> retrieveFlightReservationsByCustomer(CustomerEntity cust);

    public ReservationEntity retrieveReservationByReservationId(long reservationId, CustomerEntity customer) throws InvalidReservationId, NotMyReservationException;

    public ReservationEntity createNewReservation(CustomerEntity customer, ReservationEntity newReservation);

}
