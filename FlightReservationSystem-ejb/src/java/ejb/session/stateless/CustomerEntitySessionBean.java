/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import entity.EmployeeEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CustomerNotFoundException;
import util.exception.EmailExistException;
import util.exception.InvalidEmailException;
import util.exception.WrongPasswordException;

/**
 *
 * @author miche
 */
@Stateless
public class CustomerEntitySessionBean implements CustomerEntitySessionBeanRemote, CustomerEntitySessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;

    @Override
    public CustomerEntity registerCustomer(CustomerEntity newCustomer) throws EmailExistException {
        try {
            emailExists(newCustomer.getEmail());
        } catch (EmailExistException ex) {
            throw new EmailExistException(ex.getMessage());
        }

        entityManager.persist(newCustomer);
        entityManager.flush();

        return newCustomer;
    }

    @Override
    public void emailExists(String email) throws EmailExistException {
        Query query = entityManager.createQuery("SELECT e FROM CustomerEntity e WHERE e.email = :inEmail");
        query.setParameter("inEmail", email);
        try {
            query.getSingleResult();
            throw new EmailExistException("This email has already been registered!");
        } catch (NoResultException ex) {
        }
    }

    @Override
    public CustomerEntity customerLogin(String email, String password) throws InvalidEmailException, WrongPasswordException {
        try {
            CustomerEntity customer = this.retrieveCustomerByEmail(email);
            customer = entityManager.find(CustomerEntity.class, customer.getCustomerId());

            if (customer.getPassword().equals(password)) {
                return customer;
            } else {
                throw new WrongPasswordException("Invalid password!");
            }
        } catch (CustomerNotFoundException ex) {
            throw new InvalidEmailException(ex.getMessage());
        }
    }
    
    @Override
    public CustomerEntity retrieveCustomerByEmail(String email) throws CustomerNotFoundException {
        Query query = entityManager.createQuery("SELECT e FROM CustomerEntity e WHERE e.email = :inEmail");
        query.setParameter("inEmail", email);
        CustomerEntity customer = null;
        try {
            customer = (CustomerEntity) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new CustomerNotFoundException("Email has not been registered yet!");
        }
        
        return customer;
    }

    @Override
    public CustomerEntity retrieveCustomer(CustomerEntity cust) {
        return entityManager.find(CustomerEntity.class, cust);
    }
}
