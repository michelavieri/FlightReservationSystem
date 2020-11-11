/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author miche
 */
public class NotMyReservationException extends Exception {

    /**
     * Creates a new instance of <code>NotMyReservationException</code> without
     * detail message.
     */
    public NotMyReservationException() {
    }

    /**
     * Constructs an instance of <code>NotMyReservationException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NotMyReservationException(String msg) {
        super(msg);
    }
}
