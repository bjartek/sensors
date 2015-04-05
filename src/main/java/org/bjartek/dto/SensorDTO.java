package org.bjartek.dto;


import java.time.LocalDateTime;
import java.util.Optional;

public class SensorDTO extends DTO2O {

    public String name;
    public String location;

    public Optional<LocalDateTime> latestReading;
    public Optional<Double> latestTemperature;
    public Optional<Double> latestHumidity;

    @Override
    public String getEtag() {
        return name + latestReading.map(r -> latestReading.toString()).get();
    }
}
