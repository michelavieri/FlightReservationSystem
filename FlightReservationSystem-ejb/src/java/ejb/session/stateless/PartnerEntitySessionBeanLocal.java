/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PartnerEntity;
import javax.ejb.Local;
import util.exception.PartnerNotFoundException;
import util.exception.PartnerUsernameExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author miche
 */
@Local
public interface PartnerEntitySessionBeanLocal {

    public String createNewPartner(PartnerEntity newPartnerEntity) throws PartnerUsernameExistException, UnknownPersistenceException;

    public PartnerEntity retrievePartnerByUsername(String username) throws PartnerNotFoundException;
    
}
