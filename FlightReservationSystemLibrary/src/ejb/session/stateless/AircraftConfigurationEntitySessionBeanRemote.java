/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftConfigurationEntity;
import entity.CabinClassConfigurationEntity;
import entity.FlightEntity;
import java.util.List;
import javax.ejb.Remote;
import util.exception.AircraftConfigurationNotFoundException;

/**
 *
 * @author miche
 */
@Remote
public interface AircraftConfigurationEntitySessionBeanRemote {

    AircraftConfigurationEntity createAircraftConfiguration(AircraftConfigurationEntity newAircraftConfig);

    List<AircraftConfigurationEntity> retrieveAllAircraftConfigurations();

    AircraftConfigurationEntity retrieveAircraftTypeByCode(String code) throws AircraftConfigurationNotFoundException;

    List<FlightEntity> getFlightEntities(AircraftConfigurationEntity aircraftConfig);

    List<CabinClassConfigurationEntity> getCabinClassConfig(AircraftConfigurationEntity aircraftConfig);

    public List<CabinClassConfigurationEntity> getCabinClassConfigUnmanaged(AircraftConfigurationEntity aircraftConfig);
}
