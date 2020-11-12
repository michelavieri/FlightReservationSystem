/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.SeatEntity;
import javax.ejb.Local;

/**
 *
 * @author Chrisya
 */
@Local
public interface SeatEntitySessionBeanLocal {

    public SeatEntity createNewSeat(SeatEntity newSeat);
    
}
