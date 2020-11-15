/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.CustomerEntitySessionBeanLocal;
import ejb.session.stateless.FlightScheduleEntitySessionBeanLocal;
import ejb.session.stateless.PartnerEntitySessionBeanLocal;
import ejb.session.stateless.ReservationEntitySessionBeanLocal;
import entity.CustomerEntity;
import entity.FlightScheduleEntity;
import entity.PartnerEntity;
import entity.ReservationEntity;
import java.util.ArrayList;
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
import util.exception.InvalidUsernameException;
import util.exception.WrongPasswordException;

/**
 *
 * @author Chrisya
 */
@WebService(serviceName = "HolidayReservationService")
@Stateless
public class HolidayReservationService {

    @EJB(name = "PartnerEntitySessionBeanLocal")
    private PartnerEntitySessionBeanLocal partnerEntitySessionBeanLocal;

    @EJB
    private FlightScheduleEntitySessionBeanLocal flightScheduleEntitySessionBeanLocal;

    @EJB
    private ReservationEntitySessionBeanLocal reservationEntitySessionBeanLocal;

    @EJB
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "partnerLogin")
    public PartnerEntity partnerLogin(String username, String password) {
        
        PartnerEntity partner = null;
        try {
            partner = partnerEntitySessionBeanLocal.partnerLoginUnmanaged(username, password);
            
            List<ReservationEntity> reservations = partner.getReservationsEntitys();
            
            for(ReservationEntity reservation:reservations) {
                reservation.setCustomer(null);
            }
            
        } catch (InvalidUsernameException ex) {
            System.out.println(ex.getMessage());
        } catch (WrongPasswordException ex) {
            System.out.println(ex.getMessage());
        }
        
        return partner;
    }
    
    @WebMethod(operationName = "testing")
    public List<List<Integer>> searchConnectingFlights() {
        
        List<Integer> result = new ArrayList<>();
        List<List<Integer>> test = new ArrayList<>();
        
        result.add(1);
        test.add(result);
        
        return test;
    }
    
//    @WebMethod(operationName = "searchConnectingFlights")
//    public List<List<FlightScheduleEntity>> searchConnectingFlights(String departureAirport,
//            String destinationAirport, String departureDateTime, int numOfPassenger, int stopovers, CabinClassTypeEnum classType) {
//        
//        List<List<FlightScheduleEntity>> result = reservationEntitySessionBeanLocal.searchConnectingFlights(departureAirport, destinationAirport, departureDateTime, numOfPassenger, stopovers, classType);
//        
//        return result;
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
