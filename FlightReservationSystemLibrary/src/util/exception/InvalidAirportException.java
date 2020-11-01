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
public class InvalidAirportException extends Exception {

    /**
     * Creates a new instance of <code>InvalidAirportException</code> without
     * detail message.
     */
    public InvalidAirportException() {
    }

    /**
     * Constructs an instance of <code>InvalidAirportException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidAirportException(String msg) {
        super(msg);
    }
}
