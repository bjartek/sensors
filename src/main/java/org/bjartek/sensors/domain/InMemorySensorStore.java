package org.bjartek.sensors.domain;

import org.bjartek.sensors.dto.SensorDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;

public class InMemorySensorStore implements SensorStore {

    List<SensorDTO> sensors;

    public InMemorySensorStore() {

        SensorDTO livingRoom = new SensorDTO(){{
            name = "LivingRoom";
            location  = "The shelf above the TV";
            latestHumidity = Optional.of(18.2);
            latestTemperature = Optional.of(20.0);
            latestReading = Optional.of(LocalDateTime.parse("2015-08-04T10:11:30"));
        }};

        SensorDTO outside = new SensorDTO(){{
            name = "Outside";
            location  = "On the wall outside the living room window";
            latestHumidity = Optional.of(17.2);
            latestTemperature = Optional.of(25.0);
            latestReading = Optional.of(LocalDateTime.parse("2015-08-04T10:11:30"));
        }};
        sensors =  asList(livingRoom, outside);
    }

    @Override
    public List<SensorDTO> getAllSensors() {


        return  sensors;
    }

    @Override
    public String generateEtag() {

        SensorDTO last = sensors.get(sensors.size() - 1);
        return last.getEtag();
    }
}
