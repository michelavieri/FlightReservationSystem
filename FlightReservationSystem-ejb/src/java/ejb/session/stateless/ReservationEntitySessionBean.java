/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.BookingTicketEntity;
import entity.CreditCardEntity;
import entity.CustomerEntity;
import entity.ReservationEntity;
import entity.SeatEntity;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.InvalidReservationId;
import util.exception.NotMyReservationException;

/**
 *
 * @author Chrisya
 */
@Stateless
public class ReservationEntitySessionBean implements ReservationEntitySessionBeanRemote, ReservationEntitySessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;

    public ReservationEntitySessionBean() {

    }

    @Override
    public ReservationEntity createNewReservation(CustomerEntity customer, CreditCardEntity card, ReservationEntity newReservation) {
        customer.getReservationsEntitys().add(newReservation);
        newReservation.setCustomer(customer);
        entityManager.persist(card);

        newReservation.getTickets().size();
        List<BookingTicketEntity> tickets = newReservation.getTickets();
        for (BookingTicketEntity ticket : tickets) {
            SeatEntity seat = ticket.getSeat();
            seat = entityManager.find(SeatEntity.class, seat.getSeatId());
            seat.setBooked(true);
            entityManager.persist(ticket);
        }
        entityManager.persist(newReservation);
        newReservation.setCreditCardEntity(card);
        card.setReservation(newReservation);

        entityManager.flush();

        return newReservation;
    }

    @Override
    public List<ReservationEntity> retrieveFlightReservationsByCustomer(CustomerEntity cust) {
        cust = entityManager.find(CustomerEntity.class, cust.getCustomerId());
        if (cust.getReservationsEntitys().isEmpty()) {
            return new ArrayList<>();
        }
        cust.getReservationsEntitys().size();
        List<ReservationEntity> reservations = cust.getReservationsEntitys();

        return reservations;
    }

    @Override
    public ReservationEntity retrieveReservationByReservationId(long reservationId, CustomerEntity customer) throws InvalidReservationId, NotMyReservationException {
        Query query = entityManager.createQuery("SELECT r FROM ReservationEntity r WHERE r.reservationId = :id");
        query.setParameter("id", reservationId);
        ReservationEntity reservation = null;
        try {
            reservation = (ReservationEntity) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new InvalidReservationId("The reservation Id is invalid!");
        }

        if (!reservation.getCustomer().equals(reservation)) {
            throw new NotMyReservationException("This reservation is not your reservation!");
        }
        return reservation;
    }
}
