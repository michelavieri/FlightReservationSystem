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
public class AircraftTypeNameExistException extends Exception {

    /**
     * Creates a new instance of <code>AircraftTypeNameExistException</code>
     * without detail message.
     */
    public AircraftTypeNameExistException() {
    }

    /**
     * Constructs an instance of <code>AircraftTypeNameExistException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public AircraftTypeNameExistException(String msg) {
        super(msg);
    }
}
