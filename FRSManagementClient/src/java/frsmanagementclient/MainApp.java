/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frsmanagementclient;

import ejb.session.stateless.EmployeeEntitySessionBeanRemote;
import entity.EmployeeEntity;
import java.util.Scanner;
import util.exception.InvalidLoginCredentialException;
import util.exception.InvalidUsernameException;
import util.exception.WrongPasswordException;

/**
 *
 * @author miche
 */
public class MainApp {
    
    private EmployeeEntity currentEmployee;
    
    private EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote;
    
    public MainApp() {
    }
    
    public void runApp() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        while (true) {
            System.out.println("*** Welcome, this is FRS Management Application ***");
            System.out.println("1: Login");
            System.out.println("2: Exit");
            response = 0;

            while (response < 1 || response > 2) {
                System.out.print("> ");
                response = sc.nextInt();

                if (response == 1) {
                    try {
                        doLogin(sc);
                        System.out.println("Login successful!");
                        
                        menuMain(sc);
                    } catch (InvalidUsernameException | WrongPasswordException | InvalidLoginCredentialException ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage());
                    }
                } else if (response == 2) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!");
                }
            }
            if (response == 2) {
                break;
            }
        }
    }
    
    public void doLogin(Scanner sc) throws InvalidUsernameException, WrongPasswordException, InvalidLoginCredentialException {
        System.out.println("*** FRS Employee :: Login ***");
        sc.nextLine();
        System.out.print("Enter username> ");
        String username = sc.nextLine();
        System.out.print("Enter password> ");
        String password = sc.nextLine();

        if (username.length() > 0 && password.length() > 0) {
            try {
                currentEmployee = employeeEntitySessionBeanRemote.employeeLogin(username, password);
            } catch (InvalidUsernameException ex) {
                throw new InvalidUsernameException(ex.getMessage());
            } catch (WrongPasswordException ex) {
                throw new WrongPasswordException(ex.getMessage());
            }
        } else {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }
    
    public void menuMain(Scanner sc) {
        Integer response = 0;

        while (true) {
            System.out.println("*** FRS Management Application ***");
            System.out.println("You are login as " + currentEmployee.getName() + " with " + currentEmployee.getRole().toString() + " rights");
            System.out.println("1: Create Customer");
            System.out.println("2: Open Deposit Account");
            System.out.println("3: Issue ATM Card");
            System.out.println("4: Issue Replacement ATM Card");
            System.out.println("5: Create Employee");
            System.out.println("6: Logout");
            response = 0;

            while (response < 1 || response > 6) {
                System.out.print("> ");
                response = sc.nextInt();

                if (response == 1) {
                    
                } else if (response == 2) {
                    
                } else if (response == 3) {
                    
                } else if (response == 4) {
                    
                } else if (response == 5) {
                    
                } else {
                    break;
                }
            }
            if (response == 6) {
                break;
            }
        }
    }
}
