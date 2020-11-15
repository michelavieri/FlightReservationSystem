/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PartnerEntity;
import javax.ejb.Remote;
import util.exception.InvalidUsernameException;
import util.exception.PartnerNotFoundException;
import util.exception.PartnerUsernameExistException;
import util.exception.UnknownPersistenceException;
import util.exception.WrongPasswordException;

/**
 *
 * @author miche
 */
@Remote
public interface PartnerEntitySessionBeanRemote {
    
    public String createNewPartner(PartnerEntity newPartnerEntity) throws PartnerUsernameExistException, UnknownPersistenceException;

    public PartnerEntity retrievePartnerByUsername(String username) throws PartnerNotFoundException;
    
    public PartnerEntity partnerLogin(String username, String password) throws InvalidUsernameException, WrongPasswordException;
}
