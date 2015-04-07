package org.bjartek.sensors.domain.dto;

import java.time.LocalDateTime;
import java.util.Optional;

public class SensorReadingDTO extends DTO2O {

    //@Data(prompt = "")
    public LocalDateTime time;

    //@Data(prompt = "")
    //@Template(prompt = "")
    public Double temperature;

    //@Data(prompt = "")
    //@Template(prompt = "")
    public Optional<Double> humidity;

    @Override
    public String getEtag() {
        return time.toString();
    }

}
