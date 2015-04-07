package org.bjartek.sensors.domain;

import org.bjartek.sensors.domain.dto.SensorDTO;

import java.util.List;

public interface SensorStore {
    List<SensorDTO> getAllSensors();

    String generateEtag();
}
