/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frsreservationclient;

import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.FareEntitySessionBeanRemote;
import ejb.session.stateless.FlightScheduleEntitySessionBeanRemote;
import ejb.session.stateless.ReservationEntitySessionBeanRemote;
import ejb.session.stateless.SeatEntitySessionBeanRemote;
import ejb.session.stateless.SeatsInventoryEntitySessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author miche
 */
public class Main {

    @EJB
    private static FareEntitySessionBeanRemote fareEntitySessionBeanRemote;

    @EJB
    private static SeatEntitySessionBeanRemote seatEntitySessionBeanRemote;

    @EJB
    private static SeatsInventoryEntitySessionBeanRemote seatsInventoryEntitySessionBeanRemote;

    @EJB
    private static FlightScheduleEntitySessionBeanRemote flightScheduleEntitySessionBeanRemote;

    @EJB
    private static ReservationEntitySessionBeanRemote reservationEntitySessionBeanRemote;

    @EJB
    private static CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;
    
    
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MainApp mainApp = new MainApp(customerEntitySessionBeanRemote, reservationEntitySessionBeanRemote,
        flightScheduleEntitySessionBeanRemote, seatsInventoryEntitySessionBeanRemote, seatEntitySessionBeanRemote,
        fareEntitySessionBeanRemote);
        mainApp.runApp();
    }
}
