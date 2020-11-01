/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.AircraftTypeEntitySessionBeanLocal;
import ejb.session.stateless.AirportEntitySessionBeanLocal;
import ejb.session.stateless.EmployeeEntitySessionBeanLocal;
import ejb.session.stateless.PartnerEntitySessionBeanLocal;
import entity.AircraftTypeEntity;
import entity.AirportEntity;
import entity.EmployeeEntity;
import entity.PartnerEntity;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import util.enumeration.UserRoleEnum;
import util.exception.AircraftTypeNameExistException;
import util.exception.AirportNameExistException;
import util.exception.EmployeeNotFoundException;
import util.exception.EmployeeUsernameExistException;
import util.exception.PartnerUsernameExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author miche
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {
    
    @EJB
    private EmployeeEntitySessionBeanLocal employeeEntitySessionBeanLocal;
    
    @EJB
    private PartnerEntitySessionBeanLocal partnerEntitySessionBeanLocal;
    
     @EJB
    private AirportEntitySessionBeanLocal airportEntitySessionBeanLocal;
     
      @EJB
    private AircraftTypeEntitySessionBeanLocal aircraftTypeEntitySessionBeanLocal;

    public DataInitSessionBean() {
    }
    
    @PostConstruct
    public void postConstruct() {
        try
        {
            employeeEntitySessionBeanLocal.retrieveEmployeeByUsername("employee");//name of employee?
        }
        catch(EmployeeNotFoundException ex)
        {
            initializeData();
        }
    }
    
     private void initializeData() {
        try
        {
            employeeEntitySessionBeanLocal.createNewEmployee(new EmployeeEntity("employee", "Default Employee",  "password", UserRoleEnum.EMPLOYEE));

            partnerEntitySessionBeanLocal.createNewPartner(new PartnerEntity("holidayReservation", "Holiday Reservation System", "password"));
            
            airportEntitySessionBeanLocal.createNewAirport(new AirportEntity("SIN", "Singapore Changi Airport", "Singapore", "Singapore", "Singapore", 8));
            
            airportEntitySessionBeanLocal.createNewAirport(new AirportEntity("TPE", "Taoyuan International Airport", "Taipei", "Taipei", "Taiwan", 8));
            
            aircraftTypeEntitySessionBeanLocal.createNewAircraftType(new AircraftTypeEntity("Boeing 737", 230));
            
            aircraftTypeEntitySessionBeanLocal.createNewAircraftType(new AircraftTypeEntity("Boeing 747", 410));
        }
        catch(EmployeeUsernameExistException | PartnerUsernameExistException | AirportNameExistException | AircraftTypeNameExistException | UnknownPersistenceException ex)
        {
            ex.printStackTrace();
        }
    }
}
