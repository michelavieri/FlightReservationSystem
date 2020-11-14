/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AirportEntity;
import entity.BookingTicketEntity;
import entity.CreditCardEntity;
import entity.CustomerEntity;
import entity.FlightRouteEntity;
import entity.FlightScheduleEntity;
import entity.ReservationEntity;
import entity.SeatEntity;
import entity.SeatsInventoryEntity;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.CabinClassTypeEnum;
import util.exception.InvalidReservationId;
import util.exception.NoTicketException;
import util.exception.NotMyReservationException;

/**
 *
 * @author Chrisya
 */
@Stateless
public class ReservationEntitySessionBean implements ReservationEntitySessionBeanRemote, ReservationEntitySessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;

    public ReservationEntitySessionBean() {

    }

    @Override
    public ReservationEntity createNewReservation(CustomerEntity customer, CreditCardEntity card, ReservationEntity newReservation) {
        customer = entityManager.find(CustomerEntity.class, customer.getCustomerId());
        entityManager.persist(newReservation);
        customer.getReservationsEntitys().size();
        customer.getReservationsEntitys().add(newReservation);
        newReservation.setCustomer(customer);
        entityManager.persist(card);

        newReservation.getTickets().size();
        List<BookingTicketEntity> tickets = newReservation.getTickets();
        for (BookingTicketEntity ticket : tickets) {
            entityManager.persist(ticket);
            entityManager.persist(ticket.getPassenger());
            SeatEntity seat = ticket.getSeat();
            seat = entityManager.find(SeatEntity.class, seat.getSeatId());
            seat.setBooked(true);
            SeatsInventoryEntity seatsInventory = entityManager.find(SeatsInventoryEntity.class, seat.getSeatsInventory().getInventoryId());
            int reservedSeats = seatsInventory.getReservedSeatsSize();
            int balanceSeats = seatsInventory.getBalanceSeatsSize();
            balanceSeats--;
            reservedSeats++;
            seatsInventory.setBalanceSeatsSize(balanceSeats);
            seatsInventory.setReservedSeatsSize(reservedSeats);
            ticket.setReservationEntity(newReservation);
        }
        
        newReservation.setCreditCardEntity(card);
        card.setReservation(newReservation);
        customer = entityManager.find(CustomerEntity.class, customer.getCustomerId());
        customer.getReservationsEntitys().size();
        List<ReservationEntity> reservations = customer.getReservationsEntitys();
        reservations.add(newReservation);

        entityManager.flush();

        return newReservation;
    }

    @Override
    public List<ReservationEntity> retrieveFlightReservationsByCustomer(CustomerEntity cust) {
        cust = entityManager.find(CustomerEntity.class, cust.getCustomerId());
        cust.getReservationsEntitys().size();
        if (cust.getReservationsEntitys().isEmpty()) {
            return new ArrayList<>();
        }
        cust.getReservationsEntitys().size();
        List<ReservationEntity> reservations = cust.getReservationsEntitys();

        return reservations;
    }

    @Override
    public ReservationEntity retrieveReservationByReservationId(long reservationId, CustomerEntity customer) throws InvalidReservationId, NotMyReservationException {
        Query query = entityManager.createQuery("SELECT r FROM ReservationEntity r WHERE r.reservationId = :id");
        query.setParameter("id", reservationId);
        ReservationEntity reservation = null;
        try {
            reservation = (ReservationEntity) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new InvalidReservationId("The reservation Id is invalid!");
        }

        if (!reservation.getCustomer().equals(customer)) {
            throw new NotMyReservationException("This reservation is not your reservation!");
        }
        return reservation;
    }
    
    @Override
    public List<BookingTicketEntity> retrieveTickets(long reservationId) throws NoTicketException {
        Query query = entityManager.createQuery("SELECT t FROM BookingTicketEntity t WHERE t.reservationEntity.reservationId = :inId");
        query.setParameter("inId", reservationId);
        
        try {
            return query.getResultList();
        } catch (NoResultException ex) {
            throw new NoTicketException("No tickets for this reservation!");
        }
    }

    @Override
    public List<List<FlightScheduleEntity>> searchConnectingFlights(String departureAirport,
            String destinationAirport, String departureDateTime, int numOfPassenger, int stopovers, CabinClassTypeEnum classType) {
        List<FlightScheduleEntity> availableSchedule = new ArrayList<>();
        List<List<FlightScheduleEntity>> finalSchedule = new ArrayList<>();

        Query query = entityManager.createQuery("SELECT a FROM AirportEntity a WHERE a.airportCode = :depAirportCode");
        query.setParameter("depAirportCode", departureAirport);
        AirportEntity departure = (AirportEntity) query.getSingleResult();

        query = entityManager.createQuery("SELECT a FROM AirportEntity a WHERE a.airportCode = :destAirportCode");
        query.setParameter("destAirportCode", destinationAirport);
        AirportEntity destination = (AirportEntity) query.getSingleResult();

        query = entityManager.createQuery("SELECT a FROM FlightScheduleEntity a");
        List<FlightScheduleEntity> allSchedules = query.getResultList();
        recurseTransit(allSchedules, departure, destination, stopovers, availableSchedule, finalSchedule, classType, numOfPassenger);

        List<List<FlightScheduleEntity>> copyTemp = new ArrayList<>();
        copyTemp.addAll(finalSchedule);
        for (List<FlightScheduleEntity> schedules : copyTemp) {
            if (!schedules.get(0).getDepartureDateTime().substring(0, 10).equals(departureDateTime)) {
                finalSchedule.remove(schedules);
            }
        }
        return finalSchedule;
    }

    @Override
    public List<List<FlightScheduleEntity>> searchConnectingFlightsBefore(String departureAirport,
            String destinationAirport, String departureDateTime, int numOfPassenger, int stopovers, CabinClassTypeEnum classType) {
        List<FlightScheduleEntity> availableSchedule = new ArrayList<>();
        List<List<FlightScheduleEntity>> finalSchedule = new ArrayList<>();

        Query query = entityManager.createQuery("SELECT a FROM AirportEntity a WHERE a.airportCode = :depAirportCode");
        query.setParameter("depAirportCode", departureAirport);
        AirportEntity departure = (AirportEntity) query.getSingleResult();

        query = entityManager.createQuery("SELECT a FROM AirportEntity a WHERE a.airportCode = :destAirportCode");
        query.setParameter("destAirportCode", destinationAirport);
        AirportEntity destination = (AirportEntity) query.getSingleResult();

        query = entityManager.createQuery("SELECT a FROM FlightScheduleEntity a");
        List<FlightScheduleEntity> allSchedules = query.getResultList();

        recurseTransit(allSchedules, departure, destination, stopovers, availableSchedule, finalSchedule, classType, numOfPassenger);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm Z");
        DateTimeFormatter dateFormat2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate ddate = LocalDate.parse(departureDateTime, dateFormat2);
//        ZonedDateTime date = ddate.atStartOfDay(ZoneId.systemDefault());

        List<List<FlightScheduleEntity>> copyTemp = new ArrayList<>();
        copyTemp.addAll(finalSchedule);
        for (List<FlightScheduleEntity> schedules : copyTemp) {
            LocalDate departureTime = LocalDate.parse(schedules.get(0).getDepartureDateTime().substring(0, 10), dateFormat2);
            LocalDate minusOne = ddate.minusDays(1);
            LocalDate minusTwo = ddate.minusDays(2);
            LocalDate minusThree = ddate.minusDays(3);
            if (!departureTime.equals(minusOne) && !departureTime.equals(minusTwo) && !departureTime.equals(minusThree)) {
                finalSchedule.remove(schedules);
            }
        }
        return finalSchedule;
    }

    @Override
    public List<List<FlightScheduleEntity>> searchConnectingFlightsAfter(String departureAirport,
            String destinationAirport, String departureDateTime, int numOfPassenger, int stopovers, CabinClassTypeEnum classType) {
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        List<FlightScheduleEntity> availableSchedule = new ArrayList<>();
        List<List<FlightScheduleEntity>> finalSchedule = new ArrayList<>();

        Query query = entityManager.createQuery("SELECT a FROM AirportEntity a WHERE a.airportCode = :depAirportCode");
        query.setParameter("depAirportCode", departureAirport);
        AirportEntity departure = (AirportEntity) query.getSingleResult();

        query = entityManager.createQuery("SELECT a FROM AirportEntity a WHERE a.airportCode = :destAirportCode");
        query.setParameter("destAirportCode", destinationAirport);
        AirportEntity destination = (AirportEntity) query.getSingleResult();

        query = entityManager.createQuery("SELECT a FROM FlightScheduleEntity a");
        List<FlightScheduleEntity> allSchedules = query.getResultList();
        recurseTransit(allSchedules, departure, destination, stopovers, availableSchedule, finalSchedule, classType, numOfPassenger);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm Z");
        DateTimeFormatter dateFormat2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate ddate = LocalDate.parse(departureDateTime, dateFormat2);
//        ZonedDateTime date = ddate.atStartOfDay(ZoneId.systemDefault());

        List<List<FlightScheduleEntity>> copyTemp = new ArrayList<>();
        copyTemp.addAll(finalSchedule);
        for (List<FlightScheduleEntity> schedules : copyTemp) {
            LocalDate departureTime = LocalDate.parse(schedules.get(0).getDepartureDateTime().substring(0, 10), dateFormat2);
            LocalDate minusOne = ddate.plusDays(1);
            LocalDate minusTwo = ddate.plusDays(2);
            LocalDate minusThree = ddate.plusDays(3);
            if (!departureTime.equals(minusOne) && !departureTime.equals(minusTwo) && !departureTime.equals(minusThree)) {
                finalSchedule.remove(schedules);
            }
        }

        return finalSchedule;
    }

    @Override
    public List<List<FlightScheduleEntity>> recurseTransit(List<FlightScheduleEntity> allSchedules, AirportEntity departureAirport, AirportEntity destinationAirport,
            int stopovers, List<FlightScheduleEntity> availableSchedule,
            List<List<FlightScheduleEntity>> finalSchedule, CabinClassTypeEnum classType, int numOfPassenger) {
//        System.out.println("HEREEEEEEEEEEEEEEEEE" + allSchedules.size());
        if (stopovers <= 0) {
            return finalSchedule;
        }
        for (int i = 0; i < allSchedules.size(); i++) {
            FlightRouteEntity route = allSchedules.get(i).getPlan().getFlight().getRoute();

            if (availableSchedule.isEmpty()) {
                if (route.getOriginAirport().equals(departureAirport)) {
                    if (route.getDestinationAirport().equals(destinationAirport)) {
                        if (classType != null) {
                            List<SeatsInventoryEntity> seats = allSchedules.get(i).getSeatsInventoryEntitys();
                            for (SeatsInventoryEntity seat : seats) {
                                if (seat.getCabinClass().getType().equals(classType)) {
                                    List<FlightScheduleEntity> scheduleTemp = new ArrayList<>(availableSchedule);
                                    scheduleTemp.add(allSchedules.get(i));
                                    finalSchedule.add(scheduleTemp);
//                                    System.out.println("masuk sini");
                                }
                            }
                        } else {
                            List<FlightScheduleEntity> scheduleTemp = new ArrayList<>(availableSchedule);
                            scheduleTemp.add(allSchedules.get(i));
                            finalSchedule.add(scheduleTemp);
//                            System.out.println("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
                        }
                    } else {
                        List<FlightScheduleEntity> scheduleTemp = new ArrayList<>(availableSchedule);
                        scheduleTemp.add(allSchedules.get(i));
                        recurseTransit(allSchedules, route.getDestinationAirport(), destinationAirport, stopovers - 1, scheduleTemp, finalSchedule, classType, numOfPassenger);
                    }
                }
            } else {
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm Z");
                ZonedDateTime departureTime = ZonedDateTime.parse(allSchedules.get(i).getDepartureDateTime(), dateFormat);
                ZonedDateTime arrivalTime = ZonedDateTime.parse(availableSchedule.get(availableSchedule.size() - 1).getArrivalDateTime(), dateFormat);

                if (route.getOriginAirport().equals(departureAirport) && departureTime.isAfter(arrivalTime)
                        && arrivalTime.plusDays(7).isAfter(departureTime)) {
                    if (route.getDestinationAirport().equals(destinationAirport)) {
                        List<SeatsInventoryEntity> seats = allSchedules.get(i).getSeatsInventoryEntitys();
                        if (classType != null) {
                            for (SeatsInventoryEntity seat : seats) {
                                if (seat.getCabinClass().getType().equals(classType)) {
                                    List<FlightScheduleEntity> scheduleTemp = new ArrayList<>(availableSchedule);
                                    scheduleTemp.add(allSchedules.get(i));
                                    finalSchedule.add(scheduleTemp);
//                                    System.out.println("ttttt");
                                    continue;
                                }
                            }
                        } else {
                            List<FlightScheduleEntity> scheduleTemp = new ArrayList<>(availableSchedule);
                            scheduleTemp.add(allSchedules.get(i));
                            finalSchedule.add(scheduleTemp);
                            continue;
                        }
                    } else {
                        List<SeatsInventoryEntity> seats = allSchedules.get(i).getSeatsInventoryEntitys();
                        for (SeatsInventoryEntity seat : seats) {
                            if (seat.getCabinClass().getType().equals(classType)) {
                                if (seat.getBalanceSeatsSize() < numOfPassenger) {
                                    List<FlightScheduleEntity> scheduleTemp = new ArrayList<>(availableSchedule);
                                    scheduleTemp.add(allSchedules.get(i));
                                    recurseTransit(allSchedules, route.getDestinationAirport(), destinationAirport, stopovers - 1, scheduleTemp, finalSchedule, classType, numOfPassenger);

                                }
                            }
                        }
                    }
                }
            }
        }
        return finalSchedule;
    }

    @Override
    public FlightScheduleEntity retrieveFlightScheduleById(Long id) {
        Query query = entityManager.createQuery("SELECT s FROM FlightScheduleEntity s WHERE s.scheduleId = :id");
        query.setParameter("id", id);

        FlightScheduleEntity schedule = (FlightScheduleEntity) query.getSingleResult();

        return schedule;
    }
}
