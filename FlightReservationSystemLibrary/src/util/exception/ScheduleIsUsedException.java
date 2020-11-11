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
public class ScheduleIsUsedException extends Exception {

    /**
     * Creates a new instance of <code>ScheduleIsUsedException</code> without
     * detail message.
     */
    public ScheduleIsUsedException() {
    }

    /**
     * Constructs an instance of <code>ScheduleIsUsedException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ScheduleIsUsedException(String msg) {
        super(msg);
    }
}
