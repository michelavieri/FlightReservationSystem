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
public class SeatIsBookedException extends Exception {

    /**
     * Creates a new instance of <code>SeatIsBookedException</code> without
     * detail message.
     */
    public SeatIsBookedException() {
    }

    /**
     * Constructs an instance of <code>SeatIsBookedException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public SeatIsBookedException(String msg) {
        super(msg);
    }
}
