/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frsmanagementclient;

import ejb.session.stateless.EmployeeEntitySessionBeanRemote;
import entity.AircraftTypeEntity;
import entity.CabinClassConfigurationEntity;
import entity.EmployeeEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import util.enumeration.UserRoleEnum;
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
        UserRoleEnum role = currentEmployee.getRole();

        if (role == UserRoleEnum.FLEET_MANAGER) {
            menuMainFleet(sc);
        } else if (role == UserRoleEnum.ROUTE_PLANNER) {
            menuMainRoute(sc);
        } else if (role == UserRoleEnum.SCHEDULE_MANAGER) {
            menuMainSchedule(sc);
        } else if (role == UserRoleEnum.SALES_MANAGER) {
            menuMainSales(sc);
        } else {
            menuMainEmployee(sc);
        }
    }

    public void menuMainFleet(Scanner sc) {
        Integer response = 0;

        while (true) {
            System.out.println("*** FRS Management Application ***");
            System.out.println("You are login as " + currentEmployee.getName() + " with Fleet Manager rights");
            System.out.println("1: Create Aircraft Configuration");
            System.out.println("2: View All Aircraft Configurations");
            System.out.println("3: View Aircraft Configuration Details");
            System.out.println("4: Logout");
            response = 0;

            while (response < 1 || response > 6) {
                System.out.print("> ");
                response = sc.nextInt();

                if (response == 1) {
                    System.out.println("*** FRS Fleet Manager :: Create New Aircraft Configuration ***");
                    sc.nextLine();
                    System.out.println("Here are the available lists of aircraft types:")
                    List<AircraftTypeEntity> types = aircraftTypeEntitySessionBean.retrieveAllTypes();
                    for (AircraftTypeEntity a : types) {
                        System.out.println("- " + a);
                    }
                    System.out.print("Enter aircraft type> ");
                    String type = sc.nextLine();
                    System.out.print("Enter aircraft code> ");
                    String code = sc.nextLine();
                    System.out.print("Enter aircraft name> ");
                    String name = sc.nextLine();
                    int numOfClasses = 0;
                    while (numOfClasses < 1 || numOfClasses > 4) {
                        System.out.print("Enter number of cabin classes (1-4)> ");
                        numOfClasses = sc.nextInt();
                        if (numOfClasses < 1 || numOfClasses > 4) {
                            System.out.println("Please enter a number from 1-4!");
                        }
                    }
                    List<CabinClassConfigurationEntity> classes = new ArrayList<CabinClassConfigurationEntity>();
                    System.out.println("Here are the list of cabin class types:");
                    System.out.println("1: First Class");
                    System.out.println("2: Business Class");
                    System.out.println("3: Premium Economy Class");
                    System.out.println("4: Economy Class");
                    System.out.println("Enter " + numOfClasses + " cabin classes based on the numbers above:");
                    for (int i = 1; i <= numOfClasses; i++) {
                        int response2 = 0;
                        CabinClassTypeEnum classType = CabinClassTypeEnum.FIRST_CLASS;
                        while (response2 < 1 || response2 > 4) {
                            System.out.print(i + "> ");
                            if (response2 == 1) {
                                classType = CabinClassTypeEnum.FIRST_CLASS;
                            } else if (response2 == 2) {
                                classType = CabinClassTypeEnum.BUSINESS_CLASS;
                            } else if (response2 == 3) {
                                classType = CabinClassTypeEnum.PREMIUM_ECONOMY_CLASS;
                            } else if (response2 == 4) {
                                classType = CabinClassTypeEnum.ECONOMY_CLASS;
                            } else {
                                System.out.println("Please enter numbers from 1-4!");
                            }

                            System.out.println("***Create Cabin Class Configuration***");
                            System.out.print("Enter number of aisles> ");
                            int aisles = sc.nextInt();
                            System.out.print("Enter number of rows> ");
                            int rows = sc.nextInt();
                            System.out.print("Enter number of seat abreast> ");
                            int abreast = sc.nextInt();
                            System.out.print("Enter maximum capacity> ");
                            int maxCapacity = sc.nextInt();
                            classes.add(cabinClassConfigurationSessionBeanRemote.
                                    createNewCabinClassConfiguration(
                                            new CabinClassConfigurationEntity(aisles, rows, abreast, maxCapacity, classType)));
                        }
                    }

                    aircraftConfigurationEntitySessionBeanRemote.createAircraftConfiguration(code, name, numOfClasses, type, classes);
                } else if (response == 2) {

                } else if (response == 3) {

                } else if (response == 4) {

                } else if (response == 5) {

                } else if (response == 6) {
                    break;
                } else {
                    System.out.println("Invalid response!");
                }
            }
            if (response == 6) {
                break;
            }
        }
    }

    public void menuMainRoute(Scanner sc) {
        Integer response = 0;

        while (true) {
            System.out.println("*** FRS Management Application ***");
            System.out.println("You are login as " + currentEmployee.getName() + " with Route Planner rights");
            System.out.println("1: Create Flight Route");
            System.out.println("2: View All Flight Routes");
            System.out.println("3: Delete Flight Route");
            System.out.println("4: Logout");
            response = 0;

            while (response < 1 || response > 6) {
                System.out.print("> ");
                response = sc.nextInt();

                if (response == 1) {

                } else if (response == 2) {

                } else if (response == 3) {

                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid response!");
                }
            }
            if (response == 6) {
                break;
            }
        }
    }

    public void menuMainSchedule(Scanner sc) {
        Integer response = 0;

        while (true) {
            System.out.println("*** FRS Management Application ***");
            System.out.println("You are login as " + currentEmployee.getName() + " with Schedule Manager rights");
            System.out.println("1: Create Flight");
            System.out.println("2: View All Flights");
            System.out.println("3: View Flight Details");
            System.out.println("4: Create Flight Schedule Plan");
            System.out.println("5: View All Flight Schedule Plans");
            System.out.println("6: View Flight Schedule Plan Details");
            System.out.println("7: Logout");
            response = 0;

            while (response < 1 || response > 6) {
                System.out.print("> ");
                response = sc.nextInt();

                if (response == 1) {

                } else if (response == 2) {

                } else if (response == 3) {

                } else if (response == 4) {

                } else if (response == 5) {

                } else if (response == 6) {

                } else if (response == 7) {
                    break;
                } else {
                    System.out.println("Invalid response!");
                }
            }
            if (response == 6) {
                break;
            }
        }
    }

    public void menuMainSales(Scanner sc) {
        Integer response = 0;

        while (true) {
            System.out.println("*** FRS Management Application ***");
            System.out.println("You are login as " + currentEmployee.getName() + " with Sales Manager rights");
            System.out.println("1: View Seats Inventory");
            System.out.println("2: View Flight Reservations");
            System.out.println("3: Logout");
            response = 0;

            while (response < 1 || response > 3) {
                System.out.print("> ");
                response = sc.nextInt();

                if (response == 1) {

                } else if (response == 2) {

                } else if (response == 3) {
                    break;
                } else {
                    System.out.println("Invalid response!");
                }
            }
            if (response == 3) {
                break;
            }
        }
    }

    public void menuMainEmployee(Scanner sc) {
        while (true) {
            System.out.println("*** FRS Management Application ***");
            System.out.println("You are login as " + currentEmployee.getName() + " with " + currentEmployee.getRole().toString() + " rights");
            System.out.println("1: Logout");
            int response = 0;

            while (response < 1 || response > 1) {
                System.out.print("> ");
                response = sc.nextInt();

                if (response == 1) {
                    break;
                } else {
                    System.out.println("Invalid response!");
                }
            }
            if (response == 1) {
                break;
            }
        }
    }
}
