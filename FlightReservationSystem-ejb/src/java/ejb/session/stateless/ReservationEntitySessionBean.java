/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

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
public class ReservationEntitySessionBean implements ReservationEntitySessionBeanRemote, ReservationEntitySessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;

    public ReservationEntitySessionBean() {

    }

    @Override
    public List<ReservationEntity> retrieveReservationsByScheduleIdFirstClass(Long id) {
        List<ReservationEntity> reservations = new ArrayList<>();
        Query query = entityManager.createQuery("SELECT r FROM ReservationEntity r WHERE r.cabinClass := inClass ORDER BY r.seatNumber ASC");
        query.setParameter("inClass", CabinClassTypeEnum.FIRST_CLASS);
        try {
            reservations = query.getResultList();
        } catch (NoResultException ex) {
            return reservations;
        }
        return reservations;
    }
    
    @Override
    public List<ReservationEntity> retrieveReservationsByScheduleIdBusinessClass(Long id) {
        List<ReservationEntity> reservations = new ArrayList<>();
        Query query = entityManager.createQuery("SELECT r FROM ReservationEntity r WHERE r.cabinClass := inClass ORDER BY r.seatNumber ASC");
        query.setParameter("inClass", CabinClassTypeEnum.BUSINESS_CLASS);
        try {
            reservations = query.getResultList();
        } catch (NoResultException ex) {
            return reservations;
        }
        return reservations;
    }
    
    @Override
    public List<ReservationEntity> retrieveReservationsByScheduleIdPremiumClass(Long id) {
        List<ReservationEntity> reservations = new ArrayList<>();
        Query query = entityManager.createQuery("SELECT r FROM ReservationEntity r WHERE r.cabinClass := inClass ORDER BY r.seatNumber ASC");
        query.setParameter("inClass", CabinClassTypeEnum.PREMIUM_ECONOMY_CLASS);
        try {
            reservations = query.getResultList();
        } catch (NoResultException ex) {
            return reservations;
        }
        return reservations;
    }

    @Override
    public List<ReservationEntity> retrieveReservationsByScheduleIdEconomyClass(Long id) {
        List<ReservationEntity> reservations = new ArrayList<>();
        Query query = entityManager.createQuery("SELECT r FROM ReservationEntity r WHERE r.cabinClass := inClass ORDER BY r.seatNumber ASC");
        query.setParameter("inClass", CabinClassTypeEnum.ECONOMY_CLASS);
        try {
            reservations = query.getResultList();
        } catch (NoResultException ex) {
            return reservations;
        }
        return reservations;
    }
}
