/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftConfigurationEntity;
import entity.AircraftTypeEntity;
import entity.CabinClassConfigurationEntity;
import entity.FlightEntity;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.AircraftConfigurationNotFoundException;

/**
 *
 * @author miche
 */
@Stateless
public class AircraftConfigurationEntitySessionBean implements AircraftConfigurationEntitySessionBeanRemote, aircraftConfigurationEntitySessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;

    public AircraftConfigurationEntitySessionBean() {
    }

    @Override
    public AircraftConfigurationEntity createAircraftConfiguration(AircraftConfigurationEntity newAircraftConfig) {
        entityManager.persist(newAircraftConfig);
        newAircraftConfig.getCabinClassConfigurationEntitys().size();
        List<CabinClassConfigurationEntity> classes = newAircraftConfig.getCabinClassConfigurationEntitys();
        AircraftTypeEntity type = newAircraftConfig.getType();
        type = entityManager.find(AircraftTypeEntity.class, type.getId());

        type.getAircraftConfigurationEntitys().size();
        List<AircraftConfigurationEntity> configs = type.getAircraftConfigurationEntitys();
        configs.add(newAircraftConfig);
        type.setAircraftConfigurationEntitys(configs);

        for (CabinClassConfigurationEntity c : classes) {
            c.setAircraftConfig(newAircraftConfig);
            entityManager.persist(c);
        }
        entityManager.flush();

        return newAircraftConfig;
    }

    @Override
    public List<AircraftConfigurationEntity> retrieveAllAircraftConfigurations() {
        List<AircraftConfigurationEntity> aircrafts = new ArrayList<AircraftConfigurationEntity>();
        Query query = entityManager.createQuery("SELECT a FROM AircraftConfigurationEntity a ORDER BY a.type ASC, a.name ASC");
        try {
            aircrafts = query.getResultList();
        } catch (NoResultException ex) {
            return aircrafts;
        }
        for (AircraftConfigurationEntity aircraft : aircrafts) {
            aircraft.getCabinClassConfigurationEntitys().size();
            aircraft.getFlightEntitys().size();
        }
        return aircrafts;
    }

    @Override
    public AircraftConfigurationEntity retrieveAircraftTypeByCode(String code) throws AircraftConfigurationNotFoundException {
        Query query = entityManager.createQuery("SELECT a FROM AircraftConfigurationEntity a WHERE a.code = :inCode");
        query.setParameter("inCode", code);

        try {
            return (AircraftConfigurationEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new AircraftConfigurationNotFoundException("Aircraft Configuration " + code + " does not exist!");
        }
    }

    @Override
    public List<FlightEntity> getFlightEntities(AircraftConfigurationEntity aircraftConfig) {
        aircraftConfig = entityManager.find(AircraftConfigurationEntity.class, aircraftConfig.getId());
        aircraftConfig.getFlightEntitys().size();
        List<FlightEntity> flights = aircraftConfig.getFlightEntitys();
        return flights;
    }

    @Override
    public List<CabinClassConfigurationEntity> getCabinClassConfig(AircraftConfigurationEntity aircraftConfig) {
        aircraftConfig = entityManager.find(AircraftConfigurationEntity.class, aircraftConfig.getId());
        aircraftConfig.getCabinClassConfigurationEntitys().size();
        List<CabinClassConfigurationEntity> classes = aircraftConfig.getCabinClassConfigurationEntitys();
        return classes;
    }

}
