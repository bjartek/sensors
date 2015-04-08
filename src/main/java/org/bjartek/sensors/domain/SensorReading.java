package org.bjartek.sensors.domain;

import java.time.LocalDateTime;
import java.util.Optional;

public class SensorReading  {

    public LocalDateTime time = LocalDateTime.now();

    public Double temperature;

    public Optional<Double> humidity;


}
