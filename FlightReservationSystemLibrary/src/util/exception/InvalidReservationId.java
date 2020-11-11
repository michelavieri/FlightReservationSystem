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
public class InvalidReservationId extends Exception {

    /**
     * Creates a new instance of <code>InvalidReservationId</code> without
     * detail message.
     */
    public InvalidReservationId() {
    }

    /**
     * Constructs an instance of <code>InvalidReservationId</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidReservationId(String msg) {
        super(msg);
    }
}
