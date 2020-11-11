/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frsreservationclient;

import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import entity.CustomerEntity;
import java.util.Scanner;
import util.exception.EmailExistException;
import util.exception.InvalidLoginCredentialsException;
import util.exception.InvalidRegistrationException;
import util.exception.InvalidUsernameException;
import util.exception.UsernameExistException;
import util.exception.WrongPasswordException;

/**
 *
 * @author miche
 */
public class MainApp {

    public CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;

    private CustomerEntity currentCustomer;

    public MainApp() {

    }

    public MainApp(CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote) {
        this.customerEntitySessionBeanRemote = customerEntitySessionBeanRemote;
    }

    public void runApp() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        while (true) {
            System.out.println("*** Welcome, this is FRS Reservation Application ***");
            System.out.println("1: Register as Customer");
            System.out.println("2: Customer Login");
            System.out.println("3: Search Flight");
            System.out.println("4: Exit");
            response = 0;

            while (response < 1 || response > 2) {
                System.out.print("> ");
                response = sc.nextInt();

                if (response == 1) {
//                    try {
//                        doRegister(sc);
//                        System.out.println("Your account has been registered! You are automatically logged in.");
//                        menuMain(sc);
//                    } catch (InvalidRegistrationException ex) {
//                        System.out.println(ex.getMessage());
//                    }
                } else if (response == 2) {
//                    try {
//                        doLogin(sc);
//                        System.out.println("Login successful!");
//                        menuMain(sc);
//                    } catch (InvalidUsernameException | WrongPasswordException | InvalidLoginCredentialsException ex) {
//                        System.out.println("Invalid login credential: " + ex.getMessage());
//                    }
                } else if (response == 3) {
                    ReservationOperationModule.searchFlights(sc);
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

    private void doRegister(Scanner sc) {
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

//        try {
//            customerEntitySessionBeanRemote.registerCustomer(new CustomerEntity(firstName, lastName, email, phoneNum, address, password));
//        } catch (InvalidUsernameException | UsernameExistException | EmailExistException ex) {
//            throw new InvalidRegistrationException(ex.getMessage());
//        }
    }

    private void doLogin(Scanner sc) {
        System.out.println("*** FRS Employee :: Login ***");
        sc.nextLine();
        System.out.print("Enter email> ");
        String email = sc.nextLine();
        System.out.print("Enter password> ");
        String password = sc.nextLine();

//        if (email.length() > 0 && password.length() > 0) {
//            try {
//                currentCustomer = customerEntitySessionBeanRemote.employeeLogin(email, password);
//            } catch (InvalidUsernameException ex) {
//                throw new InvalidUsernameException(ex.getMessage());
//            } catch (WrongPasswordException ex) {
//                throw new WrongPasswordException(ex.getMessage());
//            }
//        } else {
//            throw new InvalidLoginCredentialsException("Missing login credential!");
//        }
    }

    private void menuMain(Scanner sc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
