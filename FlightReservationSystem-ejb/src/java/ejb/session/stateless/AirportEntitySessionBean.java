/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AirportEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.AirportNameExistException;
import util.exception.AirportNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Chrisya
 */
@Stateless
public class AirportEntitySessionBean implements AirportEntitySessionBeanRemote, AirportEntitySessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;

    public AirportEntitySessionBean() {
    }

    @Override
    public String createNewAirport(AirportEntity newAirportEntity) throws AirportNameExistException, UnknownPersistenceException
    {
        try
        {
            entityManager.persist(newAirportEntity);
            entityManager.flush();

            return newAirportEntity.getAirportCode();
        }
        catch(PersistenceException ex)
        {
            if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
            {
                if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                {
                    throw new AirportNameExistException();
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
    public AirportEntity retrieveAirportByName(String name) throws AirportNotFoundException
    {
        Query query = entityManager.createQuery("SELECT a FROM AirportEntity a WHERE a.airportName = :inName");
        query.setParameter("inName", name);
        
        try
        {
            return (AirportEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new AirportNotFoundException("Airport name " + name + " does not exist!");
        }
    }
}
