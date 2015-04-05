package org.bjartek.domain;

import java.time.LocalDateTime;
import java.util.Optional;

public class SensorReading  {

    public LocalDateTime time = LocalDateTime.now();

    public Double celsius;

    public Optional<Double> humidity;


}
