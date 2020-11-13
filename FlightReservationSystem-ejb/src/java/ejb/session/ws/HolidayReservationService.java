/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.CustomerEntitySessionBeanLocal;
import ejb.session.stateless.FlightScheduleEntitySessionBeanLocal;
import ejb.session.stateless.ReservationEntitySessionBeanLocal;
import entity.CustomerEntity;
import entity.FlightScheduleEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import util.enumeration.CabinClassTypeEnum;
import util.exception.InvalidEmailException;
import util.exception.WrongPasswordException;

/**
 *
 * @author Chrisya
 */
@WebService(serviceName = "HoildayReservationService")
@Stateless()
public class HolidayReservationService {

    @EJB
    private FlightScheduleEntitySessionBeanLocal flightScheduleEntitySessionBeanLocal;

    @EJB
    private ReservationEntitySessionBeanLocal reservationEntitySessionBeanLocal;

    @EJB
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "customerLogin")
    public CustomerEntity customerLogin(String email, String password) {
        CustomerEntity customer = null;
        try {
            customer = customerEntitySessionBeanLocal.customerLogin(email, password);
        } catch (InvalidEmailException ex) {
            
        } catch (WrongPasswordException ex) {
            
        }
        
        return customer;
    }
    
//    @WebMethod(operationName = "searchConnectingFlights")
//    public List<List<FlightScheduleEntity>> searchConnectingFlights(String departureAirport,
//            String destinationAirport, String departureDateTime, int numOfPassenger, int stopovers, CabinClassTypeEnum classType) {
//        
//        return reservationEntitySessionBeanLocal.searchConnectingFlights(departureAirport, destinationAirport, departureDateTime, numOfPassenger, stopovers, classType);
//    }
//    
//    @WebMethod(operationName = "searchConnectingFlightsBefore")
//    public List<List<FlightScheduleEntity>> searchConnectingFlightsBefore(String departureAirport,
//            String destinationAirport, String departureDateTime, int numOfPassenger, int stopovers, CabinClassTypeEnum classType) {
//        
//        return reservationEntitySessionBeanLocal.searchConnectingFlightsBefore(departureAirport, destinationAirport, departureDateTime, numOfPassenger, stopovers, classType);
//    }
//    
//    @WebMethod(operationName = "searchConnectingFlightsAfter")
//    public List<List<FlightScheduleEntity>> searchConnectingFlightsAfter(String departureAirport,
//            String destinationAirport, String departureDateTime, int numOfPassenger, int stopovers, CabinClassTypeEnum classType) {
//        
//        return reservationEntitySessionBeanLocal.searchConnectingFlightsAfter(departureAirport, destinationAirport, departureDateTime, numOfPassenger, stopovers, classType);
//    }
//    
//    @WebMethod(operationName = "searchDirectFlights")
//    public List<List<FlightScheduleEntity>> searchDirectFlights(String departureAirport, String destinationAirport, String departureDate, int numOfPassenger, CabinClassTypeEnum classType) {
//        
//        return flightScheduleEntitySessionBeanLocal.searchDirectFlights(departureAirport, destinationAirport, departureDate, numOfPassenger, classType);
//    }
}
