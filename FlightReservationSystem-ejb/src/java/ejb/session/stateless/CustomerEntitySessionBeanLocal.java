/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import javax.ejb.Local;
import util.exception.CustomerNotFoundException;
import util.exception.EmailExistException;
import util.exception.InvalidEmailException;
import util.exception.WrongPasswordException;

/**
 *
 * @author miche
 */
@Local
public interface CustomerEntitySessionBeanLocal {

    public void emailExists(String email) throws EmailExistException;

    public CustomerEntity retrieveCustomer(CustomerEntity cust);

    public CustomerEntity registerCustomer(CustomerEntity newCustomer) throws EmailExistException;

    public CustomerEntity retrieveCustomerByEmail(String email) throws CustomerNotFoundException;

    public CustomerEntity customerLogin(String email, String password) throws InvalidEmailException, WrongPasswordException;
}
