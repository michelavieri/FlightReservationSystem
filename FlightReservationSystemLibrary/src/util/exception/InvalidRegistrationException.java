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
public class InvalidRegistrationException extends Exception {

    /**
     * Creates a new instance of <code>InvalidRegistrationException</code>
     * without detail message.
     */
    public InvalidRegistrationException() {
    }

    /**
     * Constructs an instance of <code>InvalidRegistrationException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidRegistrationException(String msg) {
        super(msg);
    }
}
