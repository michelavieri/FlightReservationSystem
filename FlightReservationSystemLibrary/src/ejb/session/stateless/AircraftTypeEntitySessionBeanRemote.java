/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftTypeEntity;
import javax.ejb.Remote;
import util.exception.AircraftTypeNameExistException;
import util.exception.AircraftTypeNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Chrisya
 */
@Remote
public interface AircraftTypeEntitySessionBeanRemote {
     public Long createNewAircraftType(AircraftTypeEntity newAircraftTypeEntity) throws AircraftTypeNameExistException, UnknownPersistenceException;

    public AircraftTypeEntity retrieveAircraftTypeByName(String name) throws AircraftTypeNotFoundException;
}
