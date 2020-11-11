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
import entity.AircraftConfigurationEntity;
import entity.AircraftTypeEntity;
import entity.CabinClassConfigurationEntity;
import entity.EmployeeEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import util.enumeration.CabinClassTypeEnum;
import util.enumeration.UserRoleEnum;
import util.exception.AircraftTypeNotFoundException;
import util.exception.InvalidLoginCredentialsException;
import util.exception.InvalidUsernameException;
import util.exception.WrongPasswordException;
import ejb.session.stateless.AircraftConfigurationEntitySessionBeanRemote;
import ejb.session.stateless.FlightEntitySessionBeanRemote;
import ejb.session.stateless.FlightRouteEntitySessionBeanRemote;
import ejb.session.stateless.FlightScheduleEntitySessionBeanRemote;
import ejb.session.stateless.FlightSchedulePlanEntitySessionBeanRemote;
import ejb.session.stateless.ReservationEntitySessionBeanRemote;
import ejb.session.stateless.SeatsInventoryEntitySessionBeanRemote;
import entity.AirportEntity;
import entity.FlightEntity;
import entity.FlightRouteEntity;
import entity.FlightScheduleEntity;
import entity.FlightSchedulePlanEntity;
import entity.ReservationEntity;
import entity.SeatsInventoryEntity;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.AirportNotFoundException;
import util.exception.FlightNotFoundException;
import util.exception.FlightRouteNotFoundException;
import util.exception.FlightScheduleNotFoundException;

/**
 *
 * @author miche
 */
public class MainApp {

    private EmployeeEntity currentEmployee;

    private EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote;

    private PartnerEntitySessionBeanRemote partnerEntitySessionBeanRemote;

    private AirportEntitySessionBeanRemote airportEntitySessionBeanRemote;

    private AircraftTypeEntitySessionBeanRemote aircraftTypeEntitySessionBeanRemote;

    private CabinClassConfigurationSessionBeanRemote cabinClassConfigurationSessionBeanRemote;

    private AircraftConfigurationEntitySessionBeanRemote aircraftConfigurationEntitySessionBeanRemote;

    private FlightEntitySessionBeanRemote flightEntitySessionBeanRemote;

    private FlightRouteEntitySessionBeanRemote flightRouteEntitySessionBeanRemote;

    private FlightScheduleEntitySessionBeanRemote flightScheduleEntitySessionBeanRemote;

    private FlightSchedulePlanEntitySessionBeanRemote flightSchedulePlanEntitySessionBeanRemote;

    private FlightOperationModule flightOperationModule;

    private ReservationEntitySessionBeanRemote reservationEntitySessionBeanRemote;
    
    private SeatsInventoryEntitySessionBeanRemote seatsInventoryEntitySessionBeanRemote;

    public MainApp() {
    }

    public MainApp(PartnerEntitySessionBeanRemote partnerEntitySessionBeanRemote,
            EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote,
            AirportEntitySessionBeanRemote airportEntitySessionBeanRemote,
            AircraftTypeEntitySessionBeanRemote aircraftTypeEntitySessionBeanRemote,
            CabinClassConfigurationSessionBeanRemote cabinClassConfigurationSessionBeanRemote,
            AircraftConfigurationEntitySessionBeanRemote aircraftConfigurationEntitySessionBeanRemote,
            FlightEntitySessionBeanRemote flightEntitySessionBeanRemote,
            FlightRouteEntitySessionBeanRemote flightRouteEntitySessionBeanRemote,
            FlightScheduleEntitySessionBeanRemote flightScheduleEntitySessionBeanRemote,
            FlightSchedulePlanEntitySessionBeanRemote flightSchedulePlanEntitySessionBeanRemote,
            ReservationEntitySessionBeanRemote reservationEntitySessionBeanRemote,
            SeatsInventoryEntitySessionBeanRemote seatsInventoryEntitySessionBeanRemote) {
        this.partnerEntitySessionBeanRemote = partnerEntitySessionBeanRemote;
        this.employeeEntitySessionBeanRemote = employeeEntitySessionBeanRemote;
        this.airportEntitySessionBeanRemote = airportEntitySessionBeanRemote;
        this.aircraftTypeEntitySessionBeanRemote = aircraftTypeEntitySessionBeanRemote;
        this.cabinClassConfigurationSessionBeanRemote = cabinClassConfigurationSessionBeanRemote;
        this.aircraftConfigurationEntitySessionBeanRemote = aircraftConfigurationEntitySessionBeanRemote;
        this.flightEntitySessionBeanRemote = flightEntitySessionBeanRemote;
        this.flightRouteEntitySessionBeanRemote = flightRouteEntitySessionBeanRemote;
        this.flightScheduleEntitySessionBeanRemote = flightScheduleEntitySessionBeanRemote;
        this.flightSchedulePlanEntitySessionBeanRemote = flightSchedulePlanEntitySessionBeanRemote;
        this.reservationEntitySessionBeanRemote = reservationEntitySessionBeanRemote;
        this.seatsInventoryEntitySessionBeanRemote = seatsInventoryEntitySessionBeanRemote;
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
                    } catch (InvalidUsernameException | WrongPasswordException | InvalidLoginCredentialsException ex) {
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

    public void doLogin(Scanner sc) throws InvalidUsernameException, WrongPasswordException, InvalidLoginCredentialsException {
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
            throw new InvalidLoginCredentialsException("Missing login credential!");
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
            System.out.println("You are logged in as " + currentEmployee.getName() + " with Fleet Manager rights");
            System.out.println("1: Create Aircraft Configuration");
            System.out.println("2: View All Aircraft Configurations");
            System.out.println("3: View Aircraft Configuration Details");
            System.out.println("4: Logout");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");
                response = sc.nextInt();

                if (response == 1) {
                    System.out.println("*** FRS Fleet Manager :: Create New Aircraft Configuration ***");
                    sc.nextLine();
                    System.out.println("Here are the available lists of aircraft types:");
                    List<AircraftTypeEntity> types = aircraftTypeEntitySessionBeanRemote.retrieveAllTypes();
                    for (AircraftTypeEntity a : types) {
                        System.out.println("- " + a.getName());
                    }
                    System.out.print("Enter aircraft type> ");
                    String type = sc.nextLine();
                    AircraftTypeEntity aircraftType = null;
                    try {
                        aircraftType = aircraftTypeEntitySessionBeanRemote.retrieveAircraftTypeByName(type);
                    } catch (AircraftTypeNotFoundException ex) {
                        System.out.println(ex.getMessage());
                        break;
                    }
                    System.out.print("Enter aircraft configuration code> ");
                    String code = sc.nextLine();
                    System.out.print("Enter aircraft configuration name> ");
                    String name = sc.nextLine();
                    int numOfClasses = 0;
                    while (numOfClasses < 1 || numOfClasses > 4) {
                        System.out.print("Enter number of cabin classes (1-4)> ");
                        numOfClasses = sc.nextInt();
                        if (numOfClasses < 1 || numOfClasses > 4) {
                            System.out.println("Please enter a number from 1-4!");
                        }
                    }
                    int totalMaxCapacity = 0;
                    List<CabinClassConfigurationEntity> classes = new ArrayList<CabinClassConfigurationEntity>();
                    System.out.println("*** The Maximum Capacity for this aircraft is: " + aircraftType.getMaxCapacity() + " ***");

                    System.out.println("Enter " + numOfClasses + " cabin classes based on the numbers above:");
                    for (int i = 1; i <= numOfClasses; i++) {
                        System.out.println("Here are the list of cabin class types:");
                        System.out.println("1: First Class");
                        System.out.println("2: Business Class");
                        System.out.println("3: Premium Economy Class");
                        System.out.println("4: Economy Class");
                        int response2 = 0;
                        CabinClassTypeEnum classType = CabinClassTypeEnum.FIRST_CLASS;
                        while (response2 < 1 || response2 > 4) {
                            System.out.print(i + ". Enter type of cabin class > ");
                            response2 = sc.nextInt();
                            switch (response2) {
                                case 1:
                                    classType = CabinClassTypeEnum.FIRST_CLASS;
                                    break;
                                case 2:
                                    classType = CabinClassTypeEnum.BUSINESS_CLASS;
                                    break;
                                case 3:
                                    classType = CabinClassTypeEnum.PREMIUM_ECONOMY_CLASS;
                                    break;
                                case 4:
                                    classType = CabinClassTypeEnum.ECONOMY_CLASS;
                                    break;
                                default:
                                    System.out.println("Please enter numbers from 1-4!");
                                    break;
                            }

                            if (response2 < 1 || response2 > 4) {
                                continue;
                            }
                            System.out.println("***Create Cabin Class Configuration***");
                            System.out.print("Enter number of aisles> ");
                            int aisles = sc.nextInt();
                            System.out.print("Enter number of rows> ");
                            int rows = sc.nextInt();
                            switch (aisles) {
                                case 0:
                                    System.out.println("Enter the seating configuration in an 'A' format. e.g. '3'");
                                    break;
                                case 1:
                                    System.out.println("Enter the seating configuration in an 'A-B' format. e.g. '2-2'");
                                    break;
                                case 2:
                                    System.out.println("Enter the seating configuration in an 'A-B-C' format. e.g. '3-4-3'");
                                    break;
                                case 3:
                                    System.out.println("Enter the seating configuration in an 'A-B-C-D' format. e.g. '3-3-3-3'");
                                    break;
                                default:
                                    break;
                            }
                            sc.nextLine();
                            System.out.print("> ");
                            String configuration = sc.nextLine();
                            String[] strArr = configuration.split("-", 0);

                            int[] arrOfConfig = Stream.of(strArr).mapToInt(Integer::valueOf).toArray();
                            int abreast = 0;
                            for (int j = 0; j < arrOfConfig.length; j++) {
                                abreast += arrOfConfig[j];
                            }

                            int maxCapacity = abreast * rows;

                            classes.add(new CabinClassConfigurationEntity(aisles, rows, abreast, maxCapacity, classType, arrOfConfig));
                            totalMaxCapacity += maxCapacity;
                        }
                    }

                    if (totalMaxCapacity > aircraftType.getMaxCapacity()) {
                        System.out.println("The total maximum capacity for all cabin classes exceeds the aircraft type's maximum seat capacity");
                    } else {
                        AircraftConfigurationEntity newAircraftConfiguration = aircraftConfigurationEntitySessionBeanRemote.createAircraftConfiguration(
                                new AircraftConfigurationEntity(code, name, numOfClasses, aircraftType, classes, totalMaxCapacity));
                        aircraftTypeEntitySessionBeanRemote.addAircraftConfiguration(aircraftType, newAircraftConfiguration);
                        System.out.println("An Aircraft Configuration has been successfully created!");
                    }
                } else if (response == 2) {
                    System.out.println("*** FRS Fleet Manager :: View All Aircraft Configurations ***");
                    sc.nextLine();
                    List<AircraftConfigurationEntity> configurations
                            = aircraftConfigurationEntitySessionBeanRemote.retrieveAllAircraftConfigurations();
                    for (int i = 1; i <= configurations.size(); i++) {
                        System.out.print(i + ". ");
                        System.out.println("CODE: " + configurations.get(i - 1).getCode());
                        System.out.println("   NAME: " + configurations.get(i - 1).getName());
                        System.out.println("   AIRCRAFT TYPE: " + configurations.get(i - 1).getName());
                        System.out.println("   NUMBER OF CABIN CLASSES: " + configurations.get(i - 1).getNumCabinClass());
                        System.out.println("   MAXIMUM CAPACITY: " + configurations.get(i - 1).getMaxCapacity());
                    }
                } else if (response == 3) {
                    System.out.println("*** FRS Fleet Manager :: View Aircraft Configuration Details ***");
                    sc.nextLine();
                    System.out.print("Enter aircraft configuration code> ");
                    String code = sc.nextLine();
                    AircraftConfigurationEntity aircraftConfig = null;
                    try {
                        aircraftConfig = aircraftConfigurationEntitySessionBeanRemote.retrieveAircraftTypeByCode(code);
                    } catch (AircraftConfigurationNotFoundException ex) {
                        System.out.println(ex.getMessage());
                        break;
                    }
                    List<FlightEntity> flights = aircraftConfigurationEntitySessionBeanRemote.getFlightEntities(aircraftConfig);
                    List<CabinClassConfigurationEntity> classes = aircraftConfigurationEntitySessionBeanRemote.getCabinClassConfig(aircraftConfig);

                    System.out.println("*** Aircraft Configuration Details of " + code + " ***");
                    System.out.println("- CODE: " + aircraftConfig.getCode());
                    System.out.println("- NAME: " + aircraftConfig.getName());
                    System.out.println("- AIRCRAFT TYPE: " + aircraftConfig.getType().getName());
                    System.out.println("- NUMBER OF CABIN CLASSES: " + aircraftConfig.getNumCabinClass());
                    System.out.println("- TOTAL MAXIMUM CAPACITY: " + aircraftConfig.getMaxCapacity());

                    System.out.println("- Cabin Classes: ");
                    for (CabinClassConfigurationEntity c : classes) {
                        System.out.println("   " + searchCabinType(c.getType()));
                        System.out.println("   - Number of aisles: " + c.getNumAisle());
                        System.out.println("   - Number of rows: " + c.getNumRow());
                        System.out.println("   - Number of seats abreast: " + c.getNumSeatAbreast());
                        System.out.println("   - Maximum Capacity: " + c.getMaxCapacity());
                        System.out.println();
                    }

                    System.out.print("- FLIGHTS (Flight Code): ");
                    if (flights.isEmpty()) {
                        System.out.println("No flights registered for this aircraft configuration");
                    } else {
                        System.out.println();
                        for (FlightEntity f : flights) {
                            System.out.println("   " + f.getFlightCode());
                        }
                    }
                    System.out.println("******END OF AIRCRAFT CONFIGURATION DETAILS******");
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid response!");
                }
            }
            if (response == 4) {
                break;
            }
        }
    }

    public void menuMainRoute(Scanner sc) {
        Integer response = 0;

        while (true) {
            System.out.println("*** FRS Management Application ***");
            System.out.println("You are logged in as " + currentEmployee.getName() + " with Route Planner rights");
            System.out.println("1: Create Flight Route");
            System.out.println("2: View All Flight Routes");
            System.out.println("3: Delete Flight Route");
            System.out.println("4: Logout");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");
                response = sc.nextInt();

                if (response == 1) {
                    System.out.println("*** FRS Route Planner :: Create Flight Route ***");
                    sc.nextLine();
                    System.out.print("Enter origin airport> ");
                    String origin = sc.nextLine();
                    AirportEntity originAirport = null;
                    try {
                        originAirport = airportEntitySessionBeanRemote.retrieveAirportByCode(origin);
                    } catch (AirportNotFoundException ex) {
                        System.out.println(ex.getMessage());
                        break;
                    }

                    System.out.print("Enter destination airport> ");
                    String destination = sc.nextLine();
                    AirportEntity destinationAirport = null;
                    try {
                        destinationAirport = airportEntitySessionBeanRemote.retrieveAirportByCode(destination);
                    } catch (AirportNotFoundException ex) {
                        System.out.println(ex.getMessage());
                        break;
                    }

                    FlightRouteEntity departureRoute = flightRouteEntitySessionBeanRemote.createFlightRouteEntity(new FlightRouteEntity(originAirport, destinationAirport));
                    airportEntitySessionBeanRemote.addDepartureRoute(originAirport, departureRoute);
                    airportEntitySessionBeanRemote.addArrivalRoute(destinationAirport, departureRoute);

                    String returnRouteConfirmation = "A";
                    while (!returnRouteConfirmation.equals("Y") && !returnRouteConfirmation.equals("N")) {
                        System.out.println("Do you want to create a complementary return route? (Y/N)> ");
                        returnRouteConfirmation = sc.nextLine();
                    }

                    if (returnRouteConfirmation.equals("Y")) {
                        FlightRouteEntity returnRoute = flightRouteEntitySessionBeanRemote.createFlightRouteEntity(new FlightRouteEntity(destinationAirport, originAirport));
                        flightRouteEntitySessionBeanRemote.setReturnFlightRoute(departureRoute, returnRoute);
                        flightRouteEntitySessionBeanRemote.setDepartureFlightRoute(departureRoute, returnRoute);
                        airportEntitySessionBeanRemote.addDepartureRoute(destinationAirport, departureRoute);
                        airportEntitySessionBeanRemote.addArrivalRoute(originAirport, departureRoute);
                        System.out.println("Return flight from " + destination + " to " + origin + " has been created!");
                    }
                } else if (response == 2) {
                    System.out.println("*** FRS Route Planner :: View All Flight Routes ***");
                    sc.nextLine();
                    List<FlightRouteEntity> routes
                            = flightRouteEntitySessionBeanRemote.retrieveAllRoutesNotReturn();
                    for (int i = 1; i <= routes.size(); i++) {
                        System.out.print(i + ". ");
                        System.out.println("ROUTE: " + routes.get(i - 1).getOriginAirport().getAirportCode() + " - "
                                + routes.get(i - 1).getDestinationAirport().getAirportCode());
                        List<FlightEntity> flights = flightRouteEntitySessionBeanRemote.retrieveAllFlights(routes.get(i - 1));
                        System.out.print("   \t DISABLED: ");
                        if (routes.get(i - 1).isDisabled()) {
                            System.out.println("TRUE");
                        } else {
                            System.out.println("FALSE");
                        }
                        System.out.print("   \t FLIGHTS (FLIGHT CODE): ");
                        if (flights.isEmpty()) {
                            System.out.println("No available flights for this route");
                        } else {
                            System.out.println();
                            for (FlightEntity f : flights) {
                                System.out.println("    \t\t - " + f.getFlightCode());
                            }
                        }
                        if (routes.get(i - 1).getReturnFlightRoute() != null) {
                            FlightRouteEntity returnRoute = routes.get(i - 1).getReturnFlightRoute();
                            System.out.println("   RETURN ROUTE: " + returnRoute.getOriginAirport().getAirportCode() + " - " + returnRoute.getDestinationAirport().getAirportCode());
                            flights = flightRouteEntitySessionBeanRemote.retrieveAllFlights(returnRoute);
                            System.out.print("   \t DISABLED: ");
                            if (returnRoute.isDisabled()) {
                                System.out.println("TRUE");
                            } else {
                                System.out.println("FALSE");
                            }
                            System.out.print("   \t FLIGHTS (FLIGHT CODE): ");
                            if (flights.isEmpty()) {
                                System.out.println("No available flights for this return route");
                            } else {
                                System.out.println();
                                for (FlightEntity f : flights) {
                                    System.out.println("    \t\t - " + f.getFlightCode());
                                }
                            }
                        }
                    }
                } else if (response == 3) {
                    System.out.println("*** FRS Route Planner :: Delete Flight Route ***");
                    sc.nextLine();
                    System.out.println("Enter flight route's origin airport code> ");
                    String originAirportCode = sc.nextLine();
                    System.out.println("Enter flight route's destination airport code> ");
                    String destinationAirportCode = sc.nextLine();

                    AirportEntity originAirport = null;
                    try {
                        originAirport = airportEntitySessionBeanRemote.retrieveAirportByCode(originAirportCode);
                    } catch (AirportNotFoundException ex) {
                        System.out.println(ex.getMessage());
                        break;
                    }

                    AirportEntity destinationAirport = null;
                    try {
                        destinationAirport = airportEntitySessionBeanRemote.retrieveAirportByCode(destinationAirportCode);
                    } catch (AirportNotFoundException ex) {
                        System.out.println(ex.getMessage());
                        break;
                    }

                    FlightRouteEntity route;
                    try {
                        route = flightRouteEntitySessionBeanRemote.retrieveRouteByAirport(originAirport, destinationAirport);
                    } catch (FlightRouteNotFoundException ex) {
                        System.out.println(ex.getMessage());
                        break;
                    }

                    if (flightRouteEntitySessionBeanRemote.retrieveAllFlights(route).isEmpty()) {
                        System.out.println("1");
                        flightRouteEntitySessionBeanRemote.deleteRoute(route);
                        System.out.println("Route successfully deleted");
                    } else {
                        System.out.println("2");

                        flightRouteEntitySessionBeanRemote.disable(route);
                        System.out.println("Route successfully disabled");
                    }
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid response!");
                }
            }
            if (response == 4) {
                break;
            }
        }
    }

    public void menuMainSchedule(Scanner sc) {
        Integer response = 0;

        flightOperationModule
                = new FlightOperationModule(airportEntitySessionBeanRemote, aircraftTypeEntitySessionBeanRemote,
                        cabinClassConfigurationSessionBeanRemote, aircraftConfigurationEntitySessionBeanRemote,
                        flightEntitySessionBeanRemote, flightRouteEntitySessionBeanRemote,
                        flightScheduleEntitySessionBeanRemote, flightSchedulePlanEntitySessionBeanRemote);

        while (true) {
            System.out.println("*** FRS Management Application ***");
            System.out.println("You are logged in as " + currentEmployee.getName() + " with Schedule Manager rights");
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
                    flightOperationModule.createFlight(sc);
                } else if (response == 2) {
                    flightOperationModule.viewAllFlights(sc);
                } else if (response == 3) {
                    flightOperationModule.viewFlightDetails(sc);
                } else if (response == 4) {
                    flightOperationModule.createFlightSchedulePlan(sc);
                } else if (response == 5) {

                } else if (response == 6) {

                } else if (response == 7) {
                    break;
                } else {
                    System.out.println("Invalid response!");
                }
            }
            if (response == 7) {
                break;
            }
        }
    }

    public void menuMainSales(Scanner sc) {
        Integer response = 0;

        while (true) {
            System.out.println("*** FRS Management Application ***");
            System.out.println("You are logged in as " + currentEmployee.getName() + " with Sales Manager rights");
            System.out.println("1: View Seats Inventory");
            System.out.println("2: View Flight Reservations");
            System.out.println("3: Logout");
            response = 0;

            while (response < 1 || response > 3) {
                System.out.print("> ");
                response = sc.nextInt();

                if (response == 1) {
                    System.out.println("*** FRS Schedule Manager :: View Seats Inventory ***");
                    sc.nextLine();
                    System.out.print("Enter the flight code> ");
                    String flightNumber = sc.nextLine();
                    FlightEntity flight = null;
                    try {
                        flight = flightEntitySessionBeanRemote.retrieveFlightByCode(flightNumber);
                    } catch (FlightNotFoundException ex) {
                        System.out.println(ex.getMessage());
                        break;
                    }
                    System.out.println("List of available flight schedules ID for flight " + flightNumber + " :");
                    
                    List<FlightSchedulePlanEntity> plans = flightEntitySessionBeanRemote.retrieveSchedulePlans(flight);
                    if (plans.isEmpty()) {
                        System.out.println("No available flight schedules for flight " + flightNumber + "!");
                        break;
                    }

                    for (FlightSchedulePlanEntity plan : plans) {
                        plan.getFlightSchedules().size();
                        List<FlightScheduleEntity> schedules = plan.getFlightSchedules();
                        for (FlightScheduleEntity schedule : schedules) {
                            System.out.println("Schedule ID: " + schedule.getScheduleId());
                        }
                    }
                    System.out.println();
                    System.out.print("Enter Schedule ID to view seats inventory> ");
                    Long scheduleId = sc.nextLong();
                    List<SeatsInventoryEntity> seats;
                    try {
                        seats = seatsInventoryEntitySessionBeanRemote.retrieveSeatsInventoryByScheduleId(scheduleId);
                    } catch (FlightScheduleNotFoundException ex) {
                        System.out.println(ex.getMessage());
                        break;
                    }
                    System.out.println("Seats Inventory for schedule ID " + scheduleId + " for flight " + flightNumber);
                    System.out.println();
                    long totalAvailableSeats = 0;
                    long totalReservedSeats = 0;
                    long totalBalancedSeats = 0;
                    for (SeatsInventoryEntity seat : seats) {
                        System.out.println("Cabin class type: " + searchCabinType(seat.getCabinClass().getType()));
                        System.out.println("    Available Seats: " + seat.getAvailableSeatsSize());
                        System.out.println("    Reserved Seats: " + seat.getReservedSeatsSize());
                        System.out.println("    Balance Seats: " + seat.getBalanceSeatsSize());
                        totalAvailableSeats += seat.getAvailableSeatsSize();
                        totalReservedSeats += seat.getReservedSeatsSize();
                        totalBalancedSeats += seat.getBalanceSeatsSize();
                        System.out.println();
                    }
                    System.out.println();
                    System.out.println("TOTAL Available seats: " + totalAvailableSeats);
                    System.out.println("TOTAL Reserved seats: " + totalReservedSeats);
                    System.out.println("TOTAL Balance seats: " + totalBalancedSeats);
                } else if (response == 2) {
                    System.out.println("*** FRS Schedule Manager :: View Flight Reservations ***");
                    sc.nextLine();
                    System.out.print("Enter the flight code> ");
                    String flightNumber = sc.nextLine();
                    FlightEntity flight = null;
                    try {
                        flight = flightEntitySessionBeanRemote.retrieveFlightByCode(flightNumber);
                    } catch (FlightNotFoundException ex) {
                        System.out.println(ex.getMessage());
                        break;
                    }
                    System.out.println("List of available flight schedules ID for flight " + flightNumber + " :");
                    
                    List<FlightSchedulePlanEntity> plans = flightEntitySessionBeanRemote.retrieveSchedulePlans(flight);
                    if (plans.isEmpty()) {
                        System.out.println("No available flight schedules for flight " + flightNumber + "!");
                        break;
                    }

                    for (FlightSchedulePlanEntity plan : plans) {
                        plan.getFlightSchedules().size();
                        List<FlightScheduleEntity> schedules = plan.getFlightSchedules();
                        for (FlightScheduleEntity schedule : schedules) {
                            System.out.println("Schedule ID: " + schedule.getScheduleId());
                        }
                    }
                    System.out.println();
                    System.out.print("Enter Schedule ID to view seats inventory> ");
                    Long scheduleId = sc.nextLong();

                    List<ReservationEntity> firstClassReservations
                            = reservationEntitySessionBeanRemote.retrieveReservationsByScheduleIdFirstClass(scheduleId);

                    List<ReservationEntity> businessClassReservations
                            = reservationEntitySessionBeanRemote.retrieveReservationsByScheduleIdBusinessClass(scheduleId);

                    List<ReservationEntity> premiumClassReservations
                            = reservationEntitySessionBeanRemote.retrieveReservationsByScheduleIdPremiumClass(scheduleId);

                    List<ReservationEntity> economyClassReservations
                            = reservationEntitySessionBeanRemote.retrieveReservationsByScheduleIdEconomyClass(scheduleId);

                    if (firstClassReservations.isEmpty() && businessClassReservations.isEmpty() && premiumClassReservations.isEmpty()
                            && economyClassReservations.isEmpty()) {
                        System.out.println("There are no reservations for this schedule!");
                        break;
                    }

                    if (!firstClassReservations.isEmpty()) {
                        System.out.println("*** FIRST CLASS RESERVATIONS: ***");
                        for (ReservationEntity reservation : firstClassReservations) {
                            System.out.println("Passenger name: " + reservation.getPassengerName());
                            System.out.println("Seat number: " + reservation.getSeatNumber());
                            System.out.println("Fare Basis Code: " + reservation.getFareBasisCode());
                            System.out.println();
                        }
                    }

                    if (!businessClassReservations.isEmpty()) {
                        System.out.println("*** BUSINESS CLASS RESERVATIONS: ***");
                        for (ReservationEntity reservation : businessClassReservations) {
                            System.out.println("Passenger name: " + reservation.getPassengerName());
                            System.out.println("Seat number: " + reservation.getSeatNumber());
                            System.out.println("Fare Basis Code: " + reservation.getFareBasisCode());
                            System.out.println();
                        }
                    }

                    if (!premiumClassReservations.isEmpty()) {
                        System.out.println("*** PREMIUM ECONOMY CLASS RESERVATIONS: ***");
                        for (ReservationEntity reservation : premiumClassReservations) {
                            System.out.println("Passenger name: " + reservation.getPassengerName());
                            System.out.println("Seat number: " + reservation.getSeatNumber());
                            System.out.println("Fare Basis Code: " + reservation.getFareBasisCode());
                            System.out.println();
                        }
                    }

                    if (!economyClassReservations.isEmpty()) {
                        System.out.println("*** ECONOMY CLASS RESERVATIONS: ***");
                        for (ReservationEntity reservation : economyClassReservations) {
                            System.out.println("Passenger name: " + reservation.getPassengerName());
                            System.out.println("Seat number: " + reservation.getSeatNumber());
                            System.out.println("Fare Basis Code: " + reservation.getFareBasisCode());
                            System.out.println();
                        }
                    }
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
            System.out.println("You are logged in as " + currentEmployee.getName() + " with " + currentEmployee.getRole().toString() + " rights");
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

    public String searchCabinType(CabinClassTypeEnum type) {
        if (type.equals(CabinClassTypeEnum.FIRST_CLASS)) {
            return "First Class";
        } else if (type.equals(CabinClassTypeEnum.BUSINESS_CLASS)) {
            return "Business Class";
        } else if (type.equals(CabinClassTypeEnum.PREMIUM_ECONOMY_CLASS)) {
            return "Premium Economy Class";
        } else if (type.equals(CabinClassTypeEnum.ECONOMY_CLASS)) {
            return "Economy Class";
        }
        return "invalid";
    }
}
