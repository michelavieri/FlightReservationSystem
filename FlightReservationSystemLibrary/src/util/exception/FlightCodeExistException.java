/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author Chrisya
 */
public class FlightCodeExistException extends Exception {

    /**
     * Creates a new instance of <code>FlightCodeExistException</code> without
     * detail message.
     */
    public FlightCodeExistException() {
    }

    /**
     * Constructs an instance of <code>FlightCodeExistException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public FlightCodeExistException(String msg) {
        super(msg);
    }
}
