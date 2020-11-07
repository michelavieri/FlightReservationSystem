/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ReservationEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author miche
 */
@Local
public interface ReservationEntitySessionBeanLocal {

    public List<ReservationEntity> retrieveReservationsByScheduleIdFirstClass(Long id);
    
    public List<ReservationEntity> retrieveReservationsByScheduleIdBusinessClass(Long id);
    
    public List<ReservationEntity> retrieveReservationsByScheduleIdPremiumClass(Long id);
    
    public List<ReservationEntity> retrieveReservationsByScheduleIdEconomyClass(Long id);
}
