/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AirportEntity;
import javax.ejb.Local;
import util.exception.AirportNameExistException;
import util.exception.AirportNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Chrisya
 */
@Local
public interface AirportEntitySessionBeanLocal {

    public String createNewAirport(AirportEntity newAirportEntity) throws AirportNameExistException, UnknownPersistenceException;

    public AirportEntity retrieveAirportByName(String name) throws AirportNotFoundException;
    
}
