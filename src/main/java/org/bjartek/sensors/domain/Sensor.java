package org.bjartek.sensors.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Sensor  {

    public String name;

    public String location;

    public LocalDateTime time;

    //Sorted list of readings, oldest last.
    public List<SensorReading> readings = new ArrayList<>();

    public Optional<SensorReading> getPreviousValue() {
        if (readings.size() < 2) {
            return Optional.empty();
        }

        SensorReading secondTolast = readings.get(readings.size() - 2);

        return Optional.of(secondTolast);

    }

    public Optional<SensorReading> getCurrentValue() {
        if (readings.isEmpty()) {
            return Optional.empty();
        }

        SensorReading last = readings.get(readings.size() - 1);


        return Optional.of(last);
    }

    public LocalDateTime getLatestUpdated() {
        return getCurrentValue().map(r -> r.time).orElseGet(() -> time);
    }


}
