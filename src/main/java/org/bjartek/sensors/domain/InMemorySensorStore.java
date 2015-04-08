package org.bjartek.sensors.domain;

import net.hamnaberg.json.Template;
import net.hamnaberg.json.Value;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;

public class InMemorySensorStore implements SensorStore {


    public static List<Sensor> sensors = new ArrayList();

    public InMemorySensorStore() {


        if(sensors.isEmpty()) {
            //TODO: Here we should really store sensors and SensorReadings and then map them to DTO's when we ask for sensors.
            Sensor livingRoom = new Sensor() {{
                name = "LivingRoom";
                location = "The shelf above the TV";
                time = LocalDateTime.parse("2015-08-04T10:11:30");

                readings = asList(new SensorReading() {{
                    time = LocalDateTime.parse("2015-08-04T10:11:30");
                    temperature = 20.0;
                    humidity = Optional.of(10.0);
                }});
            }};

            Sensor outside = new Sensor() {{
                name = "Outside";
                location = "On the wall outside the living room window";
                time = LocalDateTime.parse("2015-08-04T10:11:30");

                readings = asList(new SensorReading() {{
                    time = LocalDateTime.parse("2015-08-04T10:12:30");
                    temperature = 22.0;
                    humidity = Optional.of(12.0);
                }});
            }};

            sensors.add(livingRoom);
            sensors.add(outside);
        }
    }

    @Override
    public List<Sensor> getAllSensors() {

        return sensors;
    }

    @Override
    public String generateEtag() {

        if (sensors.isEmpty()) {
            return "";
        }

        Sensor last = sensors.get(sensors.size() - 1);
        return last.getLatestUpdated().hashCode() + "";
    }

    @Override
    public Optional<Sensor> addSensor(Template template) {

        Optional<Value> newName = template.propertyByName("name").flatMap(p -> p.getValue());
        Optional<Value> newLocation = template.propertyByName("location").flatMap(p -> p.getValue());
        if (!newName.isPresent() || !newLocation.isPresent()) {
            return Optional.of(null);

        }

        //TODO: Need to check for duplicate name

        String nameOfSensor = newName.get().asString();
        if(sensors.stream().anyMatch(s -> s.name == nameOfSensor)) {
            //Throw exception here?
        }

        Sensor s = new Sensor() {{
            name = newName.get().asString();
            location = newLocation.get().asString();
            time = LocalDateTime.now();
        }};

        sensors.add(s);
        return Optional.of(s);


    }

    @Override
    public boolean deleteSensor(String name) {

        return sensors.removeIf(s -> s.name.equalsIgnoreCase(name));

    }

    @Override
    public boolean updateSensor(String name, Template template) {
        Optional<Value> newName = template.propertyByName("name").flatMap(p -> p.getValue());
        Optional<Value> newLocation = template.propertyByName("location").flatMap(p -> p.getValue());

        if (!newName.isPresent() || !newLocation.isPresent()) {
            return false;
        }

         sensors.stream().filter(s -> s.name.equalsIgnoreCase(name)).forEach(s -> {

             s.name =  newName.get().asString();
             s.location = newLocation.get().asString();
             s.time = LocalDateTime.now();
         });


        return true;
    }
}
