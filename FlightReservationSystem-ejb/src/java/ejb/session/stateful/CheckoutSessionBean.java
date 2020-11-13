/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import ejb.session.stateless.FareEntitySessionBeanLocal;
import ejb.session.stateless.ReservationEntitySessionBeanLocal;
import entity.BookingTicketEntity;
import entity.CustomerEntity;
import entity.ReservationEntity;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateful;

/**
 *
 * @author Chrisya
 */
@Stateful
public class CheckoutSessionBean implements CheckoutSessionBeanRemote, CheckoutSessionBeanLocal {

    @EJB
    private FareEntitySessionBeanLocal fareEntitySessionBean;

    @EJB
    private ReservationEntitySessionBeanLocal reservationEntitySessionBean;

    List<BookingTicketEntity> bookings;
    int numOfPassenger;
    long totalAmount;

    public CheckoutSessionBean() {
        initialiseState();
    }

    @Remove
    @Override
    public void remove() {
    }

    private void initialiseState() {
        bookings = new ArrayList<>();
        numOfPassenger = 0;
        totalAmount = 0L;
    }

    @Override
    public void addBoooking(BookingTicketEntity ticket) {
        bookings.add(ticket);

        totalAmount += Long.parseLong(fareEntitySessionBean.retrieveFareAmount(ticket.getFare()));
    }

    @Override
    public ReservationEntity doCheckout(CustomerEntity customer) {
        // ReservationEntity newReservation = reservationEntitySessionBean.createNewReservation(customer, new ReservationEntity(numOfPassenger, Long.toString(totalAmount), bookings));

//        return newReservation;
        return null;
    }

    @Override
    public void clearCart() {
        initialiseState();
    }
}
