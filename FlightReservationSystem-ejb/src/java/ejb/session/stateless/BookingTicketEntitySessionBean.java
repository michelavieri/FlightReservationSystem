/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.BookingTicketEntity;
import entity.ReservationEntity;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.CabinClassTypeEnum;

/**
 *
 * @author miche
 */
@Stateless
public class BookingTicketEntitySessionBean implements BookingTicketEntitySessionBeanRemote, BookingTicketEntitySessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;

    @Override
    public List<BookingTicketEntity> retrieveTicketsByScheduleIdFirstClass(Long scheduleId) {
        List<BookingTicketEntity> tickets = new ArrayList<>();
        Query query = entityManager.createQuery("SELECT b FROM BookingTicketEntity b WHERE b.flightSchedule.scheduleId := inId"
                + " AND b.fare.cabinClass := inClass ORDER BY b.seat.seatNumber ASC");
        query.setParameter("inId", scheduleId);
        query.setParameter("inClass", CabinClassTypeEnum.FIRST_CLASS);
        try {
            tickets = query.getResultList();
        } catch (NoResultException ex) {
            return tickets;
        }
        return tickets;
    }

    @Override
    public List<BookingTicketEntity> retrieveTicketsByScheduleIdBusinessClass(Long scheduleId) {
        List<BookingTicketEntity> tickets = new ArrayList<>();
        Query query = entityManager.createQuery("SELECT b FROM BookingTicketEntity b WHERE b.flightSchedule.scheduleId := inId"
                + " AND b.fare.cabinClass := inClass ORDER BY b.seat.seatNumber ASC");
        query.setParameter("inId", scheduleId);
        query.setParameter("inClass", CabinClassTypeEnum.BUSINESS_CLASS);
        try {
            tickets = query.getResultList();
        } catch (NoResultException ex) {
            return tickets;
        }
        return tickets;
    }
    
    @Override
    public List<BookingTicketEntity> retrieveTicketsByScheduleIdPremiumEconomyClass(Long scheduleId) {
        List<BookingTicketEntity> tickets = new ArrayList<>();
        Query query = entityManager.createQuery("SELECT b FROM BookingTicketEntity b WHERE b.flightSchedule.scheduleId := inId"
                + " AND b.fare.cabinClass := inClass ORDER BY b.seat.seatNumber ASC");
        query.setParameter("inId", scheduleId);
        query.setParameter("inClass", CabinClassTypeEnum.PREMIUM_ECONOMY_CLASS);
        try {
            tickets = query.getResultList();
        } catch (NoResultException ex) {
            return tickets;
        }
        return tickets;
    }
    
    @Override
    public List<BookingTicketEntity> retrieveTicketsByScheduleIdEconomyClass(Long scheduleId) {
        List<BookingTicketEntity> tickets = new ArrayList<>();
        Query query = entityManager.createQuery("SELECT b FROM BookingTicketEntity b WHERE b.flightSchedule.scheduleId := inId"
                + " AND b.fare.cabinClass := inClass ORDER BY b.seat.seatNumber ASC");
        query.setParameter("inId", scheduleId);
        query.setParameter("inClass", CabinClassTypeEnum.ECONOMY_CLASS);
        try {
            tickets = query.getResultList();
        } catch (NoResultException ex) {
            return tickets;
        }
        return tickets;
    }
}
