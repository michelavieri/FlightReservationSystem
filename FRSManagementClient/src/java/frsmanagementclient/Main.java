/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frsmanagementclient;

import ejb.session.stateless.AircraftTypeEntitySessionBeanRemote;
import ejb.session.stateless.AirportEntitySessionBeanRemote;
import ejb.session.stateless.CabinClassConfigurationSessionBeanRemote;
import ejb.session.stateless.EmployeeEntitySessionBeanRemote;
import ejb.session.stateless.PartnerEntitySessionBeanRemote;
import ejb.session.stateless.AircraftConfigurationEntitySessionBeanRemote;
import ejb.session.stateless.FlightEntitySessionBeanRemote;
import ejb.session.stateless.FlightRouteEntitySessionBeanRemote;
import ejb.session.stateless.FlightScheduleEntitySessionBeanRemote;
import ejb.session.stateless.FlightSchedulePlanEntitySessionBeanRemote;
import ejb.session.stateless.ReservationEntitySessionBeanRemote;
import ejb.session.stateless.SeatsInventoryEntitySessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author miche
 */
public class Main {

    @EJB
    private static SeatsInventoryEntitySessionBeanRemote seatsInventoryEntitySessionBean;

    @EJB
    private static FlightSchedulePlanEntitySessionBeanRemote flightSchedulePlanEntitySessionBean;

    @EJB
    private static FlightScheduleEntitySessionBeanRemote flightScheduleEntitySessionBean;

    @EJB
    private static ReservationEntitySessionBeanRemote reservationEntitySessionBeanRemote;

    @EJB
    private static FlightEntitySessionBeanRemote flightEntitySessionBeanRemote;

    @EJB
    private static FlightRouteEntitySessionBeanRemote flightRouteEntitySessionBeanRemote;

    @EJB
    private static AircraftConfigurationEntitySessionBeanRemote aircraftConfigurationEntitySessionBeanRemote;

    @EJB
    private static CabinClassConfigurationSessionBeanRemote cabinClassConfigurationSessionBeanRemote;

    @EJB
    private static PartnerEntitySessionBeanRemote partnerEntitySessionBeanRemote;

    @EJB
    private static EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote;

    @EJB
    private static AirportEntitySessionBeanRemote airportEntitySessionBeanRemote;

    @EJB
    private static AircraftTypeEntitySessionBeanRemote aircraftTypeEntitySessionBeanRemote;

    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        MainApp mainApp = new MainApp(partnerEntitySessionBeanRemote, employeeEntitySessionBeanRemote,
                airportEntitySessionBeanRemote, aircraftTypeEntitySessionBeanRemote, cabinClassConfigurationSessionBeanRemote,
                aircraftConfigurationEntitySessionBeanRemote, flightEntitySessionBeanRemote, flightRouteEntitySessionBeanRemote,
                flightScheduleEntitySessionBean, flightSchedulePlanEntitySessionBean, reservationEntitySessionBeanRemote, seatsInventoryEntitySessionBean);
        mainApp.runApp();
    }

}
