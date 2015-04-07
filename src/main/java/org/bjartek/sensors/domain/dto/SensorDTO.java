package org.bjartek.sensors.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Optional;

public class SensorDTO extends DTO2O {

    //@Data(prompt = "")
    //@Href(prefix = "/sensor")
    //@Template(prompt = "")
    public String name;

    //@Data(prompt = "")
    //@Template(prompt = "")
    public String location;

    //@Data(prompt = "")
    public LocalDateTime time;

    //@Data(prompt = "")
    public Double temperature;

    //@Data(prompt = "")
    public Optional<Double> humidity;

    //@Link(prompt = "Sensor details"
    //public String details = "/readings"

    @Override
    public String getEtag() {
        return (name + time.toString()).hashCode() + "";
    }
}
