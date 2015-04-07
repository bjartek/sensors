package org.bjartek.sensors.domain;

import org.bjartek.sensors.domain.dto.SensorDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;

public class InMemorySensorStore implements SensorStore {

    List<SensorDTO> sensors;

    public InMemorySensorStore() {


        //TODO: Here we should really store sensors and SensorReadings and then map them to DTO's when we ask for sensors.
        SensorDTO livingRoom = new SensorDTO(){{
            name = "LivingRoom";
            location  = "The shelf above the TV";
            humidity = Optional.of(18.2);
            temperature = 20.0;
            time = LocalDateTime.parse("2015-08-04T10:11:30");
        }};

        SensorDTO outside = new SensorDTO(){{
            name = "Outside";
            location  = "On the wall outside the living room window";
            humidity = Optional.of(17.2);
            temperature = 25.0;
            time = LocalDateTime.parse("2015-08-04T10:11:30");
        }};
        sensors =  asList(livingRoom, outside);
    }

    @Override
    public List<SensorDTO> getAllSensors() {

        return  sensors;
    }

    @Override
    public String generateEtag() {

        if(sensors.isEmpty()) {
            return "";
        }

        SensorDTO last = sensors.get(sensors.size() - 1);
        return last.getEtag();
    }
}
