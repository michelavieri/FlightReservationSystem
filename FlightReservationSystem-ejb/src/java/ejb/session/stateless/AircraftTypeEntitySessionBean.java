/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftTypeEntity;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.AircraftTypeNameExistException;
import util.exception.AircraftTypeNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Chrisya
 */
@Stateless
public class AircraftTypeEntitySessionBean implements AircraftTypeEntitySessionBeanRemote, AircraftTypeEntitySessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;

    public AircraftTypeEntitySessionBean() {
    }

    @Override
    public Long createNewAircraftType(AircraftTypeEntity newAircraftTypeEntity) throws AircraftTypeNameExistException, UnknownPersistenceException {
        try {
            entityManager.persist(newAircraftTypeEntity);
            entityManager.flush();

            return newAircraftTypeEntity.getId();
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new AircraftTypeNameExistException();
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }

    @Override
    public AircraftTypeEntity retrieveAircraftTypeByName(String name) throws AircraftTypeNotFoundException {
        Query query = entityManager.createQuery("SELECT a FROM AircraftTypeEntity a WHERE a.name = :inUsername");
        query.setParameter("inUsername", name);

        try {
            return (AircraftTypeEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new AircraftTypeNotFoundException("Aircraft Type " + name + " does not exist!");
        }
    }

    @Override
    public List<AircraftTypeEntity> retrieveAllTypes() {
        Query query = entityManager.createQuery("SELECT a FROM AircraftTypeEntity a");
        List<AircraftTypeEntity> types = new ArrayList<AircraftTypeEntity>();
        try {
            types = query.getResultList();
        } catch (NoResultException ex) {
            return types;
        }
        return types;
    }
    
    
}
