/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.BookingTicketEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author miche
 */
@Local
public interface BookingTicketEntitySessionBeanLocal {

    public List<BookingTicketEntity> retrieveTicketsByScheduleIdFirstClass(Long scheduleId);

    public List<BookingTicketEntity> retrieveTicketsByScheduleIdBusinessClass(Long scheduleId);

    public List<BookingTicketEntity> retrieveTicketsByScheduleIdPremiumEconomyClass(Long scheduleId);

    public List<BookingTicketEntity> retrieveTicketsByScheduleIdEconomyClass(Long scheduleId);

}
