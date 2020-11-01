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
public class AirportNameExistException extends Exception {

    /**
     * Creates a new instance of <code>AirportNameExistException</code> without
     * detail message.
     */
    public AirportNameExistException() {
    }

    /**
     * Constructs an instance of <code>AirportNameExistException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public AirportNameExistException(String msg) {
        super(msg);
    }
}
