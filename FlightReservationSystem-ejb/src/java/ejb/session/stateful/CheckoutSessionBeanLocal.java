/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import entity.BookingTicketEntity;
import entity.CustomerEntity;
import entity.ReservationEntity;
import javax.ejb.Local;

/**
 *
 * @author Chrisya
 */
@Local
public interface CheckoutSessionBeanLocal {

    public void remove();

    public void addBoooking(BookingTicketEntity ticket);

//    public ReservationEntity doCheckout(CustomerEntity customer);

    public void clearCart();
    
}
