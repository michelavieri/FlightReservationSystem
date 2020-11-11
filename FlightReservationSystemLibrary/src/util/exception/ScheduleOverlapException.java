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
public class ScheduleOverlapException extends Exception {

    /**
     * Creates a new instance of <code>ScheduleOverlapException</code> without
     * detail message.
     */
    public ScheduleOverlapException() {
    }

    /**
     * Constructs an instance of <code>ScheduleOverlapException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ScheduleOverlapException(String msg) {
        super(msg);
    }
}
