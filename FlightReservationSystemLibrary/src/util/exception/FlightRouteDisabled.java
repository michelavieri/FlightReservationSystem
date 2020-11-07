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
public class FlightRouteDisabled extends Exception {

    /**
     * Creates a new instance of <code>FlightRouteDisabled</code> without detail
     * message.
     */
    public FlightRouteDisabled() {
    }

    /**
     * Constructs an instance of <code>FlightRouteDisabled</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public FlightRouteDisabled(String msg) {
        super(msg);
    }
}
