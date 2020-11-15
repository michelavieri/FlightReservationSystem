/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PartnerEntity;
import entity.ReservationEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.InvalidUsernameException;
import util.exception.PartnerNotFoundException;
import util.exception.PartnerUsernameExistException;
import util.exception.UnknownPersistenceException;
import util.exception.WrongPasswordException;

/**
 *
 * @author Chrisya
 */
@Stateless
public class PartnerEntitySessionBean implements PartnerEntitySessionBeanRemote, PartnerEntitySessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;

    public PartnerEntitySessionBean() {
    }

    @Override
    public String createNewPartner(PartnerEntity newPartnerEntity) throws PartnerUsernameExistException, UnknownPersistenceException
    {
        try
        {
            entityManager.persist(newPartnerEntity);
            entityManager.flush();

            return newPartnerEntity.getUsername();
        }
        catch(PersistenceException ex)
        {
            if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
            {
                if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                {
                    throw new PartnerUsernameExistException();
                }
                else
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
            else
            {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }

     @Override
    public PartnerEntity retrievePartnerByUsername(String username) throws PartnerNotFoundException
    {
        Query query = entityManager.createQuery("SELECT p FROM PartnerEntity p WHERE p.username = :inUsername");
        query.setParameter("inUsername", username);
        
        try
        {
            return (PartnerEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new PartnerNotFoundException("Partner Username " + username + " does not exist!");
        }
    }
    
    @Override
    public PartnerEntity partnerLogin(String username, String password) throws InvalidUsernameException, WrongPasswordException {
        try {
            PartnerEntity partner = this.retrievePartnerByUsername(username);
            partner = entityManager.find(PartnerEntity.class, partner.getPartnerId());

            if (partner.getPassword().equals(password)) {
                return partner;
            } else {
                throw new WrongPasswordException("Invalid password!");
            }
        } catch (PartnerNotFoundException ex) {
            throw new InvalidUsernameException(ex.getMessage());
        }
    }

    @Override
    public PartnerEntity partnerLoginUnmanaged(String email, String password) throws InvalidUsernameException, WrongPasswordException {

        try {
            PartnerEntity partner = partnerLogin(email, password);
            entityManager.detach(partner);
            
            partner.getReservationsEntitys().size();
            List<ReservationEntity> reservations = partner.getReservationsEntitys();
            
            for(ReservationEntity reservation:reservations) {
                entityManager.detach(reservation);
            }

            return partner;
        } catch (InvalidUsernameException ex) {
            throw new InvalidUsernameException(ex.getMessage());
        } catch (WrongPasswordException ex) {
            throw new WrongPasswordException(ex.getMessage());
        }
    }

}
