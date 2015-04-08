package org.bjartek.sensors.domain;

import net.hamnaberg.json.Template;
import org.bjartek.sensors.domain.dto.SensorDTO;

import java.util.List;
import java.util.Optional;

public interface SensorStore {
    List<Sensor> getAllSensors();

    String generateEtag();

    Optional<Sensor> addSensor(Template template);

    boolean deleteSensor(String name);

    boolean updateSensor(String name, Template template);
}
