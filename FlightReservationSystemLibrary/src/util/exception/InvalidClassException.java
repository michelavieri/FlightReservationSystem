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
public class InvalidClassException extends Exception {

    /**
     * Creates a new instance of <code>InvalidClassException</code> without
     * detail message.
     */
    public InvalidClassException() {
    }

    /**
     * Constructs an instance of <code>InvalidClassException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidClassException(String msg) {
        super(msg);
    }
}
