/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CabinClassConfigurationEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author miche
 */
@Stateless
public class CabinClassConfigurationSessionBean implements CabinClassConfigurationSessionBeanRemote, CabinClassConfigurationSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;

    public CabinClassConfigurationSessionBean() {
    }

    @Override
    public CabinClassConfigurationEntity createNewCabinClassConfiguration(CabinClassConfigurationEntity newCabin) {
        entityManager.persist(newCabin);
        entityManager.flush();
        
        return newCabin;
    }
    
    
}
