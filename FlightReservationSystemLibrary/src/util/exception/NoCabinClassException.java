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
public class NoCabinClassException extends Exception {

    /**
     * Creates a new instance of <code>NoCabinClassException</code> without
     * detail message.
     */
    public NoCabinClassException() {
    }

    /**
     * Constructs an instance of <code>NoCabinClassException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NoCabinClassException(String msg) {
        super(msg);
    }
}
