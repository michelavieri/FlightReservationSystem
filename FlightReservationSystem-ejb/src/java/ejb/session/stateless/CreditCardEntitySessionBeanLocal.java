/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditCardEntity;
import javax.ejb.Local;

/**
 *
 * @author miche
 */
@Local
public interface CreditCardEntitySessionBeanLocal {
    public CreditCardEntity createCreditCard(CreditCardEntity card);
    
    public CreditCardEntity createCreditCardUnmanaged(CreditCardEntity card);
    
}
