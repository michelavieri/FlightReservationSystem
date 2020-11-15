/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditCardEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author miche
 */
@Stateless
public class CreditCardEntitySessionBean implements CreditCardEntitySessionBeanRemote, CreditCardEntitySessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public CreditCardEntity createCreditCard(CreditCardEntity card) {
        em.persist(card);
        em.flush();
        return card;
    }
    
    @Override
    public CreditCardEntity createCreditCardUnmanaged(CreditCardEntity card) {
        em.persist(card);
        em.flush();
        em.detach(card);
        if (card.getReservation() != null) {
            em.detach(card.getReservation());
        }
        return card;
    }
}
