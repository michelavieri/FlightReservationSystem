/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frsreservationclient;

import ejb.session.stateless.AircraftConfigurationEntitySessionBeanRemote;
import ejb.session.stateless.CreditCardEntitySessionBeanRemote;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.FareEntitySessionBeanRemote;
import ejb.session.stateless.FlightScheduleEntitySessionBeanRemote;
import ejb.session.stateless.ReservationEntitySessionBeanRemote;
import ejb.session.stateless.SeatEntitySessionBeanRemote;
import ejb.session.stateless.SeatsInventoryEntitySessionBeanRemote;
import entity.CustomerEntity;
import java.util.Scanner;
import util.exception.EmailExistException;
import util.exception.InvalidEmailException;
import util.exception.InvalidLoginCredentialsException;
import util.exception.WrongPasswordException;

/**
 *
 * @author miche
 */
public class MainApp {

    public CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;

    public ReservationEntitySessionBeanRemote reservationEntitySessionBeanRemote;

    public FlightScheduleEntitySessionBeanRemote flightScheduleEntitySessionBeanRemote;

    public CustomerEntity currentCustomer;

    public ReservationOperationModule reservationOperationModule;

    public SeatsInventoryEntitySessionBeanRemote seatsInventoryEntitySessionBeanRemote;

    public SeatEntitySessionBeanRemote seatEntitySessionBeanRemote;

    public FareEntitySessionBeanRemote fareEntitySessionBeanRemote;
    
    public CreditCardEntitySessionBeanRemote creditCardEntitySessionBeanRemote;
    
    public AircraftConfigurationEntitySessionBeanRemote aircraftConfigurationEntitySessionBeanRemote;

    public MainApp() {

    }

    public MainApp(CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote,
            ReservationEntitySessionBeanRemote reservationEntitySessionBeanRemote,
            FlightScheduleEntitySessionBeanRemote flightScheduleEntitySessionBeanRemote,
            SeatsInventoryEntitySessionBeanRemote seatsInventoryEntitySessionBeanRemote,
            SeatEntitySessionBeanRemote seatEntitySessionBeanRemote,
            FareEntitySessionBeanRemote fareEntitySessionBeanRemote,
            CreditCardEntitySessionBeanRemote creditCardEntitySessionBeanRemote,
            AircraftConfigurationEntitySessionBeanRemote aircraftConfigurationEntitySessionBeanRemote) {
        this.customerEntitySessionBeanRemote = customerEntitySessionBeanRemote;
        this.reservationEntitySessionBeanRemote = reservationEntitySessionBeanRemote;
        this.flightScheduleEntitySessionBeanRemote = flightScheduleEntitySessionBeanRemote;
        this.seatsInventoryEntitySessionBeanRemote = seatsInventoryEntitySessionBeanRemote;
        this.seatEntitySessionBeanRemote = seatEntitySessionBeanRemote;
        this.fareEntitySessionBeanRemote = fareEntitySessionBeanRemote;
        this.creditCardEntitySessionBeanRemote = creditCardEntitySessionBeanRemote;
        this.aircraftConfigurationEntitySessionBeanRemote = aircraftConfigurationEntitySessionBeanRemote;
    }

    public void runApp() {
        reservationOperationModule = new ReservationOperationModule(reservationEntitySessionBeanRemote,
                customerEntitySessionBeanRemote, flightScheduleEntitySessionBeanRemote, seatsInventoryEntitySessionBeanRemote,
                 seatEntitySessionBeanRemote, fareEntitySessionBeanRemote, creditCardEntitySessionBeanRemote, aircraftConfigurationEntitySessionBeanRemote);
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        while (true) {
            System.out.println("*** Welcome, this is FRS Reservation Application ***");
            System.out.println("1: Register as Customer");
            System.out.println("2: Customer Login");
            System.out.println("3: Search Flight");
            System.out.println("4: Exit");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");
                response = sc.nextInt();

                if (response == 1) {
                    System.out.println("*** FRS Reservation System :: Register as Customer ***");
                    try {
                        doRegister(sc);
                        System.out.println("Your account has been registered! You are automatically logged in.");
                        menuMain(sc);
                    } catch (EmailExistException ex) {
                        System.out.println(ex.getMessage());
                    }
                } else if (response == 2) {
                    System.out.println("*** FRS Reservation System :: Customer Login ***");
                    try {
                        doLogin(sc);
                        System.out.println("Login successful!");
                        menuMain(sc);
                    } catch (InvalidEmailException | WrongPasswordException | InvalidLoginCredentialsException ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage());
                    }
                } else if (response == 3) {
                    System.out.println("*** FRS Reservation System :: Search Flights ***");
                    reservationOperationModule.searchFlights(sc, null);
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!");
                }
            }
            if (response == 4) {
                break;
            }
        }
    }

    private void doRegister(Scanner sc) throws EmailExistException {
        System.out.println("*** FRS Customer :: Register as Customer ***");
        sc.nextLine();
        System.out.print("Enter first name> ");
        String firstName = sc.nextLine();
        System.out.print("Enter last name> ");
        String lastName = sc.nextLine();
        System.out.print("Enter email> ");
        String email = sc.nextLine();
        System.out.print("Enter phone number> ");
        String phoneNum = sc.nextLine();
        System.out.print("Enter address> ");
        String address = sc.nextLine();
        System.out.print("Enter password> ");
        String password = sc.nextLine();

        try {
            currentCustomer = customerEntitySessionBeanRemote.registerCustomer(new CustomerEntity(firstName, lastName, email, phoneNum, address, password));
        } catch (EmailExistException ex) {
            throw new EmailExistException(ex.getMessage());
        }
    }

    private void doLogin(Scanner sc) throws InvalidEmailException, WrongPasswordException, InvalidLoginCredentialsException {
        System.out.println("*** FRS Employee :: Login ***");
        sc.nextLine();
        System.out.print("Enter email> ");
        String email = sc.nextLine();
        System.out.print("Enter password> ");
        String password = sc.nextLine();

        if (email.length() > 0 && password.length() > 0) {
            try {
                currentCustomer = customerEntitySessionBeanRemote.customerLogin(email, password);
            } catch (InvalidEmailException ex) {
                throw new InvalidEmailException(ex.getMessage());
            } catch (WrongPasswordException ex) {
                throw new WrongPasswordException(ex.getMessage());
            }
        } else {
            throw new InvalidLoginCredentialsException("Missing login credential!");
        }
    }

    private void menuMain(Scanner sc) {
        reservationOperationModule = new ReservationOperationModule(reservationEntitySessionBeanRemote,
                customerEntitySessionBeanRemote, flightScheduleEntitySessionBeanRemote, seatsInventoryEntitySessionBeanRemote,
                 seatEntitySessionBeanRemote, fareEntitySessionBeanRemote, creditCardEntitySessionBeanRemote, aircraftConfigurationEntitySessionBeanRemote);
        Integer response = 0;
        while (true) {
            System.out.println("*** FRS Reservation Application: Customer Logged In ***");
            System.out.println("Welcome, " + currentCustomer.getFirstName() + "!");
            System.out.println("1: Search Flights");
            System.out.println("2: View My Flight Reservations");
            System.out.println("3: View My Flight Reservation Details");
            System.out.println("4: Logout");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");
                response = sc.nextInt();

                if (response == 1) {
                    System.out.println("*** FRS Customer :: Search Flights ***");
                    reservationOperationModule.searchFlights(sc, currentCustomer);
                } else if (response == 2) {
                    System.out.println("*** FRS Customer :: View My Flight Reservations ***");
                    this.reservationOperationModule.viewMyFlightReservations(currentCustomer);
                } else if (response == 3) {
                    System.out.println("*** FRS Customer :: View My Flight Reservation Details ***");
                    reservationOperationModule.viewFlightReservationDetails(currentCustomer, sc);
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!");
                }
            }
            if (response == 4) {
                break;
            }
        }
    }
}
