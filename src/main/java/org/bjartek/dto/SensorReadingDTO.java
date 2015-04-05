package org.bjartek.dto;

import java.time.LocalDateTime;
import java.util.Optional;

public class SensorReadingDTO extends DTO2O {

    public LocalDateTime latestReading;
    public Double latestTemperature;
    public Optional<Double> latestHumidity;

    @Override
    public String getEtag() {
        return latestReading.toString();
    }

}
