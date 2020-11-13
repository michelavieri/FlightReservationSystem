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
public class DataInitSessionBean2 {

    @EJB
    private EmployeeEntitySessionBeanLocal employeeEntitySessionBeanLocal;

    @EJB
    private PartnerEntitySessionBeanLocal partnerEntitySessionBeanLocal;

    @EJB
    private AirportEntitySessionBeanLocal airportEntitySessionBeanLocal;

    @EJB
    private AircraftTypeEntitySessionBeanLocal aircraftTypeEntitySessionBeanLocal;

    public DataInitSessionBean2() {
    }

    @PostConstruct
    public void postConstruct() {
        try {
            employeeEntitySessionBeanLocal.retrieveEmployeeByUsername("fleetmanager");
        } catch (EmployeeNotFoundException ex) {
            initializeData();
        }
    }

    private void initializeData() {
        try {
            employeeEntitySessionBeanLocal.createNewEmployee(new EmployeeEntity("fleetmanager", "Fleet Manager", "password", UserRoleEnum.FLEET_MANAGER));

            employeeEntitySessionBeanLocal.createNewEmployee(new EmployeeEntity("routeplanner", " Route Planner", "password", UserRoleEnum.ROUTE_PLANNER));

            employeeEntitySessionBeanLocal.createNewEmployee(new EmployeeEntity("salesmanager", "Sales Manager", "password", UserRoleEnum.SALES_MANAGER));

            employeeEntitySessionBeanLocal.createNewEmployee(new EmployeeEntity("schedulemanager", "Schedule Manager", "password", UserRoleEnum.SCHEDULE_MANAGER));

            partnerEntitySessionBeanLocal.createNewPartner(new PartnerEntity("holidaydotcom", "Holiday.com", "password"));

            airportEntitySessionBeanLocal.createNewAirport(new AirportEntity("SIN", "Changi", "Singapore", "Singapore", "Singapore", "+0800"));

            airportEntitySessionBeanLocal.createNewAirport(new AirportEntity("TPE", "Taoyuan", "Taoyuan", "Taipei", "Taiwan R.O.C.", "+0800"));

            airportEntitySessionBeanLocal.createNewAirport(new AirportEntity("HKG", "Hong Kong", "Chek Lap Kok", "Hong Kong", "China", "+0800"));

            airportEntitySessionBeanLocal.createNewAirport(new AirportEntity("NRT", "Narita", "Narita", "Chiba", "Japan", "+0900"));

            airportEntitySessionBeanLocal.createNewAirport(new AirportEntity("SYD", "Sydney", "Taoyuan", "New South Wales", "Australia", "+1100"));

            aircraftTypeEntitySessionBeanLocal.createNewAircraftType(new AircraftTypeEntity("Boeing 737", 200));

            aircraftTypeEntitySessionBeanLocal.createNewAircraftType(new AircraftTypeEntity("Boeing 747", 400));
        } catch (EmployeeUsernameExistException | PartnerUsernameExistException | AirportNameExistException | AircraftTypeNameExistException | UnknownPersistenceException ex) {
            ex.printStackTrace();
        }
    }
}
