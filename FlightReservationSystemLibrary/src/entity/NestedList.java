/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;

/**
 *
 * @author miche
 */

public class NestedList implements Serializable {

    private List<FlightScheduleEntity> innerList;

    public NestedList(List<FlightScheduleEntity> innerList) {
        this.innerList = innerList;
    }
    
    public List<FlightScheduleEntity> getInnerList() {
        return this.innerList;
    }
    
}
