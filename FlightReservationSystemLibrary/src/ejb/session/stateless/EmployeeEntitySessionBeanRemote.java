/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.EmployeeEntity;
import javax.ejb.Remote;
import util.exception.EmployeeNotFoundException;
import util.exception.EmployeeUsernameExistException;
import util.exception.InvalidUsernameException;
import util.exception.UnknownPersistenceException;
import util.exception.WrongPasswordException;

/**
 *
 * @author miche
 */
@Remote
public interface EmployeeEntitySessionBeanRemote {
    
    public Long createNewEmployee(EmployeeEntity newEmployeeEntity) throws EmployeeUsernameExistException, UnknownPersistenceException;

    public EmployeeEntity retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException;
    
    public EmployeeEntity employeeLogin(String username, String password) throws InvalidUsernameException, WrongPasswordException;
}
