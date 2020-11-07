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
public class MaximumSeatCapacityExceededException extends Exception {

    /**
     * Creates a new instance of
     * <code>MaximumSeatCapacityExceededException</code> without detail message.
     */
    public MaximumSeatCapacityExceededException() {
    }

    /**
     * Constructs an instance of
     * <code>MaximumSeatCapacityExceededException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public MaximumSeatCapacityExceededException(String msg) {
        super(msg);
    }
}
