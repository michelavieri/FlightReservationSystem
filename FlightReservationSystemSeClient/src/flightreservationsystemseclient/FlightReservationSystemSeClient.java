/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flightreservationsystemseclient;

import java.util.Scanner;
import ws.client.holidayReservation.PartnerEntity;

/**
 *
 * @author Chrisya
 */
public class FlightReservationSystemSeClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter username >");
        String username = sc.nextLine();
        System.out.print("Enter password >");
        String password = sc.nextLine();

        PartnerEntity partner = partnerLogin(username, password);
        System.out.println("Login successful!");

        menuMain(sc);
    }
    
    public static void menuMain(Scanner sc) {
        
        System.out.println("*** Welcome, this is Holiday Reservation System Application ***");
        System.out.println("1: Customer Login");
        System.out.println("2: Search Flight");
        System.out.println("3: Exit");
    }

    private static PartnerEntity partnerLogin(java.lang.String arg0, java.lang.String arg1) {
        ws.client.holidayReservation.HolidayReservationService_Service service = new ws.client.holidayReservation.HolidayReservationService_Service();
        ws.client.holidayReservation.HolidayReservationService port = service.getHolidayReservationServicePort();
        return port.partnerLogin(arg0, arg1);
    }

}
