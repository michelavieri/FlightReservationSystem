/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FlightScheduleEntity;
import java.util.List;

/**
 *
 * @author miche
 */
class NestedList {

    private List<FlightScheduleEntity> innerList;

    public NestedList(List<FlightScheduleEntity> innerList) {
        this.innerList = innerList;
    }
    
    public List<FlightScheduleEntity> getInnerList() {
        return this.innerList;
    }
}
