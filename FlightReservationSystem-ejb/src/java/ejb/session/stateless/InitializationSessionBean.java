/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftConfigurationEntity;
import entity.AircraftTypeEntity;
import entity.AirportEntity;
import entity.CabinClassConfigurationEntity;
import entity.FareEntity;
import entity.FlightEntity;
import entity.FlightRouteEntity;
import entity.FlightScheduleEntity;
import entity.FlightSchedulePlanEntity;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.CabinClassTypeEnum;
import util.enumeration.FlightSchedulePlanTypeEnum;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.AircraftTypeNotFoundException;
import util.exception.AirportNotFoundException;
import util.exception.FlightNotFoundException;
import util.exception.FlightRouteNotFoundException;

/**
 *
 * @author Chrisya
 */
@Stateless
public class InitializationSessionBean implements InitializationSessionBeanLocal, InitializationSessionBeanRemote {

    @EJB
    private FareEntitySessionBeanLocal fareEntitySessionBeanLocal;

    @EJB
    private FlightSchedulePlanEntitySessionBeanLocal flightSchedulePlanEntitySessionBeanLocal;

    @EJB
    private FlightScheduleEntitySessionBeanLocal flightScheduleEntitySessionBeanLocal;

    @EJB
    private FlightEntitySessionBeanLocal flightEntitySessionBeanLocal;

    @EJB
    private FlightRouteEntitySessionBeanLocal flightRouteEntitySessionBeanLocal;

    @EJB
    private AirportEntitySessionBeanLocal airportEntitySessionBeanLocal;

    @EJB
    private aircraftConfigurationEntitySessionBeanLocal aircraftConfigurationEntitySessionBeanLocal;

    @EJB
    private AircraftTypeEntitySessionBeanLocal aircraftTypeEntitySessionBeanLocal;

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;

    @Override
    public void initializeAirCraftConfiguration() {

        List<CabinClassConfigurationEntity> classes = new ArrayList<CabinClassConfigurationEntity>();

        //testdata1
        int[] config = {3, 3};
        AircraftTypeEntity aircraftType = null;
        try {
            aircraftType = aircraftTypeEntitySessionBeanLocal.retrieveAircraftTypeByName("Boeing 737");
        } catch (AircraftTypeNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        classes.add(new CabinClassConfigurationEntity(1, 30, 6, 180, CabinClassTypeEnum.ECONOMY_CLASS, config));

        AircraftConfigurationEntity newAircraftConfiguration = aircraftConfigurationEntitySessionBeanLocal.createAircraftConfiguration(
                new AircraftConfigurationEntity("B737AE", "Boeing 737 All Economy", 1, aircraftType, classes, 180));

        aircraftTypeEntitySessionBeanLocal.addAircraftConfiguration(aircraftType, newAircraftConfiguration);

        classes.clear();

        //testdata2
        try {
            aircraftType = aircraftTypeEntitySessionBeanLocal.retrieveAircraftTypeByName("Boeing 737");
        } catch (AircraftTypeNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        config = new int[]{1, 1};
        classes.add(new CabinClassConfigurationEntity(1, 5, 2, 10, CabinClassTypeEnum.FIRST_CLASS, config));

        config = new int[]{2, 2};
        classes.add(new CabinClassConfigurationEntity(1, 5, 4, 20, CabinClassTypeEnum.BUSINESS_CLASS, config));

        config = new int[]{3, 3};
        classes.add(new CabinClassConfigurationEntity(1, 25, 6, 150, CabinClassTypeEnum.ECONOMY_CLASS, config));

        newAircraftConfiguration = aircraftConfigurationEntitySessionBeanLocal.createAircraftConfiguration(
                new AircraftConfigurationEntity("B737TC", "Boeing 737 Three Classes", 3, aircraftType, classes, 180));

        aircraftTypeEntitySessionBeanLocal.addAircraftConfiguration(aircraftType, newAircraftConfiguration);

        classes.clear();

        //testdata3
        try {
            aircraftType = aircraftTypeEntitySessionBeanLocal.retrieveAircraftTypeByName("Boeing 747");
        } catch (AircraftTypeNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        config = new int[]{3, 4, 3};
        classes.add(new CabinClassConfigurationEntity(2, 38, 10, 380, CabinClassTypeEnum.ECONOMY_CLASS, config));

        newAircraftConfiguration = aircraftConfigurationEntitySessionBeanLocal.createAircraftConfiguration(
                new AircraftConfigurationEntity("B747AE", "Boeing 747 All Economy", 1, aircraftType, classes, 380));

        aircraftTypeEntitySessionBeanLocal.addAircraftConfiguration(aircraftType, newAircraftConfiguration);

        classes.clear();

        //testdata4
        try {
            aircraftType = aircraftTypeEntitySessionBeanLocal.retrieveAircraftTypeByName("Boeing 747");
        } catch (AircraftTypeNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        config = new int[]{1, 1};
        classes.add(new CabinClassConfigurationEntity(1, 5, 2, 10, CabinClassTypeEnum.FIRST_CLASS, config));

        config = new int[]{2, 2, 2};
        classes.add(new CabinClassConfigurationEntity(2, 5, 6, 30, CabinClassTypeEnum.BUSINESS_CLASS, config));

        config = new int[]{3, 4, 3};
        classes.add(new CabinClassConfigurationEntity(2, 32, 10, 320, CabinClassTypeEnum.ECONOMY_CLASS, config));

        newAircraftConfiguration = aircraftConfigurationEntitySessionBeanLocal.createAircraftConfiguration(
                new AircraftConfigurationEntity("B747TC", "Boeing 747 Three Classes", 3, aircraftType, classes, 360));

        aircraftTypeEntitySessionBeanLocal.addAircraftConfiguration(aircraftType, newAircraftConfiguration);

        classes.clear();
    }

    @Override
    public void initializeFlightRoute() {

        //testdata1
        AirportEntity originAirport = null;
        try {
            originAirport = airportEntitySessionBeanLocal.retrieveAirportByCode("SIN");
        } catch (AirportNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        AirportEntity destinationAirport = null;
        try {
            destinationAirport = destinationAirport = airportEntitySessionBeanLocal.retrieveAirportByCode("HKG");
        } catch (AirportNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        FlightRouteEntity departureRoute = flightRouteEntitySessionBeanLocal.createFlightRouteEntity(new FlightRouteEntity(originAirport, destinationAirport));
        airportEntitySessionBeanLocal.addDepartureRoute(originAirport, departureRoute);
        airportEntitySessionBeanLocal.addArrivalRoute(destinationAirport, departureRoute);

        FlightRouteEntity returnRoute = flightRouteEntitySessionBeanLocal.createFlightRouteEntity(new FlightRouteEntity(destinationAirport, originAirport));
        flightRouteEntitySessionBeanLocal.setReturnFlightRoute(departureRoute, returnRoute);
        flightRouteEntitySessionBeanLocal.setDepartureFlightRoute(departureRoute, returnRoute);
        airportEntitySessionBeanLocal.addDepartureRoute(destinationAirport, departureRoute);
        airportEntitySessionBeanLocal.addArrivalRoute(originAirport, departureRoute);

        //testdata2
        try {
            originAirport = airportEntitySessionBeanLocal.retrieveAirportByCode("SIN");
        } catch (AirportNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            destinationAirport = destinationAirport = airportEntitySessionBeanLocal.retrieveAirportByCode("TPE");
        } catch (AirportNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        departureRoute = flightRouteEntitySessionBeanLocal.createFlightRouteEntity(new FlightRouteEntity(originAirport, destinationAirport));
        airportEntitySessionBeanLocal.addDepartureRoute(originAirport, departureRoute);
        airportEntitySessionBeanLocal.addArrivalRoute(destinationAirport, departureRoute);

        returnRoute = flightRouteEntitySessionBeanLocal.createFlightRouteEntity(new FlightRouteEntity(destinationAirport, originAirport));
        flightRouteEntitySessionBeanLocal.setReturnFlightRoute(departureRoute, returnRoute);
        flightRouteEntitySessionBeanLocal.setDepartureFlightRoute(departureRoute, returnRoute);
        airportEntitySessionBeanLocal.addDepartureRoute(destinationAirport, departureRoute);
        airportEntitySessionBeanLocal.addArrivalRoute(originAirport, departureRoute);

        //testdata3
        try {
            originAirport = airportEntitySessionBeanLocal.retrieveAirportByCode("SIN");
        } catch (AirportNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            destinationAirport = destinationAirport = airportEntitySessionBeanLocal.retrieveAirportByCode("NRT");
        } catch (AirportNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        departureRoute = flightRouteEntitySessionBeanLocal.createFlightRouteEntity(new FlightRouteEntity(originAirport, destinationAirport));
        airportEntitySessionBeanLocal.addDepartureRoute(originAirport, departureRoute);
        airportEntitySessionBeanLocal.addArrivalRoute(destinationAirport, departureRoute);

        returnRoute = flightRouteEntitySessionBeanLocal.createFlightRouteEntity(new FlightRouteEntity(destinationAirport, originAirport));
        flightRouteEntitySessionBeanLocal.setReturnFlightRoute(departureRoute, returnRoute);
        flightRouteEntitySessionBeanLocal.setDepartureFlightRoute(departureRoute, returnRoute);
        airportEntitySessionBeanLocal.addDepartureRoute(destinationAirport, departureRoute);
        airportEntitySessionBeanLocal.addArrivalRoute(originAirport, departureRoute);

        //testdata4
        try {
            originAirport = airportEntitySessionBeanLocal.retrieveAirportByCode("HKG");
        } catch (AirportNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            destinationAirport = destinationAirport = airportEntitySessionBeanLocal.retrieveAirportByCode("NRT");
        } catch (AirportNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        departureRoute = flightRouteEntitySessionBeanLocal.createFlightRouteEntity(new FlightRouteEntity(originAirport, destinationAirport));
        airportEntitySessionBeanLocal.addDepartureRoute(originAirport, departureRoute);
        airportEntitySessionBeanLocal.addArrivalRoute(destinationAirport, departureRoute);

        returnRoute = flightRouteEntitySessionBeanLocal.createFlightRouteEntity(new FlightRouteEntity(destinationAirport, originAirport));
        flightRouteEntitySessionBeanLocal.setReturnFlightRoute(departureRoute, returnRoute);
        flightRouteEntitySessionBeanLocal.setDepartureFlightRoute(departureRoute, returnRoute);
        airportEntitySessionBeanLocal.addDepartureRoute(destinationAirport, departureRoute);
        airportEntitySessionBeanLocal.addArrivalRoute(originAirport, departureRoute);

        //testdata5
        try {
            originAirport = airportEntitySessionBeanLocal.retrieveAirportByCode("TPE");
        } catch (AirportNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            destinationAirport = destinationAirport = airportEntitySessionBeanLocal.retrieveAirportByCode("NRT");
        } catch (AirportNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        departureRoute = flightRouteEntitySessionBeanLocal.createFlightRouteEntity(new FlightRouteEntity(originAirport, destinationAirport));
        airportEntitySessionBeanLocal.addDepartureRoute(originAirport, departureRoute);
        airportEntitySessionBeanLocal.addArrivalRoute(destinationAirport, departureRoute);

        returnRoute = flightRouteEntitySessionBeanLocal.createFlightRouteEntity(new FlightRouteEntity(destinationAirport, originAirport));
        flightRouteEntitySessionBeanLocal.setReturnFlightRoute(departureRoute, returnRoute);
        flightRouteEntitySessionBeanLocal.setDepartureFlightRoute(departureRoute, returnRoute);
        airportEntitySessionBeanLocal.addDepartureRoute(destinationAirport, departureRoute);
        airportEntitySessionBeanLocal.addArrivalRoute(originAirport, departureRoute);

        //testdata6
        try {
            originAirport = airportEntitySessionBeanLocal.retrieveAirportByCode("SIN");
        } catch (AirportNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            destinationAirport = destinationAirport = airportEntitySessionBeanLocal.retrieveAirportByCode("SYD");
        } catch (AirportNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        departureRoute = flightRouteEntitySessionBeanLocal.createFlightRouteEntity(new FlightRouteEntity(originAirport, destinationAirport));
        airportEntitySessionBeanLocal.addDepartureRoute(originAirport, departureRoute);
        airportEntitySessionBeanLocal.addArrivalRoute(destinationAirport, departureRoute);

        returnRoute = flightRouteEntitySessionBeanLocal.createFlightRouteEntity(new FlightRouteEntity(destinationAirport, originAirport));
        flightRouteEntitySessionBeanLocal.setReturnFlightRoute(departureRoute, returnRoute);
        flightRouteEntitySessionBeanLocal.setDepartureFlightRoute(departureRoute, returnRoute);
        airportEntitySessionBeanLocal.addDepartureRoute(destinationAirport, departureRoute);
        airportEntitySessionBeanLocal.addArrivalRoute(originAirport, departureRoute);

        //testdata7
        try {
            originAirport = airportEntitySessionBeanLocal.retrieveAirportByCode("SYD");
        } catch (AirportNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            destinationAirport = destinationAirport = airportEntitySessionBeanLocal.retrieveAirportByCode("NRT");
        } catch (AirportNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        departureRoute = flightRouteEntitySessionBeanLocal.createFlightRouteEntity(new FlightRouteEntity(originAirport, destinationAirport));
        airportEntitySessionBeanLocal.addDepartureRoute(originAirport, departureRoute);
        airportEntitySessionBeanLocal.addArrivalRoute(destinationAirport, departureRoute);

        returnRoute = flightRouteEntitySessionBeanLocal.createFlightRouteEntity(new FlightRouteEntity(destinationAirport, originAirport));
        flightRouteEntitySessionBeanLocal.setReturnFlightRoute(departureRoute, returnRoute);
        flightRouteEntitySessionBeanLocal.setDepartureFlightRoute(departureRoute, returnRoute);
        airportEntitySessionBeanLocal.addDepartureRoute(destinationAirport, departureRoute);
        airportEntitySessionBeanLocal.addArrivalRoute(originAirport, departureRoute);
    }

    @Override
    public void initializeFlight() {

        //testdata1
        AirportEntity originAirport = null;
        try {
            originAirport = airportEntitySessionBeanLocal.retrieveAirportByCode("SIN");
        } catch (AirportNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        AirportEntity destinationAirport = null;
        try {
            destinationAirport = airportEntitySessionBeanLocal.retrieveAirportByCode("HKG");
        } catch (AirportNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        FlightRouteEntity route = null;
        try {
            route = flightRouteEntitySessionBeanLocal.retrieveRouteByAirport(originAirport, destinationAirport);
        } catch (FlightRouteNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        AircraftConfigurationEntity aircraftConfiguration = null;
        try {
            aircraftConfiguration = aircraftConfigurationEntitySessionBeanLocal.retrieveAircraftTypeByCode("B737TC");
        } catch (AircraftConfigurationNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        FlightEntity departureFlight = flightEntitySessionBeanLocal.createFlightEntity(new FlightEntity(111, aircraftConfiguration, route));
        FlightRouteEntity returnRoute = route.getReturnFlightRoute();

        FlightEntity returnFlight = flightEntitySessionBeanLocal.createFlightEntity(new FlightEntity(112, aircraftConfiguration, returnRoute));
        flightEntitySessionBeanLocal.setReturnFlight(departureFlight, returnFlight);
        flightEntitySessionBeanLocal.setDepartureFlight(returnFlight, departureFlight);

        //testdata2
        try {
            originAirport = airportEntitySessionBeanLocal.retrieveAirportByCode("SIN");
        } catch (AirportNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            destinationAirport = airportEntitySessionBeanLocal.retrieveAirportByCode("TPE");
        } catch (AirportNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            route = flightRouteEntitySessionBeanLocal.retrieveRouteByAirport(originAirport, destinationAirport);
        } catch (FlightRouteNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            aircraftConfiguration = aircraftConfigurationEntitySessionBeanLocal.retrieveAircraftTypeByCode("B737TC");
        } catch (AircraftConfigurationNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        departureFlight = flightEntitySessionBeanLocal.createFlightEntity(new FlightEntity(211, aircraftConfiguration, route));
        returnRoute = route.getReturnFlightRoute();

        returnFlight = flightEntitySessionBeanLocal.createFlightEntity(new FlightEntity(212, aircraftConfiguration, returnRoute));
        flightEntitySessionBeanLocal.setReturnFlight(departureFlight, returnFlight);
        flightEntitySessionBeanLocal.setDepartureFlight(returnFlight, departureFlight);

        //testdata3
        try {
            originAirport = airportEntitySessionBeanLocal.retrieveAirportByCode("SIN");
        } catch (AirportNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            destinationAirport = airportEntitySessionBeanLocal.retrieveAirportByCode("NRT");
        } catch (AirportNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            route = flightRouteEntitySessionBeanLocal.retrieveRouteByAirport(originAirport, destinationAirport);
        } catch (FlightRouteNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            aircraftConfiguration = aircraftConfigurationEntitySessionBeanLocal.retrieveAircraftTypeByCode("B747TC");
        } catch (AircraftConfigurationNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        departureFlight = flightEntitySessionBeanLocal.createFlightEntity(new FlightEntity(311, aircraftConfiguration, route));
        returnRoute = route.getReturnFlightRoute();

        returnFlight = flightEntitySessionBeanLocal.createFlightEntity(new FlightEntity(312, aircraftConfiguration, returnRoute));
        flightEntitySessionBeanLocal.setReturnFlight(departureFlight, returnFlight);
        flightEntitySessionBeanLocal.setDepartureFlight(returnFlight, departureFlight);

        //testdata4
        try {
            originAirport = airportEntitySessionBeanLocal.retrieveAirportByCode("HKG");
        } catch (AirportNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            destinationAirport = airportEntitySessionBeanLocal.retrieveAirportByCode("NRT");
        } catch (AirportNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            route = flightRouteEntitySessionBeanLocal.retrieveRouteByAirport(originAirport, destinationAirport);
        } catch (FlightRouteNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            aircraftConfiguration = aircraftConfigurationEntitySessionBeanLocal.retrieveAircraftTypeByCode("B737TC");
        } catch (AircraftConfigurationNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        departureFlight = flightEntitySessionBeanLocal.createFlightEntity(new FlightEntity(411, aircraftConfiguration, route));
        returnRoute = route.getReturnFlightRoute();

        returnFlight = flightEntitySessionBeanLocal.createFlightEntity(new FlightEntity(412, aircraftConfiguration, returnRoute));
        flightEntitySessionBeanLocal.setReturnFlight(departureFlight, returnFlight);
        flightEntitySessionBeanLocal.setDepartureFlight(returnFlight, departureFlight);

        //testdata5
        try {
            originAirport = airportEntitySessionBeanLocal.retrieveAirportByCode("TPE");
        } catch (AirportNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            destinationAirport = airportEntitySessionBeanLocal.retrieveAirportByCode("NRT");
        } catch (AirportNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            route = flightRouteEntitySessionBeanLocal.retrieveRouteByAirport(originAirport, destinationAirport);
        } catch (FlightRouteNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            aircraftConfiguration = aircraftConfigurationEntitySessionBeanLocal.retrieveAircraftTypeByCode("B737TC");
        } catch (AircraftConfigurationNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        departureFlight = flightEntitySessionBeanLocal.createFlightEntity(new FlightEntity(511, aircraftConfiguration, route));
        returnRoute = route.getReturnFlightRoute();

        returnFlight = flightEntitySessionBeanLocal.createFlightEntity(new FlightEntity(512, aircraftConfiguration, returnRoute));
        flightEntitySessionBeanLocal.setReturnFlight(departureFlight, returnFlight);
        flightEntitySessionBeanLocal.setDepartureFlight(returnFlight, departureFlight);

        //testdata6
        try {
            originAirport = airportEntitySessionBeanLocal.retrieveAirportByCode("SIN");
        } catch (AirportNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            destinationAirport = airportEntitySessionBeanLocal.retrieveAirportByCode("SYD");
        } catch (AirportNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            route = flightRouteEntitySessionBeanLocal.retrieveRouteByAirport(originAirport, destinationAirport);
        } catch (FlightRouteNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            aircraftConfiguration = aircraftConfigurationEntitySessionBeanLocal.retrieveAircraftTypeByCode("B737TC");
        } catch (AircraftConfigurationNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        departureFlight = flightEntitySessionBeanLocal.createFlightEntity(new FlightEntity(611, aircraftConfiguration, route));
        returnRoute = route.getReturnFlightRoute();

        returnFlight = flightEntitySessionBeanLocal.createFlightEntity(new FlightEntity(612, aircraftConfiguration, returnRoute));
        flightEntitySessionBeanLocal.setReturnFlight(departureFlight, returnFlight);
        flightEntitySessionBeanLocal.setDepartureFlight(returnFlight, departureFlight);

        //testdata7
        try {
            originAirport = airportEntitySessionBeanLocal.retrieveAirportByCode("SIN");
        } catch (AirportNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            destinationAirport = airportEntitySessionBeanLocal.retrieveAirportByCode("SYD");
        } catch (AirportNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            route = flightRouteEntitySessionBeanLocal.retrieveRouteByAirport(originAirport, destinationAirport);
        } catch (FlightRouteNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            aircraftConfiguration = aircraftConfigurationEntitySessionBeanLocal.retrieveAircraftTypeByCode("B737AE");
        } catch (AircraftConfigurationNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        departureFlight = flightEntitySessionBeanLocal.createFlightEntity(new FlightEntity(621, aircraftConfiguration, route));
        returnRoute = route.getReturnFlightRoute();

        returnFlight = flightEntitySessionBeanLocal.createFlightEntity(new FlightEntity(622, aircraftConfiguration, returnRoute));
        flightEntitySessionBeanLocal.setReturnFlight(departureFlight, returnFlight);
        flightEntitySessionBeanLocal.setDepartureFlight(returnFlight, departureFlight);

        //testdata8
        try {
            originAirport = airportEntitySessionBeanLocal.retrieveAirportByCode("SYD");
        } catch (AirportNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            destinationAirport = airportEntitySessionBeanLocal.retrieveAirportByCode("NRT");
        } catch (AirportNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            route = flightRouteEntitySessionBeanLocal.retrieveRouteByAirport(originAirport, destinationAirport);
        } catch (FlightRouteNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            aircraftConfiguration = aircraftConfigurationEntitySessionBeanLocal.retrieveAircraftTypeByCode("B747TC");
        } catch (AircraftConfigurationNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        departureFlight = flightEntitySessionBeanLocal.createFlightEntity(new FlightEntity(711, aircraftConfiguration, route));
        returnRoute = route.getReturnFlightRoute();

        returnFlight = flightEntitySessionBeanLocal.createFlightEntity(new FlightEntity(712, aircraftConfiguration, returnRoute));
        flightEntitySessionBeanLocal.setReturnFlight(departureFlight, returnFlight);
        flightEntitySessionBeanLocal.setDepartureFlight(returnFlight, departureFlight);
    }

    @Override
    public void initializeSchedulePlan() {

        FlightSchedulePlanEntity plan = null;
        FlightEntity flight = null;
        FareEntity fare = null;

        //testdata1
        plan = this.initializeRecurringSchedulePlan(FlightSchedulePlanTypeEnum.RECURRENT_WEEK, 7, "ML711", "MONDAY", "2020-12-01", "2020-12-31", "09:00", "14:00", 120);
        flight = plan.getFlight();
        List<CabinClassConfigurationEntity> classes = flight.getAircraftConfigurationEntity().getCabinClassConfigurationEntitys();
        classes.size();

        for (CabinClassConfigurationEntity cabinClass : classes) {
            if (cabinClass.getType().equals(CabinClassTypeEnum.FIRST_CLASS)) {

                fare = fareEntitySessionBeanLocal.createFlightSchedulePlanEntity(new FareEntity("F001", "6500"));
                fareEntitySessionBeanLocal.associateFareWithCabinClass(cabinClass, fare);
                fareEntitySessionBeanLocal.associateFareWithPlan(plan, fare);

                fare = fareEntitySessionBeanLocal.createFlightSchedulePlanEntity(new FareEntity("F002", "6000"));
                fareEntitySessionBeanLocal.associateFareWithCabinClass(cabinClass, fare);
                fareEntitySessionBeanLocal.associateFareWithPlan(plan, fare);

            } else if (cabinClass.getType().equals(CabinClassTypeEnum.BUSINESS_CLASS)) {

                fare = fareEntitySessionBeanLocal.createFlightSchedulePlanEntity(new FareEntity("J001", "3500"));
                fareEntitySessionBeanLocal.associateFareWithCabinClass(cabinClass, fare);
                fareEntitySessionBeanLocal.associateFareWithPlan(plan, fare);

                fare = fareEntitySessionBeanLocal.createFlightSchedulePlanEntity(new FareEntity("J002", "3000"));
                fareEntitySessionBeanLocal.associateFareWithCabinClass(cabinClass, fare);
                fareEntitySessionBeanLocal.associateFareWithPlan(plan, fare);

            } else if (cabinClass.getType().equals(CabinClassTypeEnum.ECONOMY_CLASS)) {

                fare = fareEntitySessionBeanLocal.createFlightSchedulePlanEntity(new FareEntity("Y001", "1500"));
                fareEntitySessionBeanLocal.associateFareWithCabinClass(cabinClass, fare);
                fareEntitySessionBeanLocal.associateFareWithPlan(plan, fare);

                fare = fareEntitySessionBeanLocal.createFlightSchedulePlanEntity(new FareEntity("Y002", "1000"));
                fareEntitySessionBeanLocal.associateFareWithCabinClass(cabinClass, fare);
                fareEntitySessionBeanLocal.associateFareWithPlan(plan, fare);

            }
        }

        fareEntitySessionBeanLocal.setReturnFare(plan, plan.getReturnSchedulePlan());

        //testdata2
        plan = this.initializeRecurringSchedulePlan(FlightSchedulePlanTypeEnum.RECURRENT_WEEK, 7, "ML611", "SUNDAY", "2020-12-01", "2020-12-31", "12:00", "08:00", 120);
        flight = plan.getFlight();

        classes = flight.getAircraftConfigurationEntity().getCabinClassConfigurationEntitys();
        classes.size();

        for (CabinClassConfigurationEntity cabinClass : classes) {
            if (cabinClass.getType().equals(CabinClassTypeEnum.FIRST_CLASS)) {

                fare = fareEntitySessionBeanLocal.createFlightSchedulePlanEntity(new FareEntity("F001", "3200"));
                fareEntitySessionBeanLocal.associateFareWithCabinClass(cabinClass, fare);
                fareEntitySessionBeanLocal.associateFareWithPlan(plan, fare);

                fare = fareEntitySessionBeanLocal.createFlightSchedulePlanEntity(new FareEntity("F002", "3000"));
                fareEntitySessionBeanLocal.associateFareWithCabinClass(cabinClass, fare);
                fareEntitySessionBeanLocal.associateFareWithPlan(plan, fare);

            } else if (cabinClass.getType().equals(CabinClassTypeEnum.BUSINESS_CLASS)) {

                fare = fareEntitySessionBeanLocal.createFlightSchedulePlanEntity(new FareEntity("J001", "1750"));
                fareEntitySessionBeanLocal.associateFareWithCabinClass(cabinClass, fare);
                fareEntitySessionBeanLocal.associateFareWithPlan(plan, fare);

                fare = fareEntitySessionBeanLocal.createFlightSchedulePlanEntity(new FareEntity("J002", "1500"));
                fareEntitySessionBeanLocal.associateFareWithCabinClass(cabinClass, fare);
                fareEntitySessionBeanLocal.associateFareWithPlan(plan, fare);

            } else if (cabinClass.getType().equals(CabinClassTypeEnum.ECONOMY_CLASS)) {

                fare = fareEntitySessionBeanLocal.createFlightSchedulePlanEntity(new FareEntity("Y001", "750"));
                fareEntitySessionBeanLocal.associateFareWithCabinClass(cabinClass, fare);
                fareEntitySessionBeanLocal.associateFareWithPlan(plan, fare);

                fare = fareEntitySessionBeanLocal.createFlightSchedulePlanEntity(new FareEntity("Y002", "500"));
                fareEntitySessionBeanLocal.associateFareWithCabinClass(cabinClass, fare);
                fareEntitySessionBeanLocal.associateFareWithPlan(plan, fare);

            }
        }

        fareEntitySessionBeanLocal.setReturnFare(plan, plan.getReturnSchedulePlan());

        //testdata3
        plan = this.initializeRecurringSchedulePlan(FlightSchedulePlanTypeEnum.RECURRENT_WEEK, 7, "ML621", "TUESDAY", "2020-12-01", "2020-12-31", "10:00", "08:00", 120);
        flight = plan.getFlight();

        classes = flight.getAircraftConfigurationEntity().getCabinClassConfigurationEntitys();
        classes.size();

        for (CabinClassConfigurationEntity cabinClass : classes) {
            if (cabinClass.getType().equals(CabinClassTypeEnum.ECONOMY_CLASS)) {

                fare = fareEntitySessionBeanLocal.createFlightSchedulePlanEntity(new FareEntity("Y001", "700"));
                fareEntitySessionBeanLocal.associateFareWithCabinClass(cabinClass, fare);
                fareEntitySessionBeanLocal.associateFareWithPlan(plan, fare);

                fare = fareEntitySessionBeanLocal.createFlightSchedulePlanEntity(new FareEntity("Y002", "400"));
                fareEntitySessionBeanLocal.associateFareWithCabinClass(cabinClass, fare);
                fareEntitySessionBeanLocal.associateFareWithPlan(plan, fare);

            }
        }

        fareEntitySessionBeanLocal.setReturnFare(plan, plan.getReturnSchedulePlan());

        //testdata4
        plan = this.initializeRecurringSchedulePlan(FlightSchedulePlanTypeEnum.RECURRENT_WEEK, 7, "ML311", "MONDAY", "2020-12-01", "2020-12-31", "10:00", "06:30", 180);
        flight = plan.getFlight();

        classes = flight.getAircraftConfigurationEntity().getCabinClassConfigurationEntitys();
        classes.size();

        for (CabinClassConfigurationEntity cabinClass : classes) {
            if (cabinClass.getType().equals(CabinClassTypeEnum.FIRST_CLASS)) {

                fare = fareEntitySessionBeanLocal.createFlightSchedulePlanEntity(new FareEntity("F001", "3350"));
                fareEntitySessionBeanLocal.associateFareWithCabinClass(cabinClass, fare);
                fareEntitySessionBeanLocal.associateFareWithPlan(plan, fare);

                fare = fareEntitySessionBeanLocal.createFlightSchedulePlanEntity(new FareEntity("F002", "3100"));
                fareEntitySessionBeanLocal.associateFareWithCabinClass(cabinClass, fare);
                fareEntitySessionBeanLocal.associateFareWithPlan(plan, fare);

            } else if (cabinClass.getType().equals(CabinClassTypeEnum.BUSINESS_CLASS)) {

                fare = fareEntitySessionBeanLocal.createFlightSchedulePlanEntity(new FareEntity("J001", "1850"));
                fareEntitySessionBeanLocal.associateFareWithCabinClass(cabinClass, fare);
                fareEntitySessionBeanLocal.associateFareWithPlan(plan, fare);

                fare = fareEntitySessionBeanLocal.createFlightSchedulePlanEntity(new FareEntity("J002", "1600"));
                fareEntitySessionBeanLocal.associateFareWithCabinClass(cabinClass, fare);
                fareEntitySessionBeanLocal.associateFareWithPlan(plan, fare);

            } else if (cabinClass.getType().equals(CabinClassTypeEnum.ECONOMY_CLASS)) {

                fare = fareEntitySessionBeanLocal.createFlightSchedulePlanEntity(new FareEntity("Y001", "850"));
                fareEntitySessionBeanLocal.associateFareWithCabinClass(cabinClass, fare);
                fareEntitySessionBeanLocal.associateFareWithPlan(plan, fare);

                fare = fareEntitySessionBeanLocal.createFlightSchedulePlanEntity(new FareEntity("Y002", "600"));
                fareEntitySessionBeanLocal.associateFareWithCabinClass(cabinClass, fare);
                fareEntitySessionBeanLocal.associateFareWithPlan(plan, fare);

            }
        }

        fareEntitySessionBeanLocal.setReturnFare(plan, plan.getReturnSchedulePlan());

        //testdata5
        plan = this.initializeRecurringSchedulePlan(FlightSchedulePlanTypeEnum.RECURRENT_DAY, 2, "ML411", "", "2020-12-01", "2020-12-31", "13:00", "04:00", 240);
        flight = plan.getFlight();

        classes = flight.getAircraftConfigurationEntity().getCabinClassConfigurationEntitys();
        classes.size();

        for (CabinClassConfigurationEntity cabinClass : classes) {
            if (cabinClass.getType().equals(CabinClassTypeEnum.FIRST_CLASS)) {

                fare = fareEntitySessionBeanLocal.createFlightSchedulePlanEntity(new FareEntity("F001", "3150"));
                fareEntitySessionBeanLocal.associateFareWithCabinClass(cabinClass, fare);
                fareEntitySessionBeanLocal.associateFareWithPlan(plan, fare);

                fare = fareEntitySessionBeanLocal.createFlightSchedulePlanEntity(new FareEntity("F002", "2900"));
                fareEntitySessionBeanLocal.associateFareWithCabinClass(cabinClass, fare);
                fareEntitySessionBeanLocal.associateFareWithPlan(plan, fare);

            } else if (cabinClass.getType().equals(CabinClassTypeEnum.BUSINESS_CLASS)) {

                fare = fareEntitySessionBeanLocal.createFlightSchedulePlanEntity(new FareEntity("J001", "1650"));
                fareEntitySessionBeanLocal.associateFareWithCabinClass(cabinClass, fare);
                fareEntitySessionBeanLocal.associateFareWithPlan(plan, fare);

                fare = fareEntitySessionBeanLocal.createFlightSchedulePlanEntity(new FareEntity("J002", "1400"));
                fareEntitySessionBeanLocal.associateFareWithCabinClass(cabinClass, fare);
                fareEntitySessionBeanLocal.associateFareWithPlan(plan, fare);

            } else if (cabinClass.getType().equals(CabinClassTypeEnum.ECONOMY_CLASS)) {

                fare = fareEntitySessionBeanLocal.createFlightSchedulePlanEntity(new FareEntity("Y001", "650"));
                fareEntitySessionBeanLocal.associateFareWithCabinClass(cabinClass, fare);
                fareEntitySessionBeanLocal.associateFareWithPlan(plan, fare);

                fare = fareEntitySessionBeanLocal.createFlightSchedulePlanEntity(new FareEntity("Y002", "400"));
                fareEntitySessionBeanLocal.associateFareWithCabinClass(cabinClass, fare);
                fareEntitySessionBeanLocal.associateFareWithPlan(plan, fare);

            }
        }

        fareEntitySessionBeanLocal.setReturnFare(plan, plan.getReturnSchedulePlan());

        //testdata6
        plan = this.initializeMultipleSchedulePlan();
        flight = plan.getFlight();

        classes = flight.getAircraftConfigurationEntity().getCabinClassConfigurationEntitys();
        classes.size();

        for (CabinClassConfigurationEntity cabinClass : classes) {
            if (cabinClass.getType().equals(CabinClassTypeEnum.FIRST_CLASS)) {

                fare = fareEntitySessionBeanLocal.createFlightSchedulePlanEntity(new FareEntity("F001", "3100"));
                fareEntitySessionBeanLocal.associateFareWithCabinClass(cabinClass, fare);
                fareEntitySessionBeanLocal.associateFareWithPlan(plan, fare);

                fare = fareEntitySessionBeanLocal.createFlightSchedulePlanEntity(new FareEntity("F002", "2850"));
                fareEntitySessionBeanLocal.associateFareWithCabinClass(cabinClass, fare);
                fareEntitySessionBeanLocal.associateFareWithPlan(plan, fare);

            } else if (cabinClass.getType().equals(CabinClassTypeEnum.BUSINESS_CLASS)) {

                fare = fareEntitySessionBeanLocal.createFlightSchedulePlanEntity(new FareEntity("J001", "1600"));
                fareEntitySessionBeanLocal.associateFareWithCabinClass(cabinClass, fare);
                fareEntitySessionBeanLocal.associateFareWithPlan(plan, fare);

                fare = fareEntitySessionBeanLocal.createFlightSchedulePlanEntity(new FareEntity("J002", "1350"));
                fareEntitySessionBeanLocal.associateFareWithCabinClass(cabinClass, fare);
                fareEntitySessionBeanLocal.associateFareWithPlan(plan, fare);

            } else if (cabinClass.getType().equals(CabinClassTypeEnum.ECONOMY_CLASS)) {

                fare = fareEntitySessionBeanLocal.createFlightSchedulePlanEntity(new FareEntity("Y001", "600"));
                fareEntitySessionBeanLocal.associateFareWithCabinClass(cabinClass, fare);
                fareEntitySessionBeanLocal.associateFareWithPlan(plan, fare);

                fare = fareEntitySessionBeanLocal.createFlightSchedulePlanEntity(new FareEntity("Y002", "350"));
                fareEntitySessionBeanLocal.associateFareWithCabinClass(cabinClass, fare);
                fareEntitySessionBeanLocal.associateFareWithPlan(plan, fare);

            }
        }

        fareEntitySessionBeanLocal.setReturnFare(plan, plan.getReturnSchedulePlan());
    }

    @Override
    public FlightSchedulePlanEntity initializeRecurringSchedulePlan(FlightSchedulePlanTypeEnum type, int dayRecurring, String flightCode, String day, String startDate, String endDate, String departureTime, String duration, int layover) {

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm Z");

        FlightEntity flight = null;

        try {
            flight = flightEntitySessionBeanLocal.retrieveFlightByCode(flightCode);
        } catch (FlightNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        FlightSchedulePlanEntity schedulePlan = flightSchedulePlanEntitySessionBeanLocal.createFlightSchedulePlanEntity(new FlightSchedulePlanEntity(type, startDate, endDate, layover, flight));

        String returnTime = this.createRecurrentSchedule(day, schedulePlan, startDate, endDate, dayRecurring, departureTime, dateFormat, duration, layover);

        LocalDate localfirstDate = LocalDate.parse(schedulePlan.getStartDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        ZonedDateTime firstDate = localfirstDate.atStartOfDay(ZoneId.systemDefault());

        firstDate = firstDate.plusMinutes(layover);
        String firstDateStr = firstDate.format(dateFormat).substring(0, 10);

        LocalDate localLastDate = LocalDate.parse(schedulePlan.getEndDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        ZonedDateTime lastDate = localLastDate.atStartOfDay(ZoneId.systemDefault());

        lastDate = lastDate.plusMinutes(layover);
        String lastDateStr = lastDate.format(dateFormat).substring(0, 10);

        FlightSchedulePlanEntity returnPlan = flightSchedulePlanEntitySessionBeanLocal.createFlightSchedulePlanEntity(new FlightSchedulePlanEntity(type, firstDateStr, lastDateStr, 0, flight.getReturnFlight()));
        flightSchedulePlanEntitySessionBeanLocal.associateReturnSchedulePlan(schedulePlan, returnPlan);

        this.createRecurrentSchedule(day, returnPlan, firstDateStr, lastDateStr, dayRecurring, returnTime, dateFormat, duration, layover);
        flightSchedulePlanEntitySessionBeanLocal.associatePlanWithFlight(returnPlan, flight.getReturnFlight());
        flightScheduleEntitySessionBeanLocal.associateNewSeatsInventory(returnPlan);

        flightSchedulePlanEntitySessionBeanLocal.associatePlanWithFlight(schedulePlan, flight);
        flightScheduleEntitySessionBeanLocal.associateNewSeatsInventory(schedulePlan);

        return schedulePlan;
    }

    @Override
    public String createRecurrentSchedule(String day, FlightSchedulePlanEntity schedule, String startDate, String endDate, int days, String departureTime, DateTimeFormatter dateFormat, String duration, int layoverDuration) {

        FlightEntity flight = flightSchedulePlanEntitySessionBeanLocal.retrieveFlightFromPlan(schedule);
        String timeZone = flightEntitySessionBeanLocal.retrieveTimeZoneByFlight(flight);

        ZonedDateTime startingDate = ZonedDateTime.parse((startDate + " " + departureTime + " " + timeZone), dateFormat);

        if (schedule.getType().equals(FlightSchedulePlanTypeEnum.RECURRENT_WEEK)) {
            while (!startingDate.getDayOfWeek().toString().equals(day)) {
                startingDate = startingDate.plusDays(1);
            }
        }

        String startingDateTime = startingDate.format(dateFormat);
        LocalDate localEndingDate = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        ZonedDateTime endingDate = localEndingDate.atStartOfDay(ZoneId.systemDefault());

        String durationHour = duration.substring(0, 2);
        String durationMin = duration.substring(3);

        int durationHourInt = Integer.parseInt(durationHour);
        int durationMinInt = Integer.parseInt(durationMin);
        int totalDuration = durationMinInt + (60 * durationHourInt);

        ZonedDateTime arrivalDateTime = startingDate.plusMinutes(totalDuration);
        String arrDateTime = arrivalDateTime.format(dateFormat);

        arrivalDateTime = startingDate.plusMinutes(totalDuration);
        arrDateTime = arrivalDateTime.format(dateFormat);

        while (startingDate.isBefore(endingDate)) {

            startingDateTime = startingDate.format(dateFormat);

            arrivalDateTime = startingDate.plusMinutes(totalDuration);
            arrDateTime = arrivalDateTime.format(dateFormat);

            FlightScheduleEntity departure = flightScheduleEntitySessionBeanLocal.createFlightScheduleEntity(new FlightScheduleEntity(startingDateTime, totalDuration, arrDateTime));
            flightScheduleEntitySessionBeanLocal.associateWithPlan(departure, schedule);

            ZonedDateTime temp1 = startingDate.plusDays(days);
            startingDate = temp1;

            ZonedDateTime temp2 = arrivalDateTime.plusDays(days);
            arrivalDateTime = temp2;
        }

        arrivalDateTime.plusMinutes(layoverDuration);
        return arrivalDateTime.format(dateFormat).substring(11, 16);
    }

    @Override
    public FlightSchedulePlanEntity initializeMultipleSchedulePlan() {

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm Z");
        FlightEntity flight = null;

        try {
            flight = flightEntitySessionBeanLocal.retrieveFlightByCode("ML511");
        } catch (FlightNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        FlightSchedulePlanEntity schedulePlan = flightSchedulePlanEntitySessionBeanLocal.createFlightSchedulePlanEntity(new FlightSchedulePlanEntity(FlightSchedulePlanTypeEnum.MULTIPLE, "2020-12-07", 120, flight));

        FlightScheduleEntity flightSchedule = new FlightScheduleEntity("2020-12-07 17:00 +0800", 180, "2020-12-07 20:00 +0800");
        flightSchedule = flightScheduleEntitySessionBeanLocal.createFlightScheduleEntity(flightSchedule);
        flightScheduleEntitySessionBeanLocal.associateWithPlan(flightSchedule, schedulePlan);

        flightSchedule = new FlightScheduleEntity("2020-12-08 17:00 +0800", 180, "2020-12-08 17:00 +0800");
        flightSchedule = flightScheduleEntitySessionBeanLocal.createFlightScheduleEntity(flightSchedule);
        flightScheduleEntitySessionBeanLocal.associateWithPlan(flightSchedule, schedulePlan);

        flightSchedule = new FlightScheduleEntity("2020-12-09 17:00 +0800", 180, "2020-12-09 17:00 +0800");
        flightSchedule = flightScheduleEntitySessionBeanLocal.createFlightScheduleEntity(flightSchedule);
        flightScheduleEntitySessionBeanLocal.associateWithPlan(flightSchedule, schedulePlan);

        flightSchedulePlanEntitySessionBeanLocal.associatePlanWithFlight(schedulePlan, flight);

        DateTimeFormatter dateFormat2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        FlightSchedulePlanEntity returnPlan = flightSchedulePlanEntitySessionBeanLocal.createFlightSchedulePlanEntity(new FlightSchedulePlanEntity(FlightSchedulePlanTypeEnum.MULTIPLE, "2020-12-07", 120, flight.getReturnFlight()));

        List<FlightScheduleEntity> schedules = schedulePlan.getFlightSchedules();
        schedules.size();

        FlightScheduleEntity returnFlightSchedule = null;

        for (FlightScheduleEntity schedule : schedules) {

            ZonedDateTime returnDepartureDateTime = ZonedDateTime.parse(schedule.getArrivalDateTime(), dateFormat).plusMinutes(120);
            String returnDepDateTime = returnDepartureDateTime.format(dateFormat);

            ZonedDateTime returnArrivalDateTime = returnDepartureDateTime.plusMinutes(120);
            String returnArrDateTime = returnArrivalDateTime.format(dateFormat);
            System.out.println("Expected return arrival time: " + returnArrDateTime);

            returnFlightSchedule = flightScheduleEntitySessionBeanLocal.createFlightScheduleEntity(new FlightScheduleEntity(returnDepDateTime, 180, returnArrDateTime));

            flightScheduleEntitySessionBeanLocal.associateReturnSchedule(schedule, returnFlightSchedule);

        }

        flightSchedulePlanEntitySessionBeanLocal.associatePlanWithFlight(returnPlan, flight.getReturnFlight());

        flightSchedulePlanEntitySessionBeanLocal.associateReturnSchedulePlan(schedulePlan, returnPlan);
        flightScheduleEntitySessionBeanLocal.associateNewSeatsInventory(returnPlan);

        flightSchedulePlanEntitySessionBeanLocal.associatePlanWithFlight(schedulePlan, flight);
        flightScheduleEntitySessionBeanLocal.associateNewSeatsInventory(schedulePlan);

        return schedulePlan;
    }
}
