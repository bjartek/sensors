package org.bjartek.sensors.domain;

import org.bjartek.sensors.dto.SensorDTO;

import java.util.List;

public interface SensorStore {
    List<SensorDTO> getAllSensors();

    String generateEtag();
}
