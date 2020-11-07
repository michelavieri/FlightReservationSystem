/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.EmployeeEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.EmployeeNotFoundException;
import util.exception.EmployeeUsernameExistException;
import util.exception.InvalidUsernameException;
import util.exception.UnknownPersistenceException;
import util.exception.WrongPasswordException;

/**
 *
 * @author Chrisya
 */
@Stateless
public class EmployeeEntitySessionBean implements EmployeeEntitySessionBeanRemote, EmployeeEntitySessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;

    public EmployeeEntitySessionBean() {
    }
    
    @Override
    public Long createNewEmployee(EmployeeEntity newEmployeeEntity) throws EmployeeUsernameExistException, UnknownPersistenceException
    {
        try
        {
            entityManager.persist(newEmployeeEntity);
            entityManager.flush();

            return newEmployeeEntity.getEmployeeId();
        }
        catch(PersistenceException ex)
        {
            if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
            {
                if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                {
                    throw new EmployeeUsernameExistException();
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
    public EmployeeEntity retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException
    {
        Query query = entityManager.createQuery("SELECT e FROM EmployeeEntity e WHERE e.username = :inUsername");
        query.setParameter("inUsername", username);
        
        try
        {
            return (EmployeeEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new EmployeeNotFoundException("Employee Username " + username + " does not exist!");
        }
    }
    
   @Override
      public EmployeeEntity employeeLogin(String username, String password) throws InvalidUsernameException, WrongPasswordException
    {
        try
        {
            EmployeeEntity employee = this.retrieveEmployeeByUsername(username);
            employee = entityManager.find(EmployeeEntity.class, employee.getEmployeeId());
            
            if(employee.getPassword().equals(password))
            {              
                return employee;
            }
            else
            {
                throw new WrongPasswordException("Invalid password!");
            }
        }
        catch(EmployeeNotFoundException ex)
        {
            throw new InvalidUsernameException("Username does not exist!");
        }
    }
}

