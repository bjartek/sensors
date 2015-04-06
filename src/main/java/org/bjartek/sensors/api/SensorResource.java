package org.bjartek.sensors.api;

import net.hamnaberg.json.*;
import net.hamnaberg.json.Collection;
import net.hamnaberg.json.Link;
import org.bjartek.sensors.Main;
import org.bjartek.sensors.domain.SensorStore;
import org.bjartek.sensors.dto.DTO2O;
import org.bjartek.sensors.dto.SensorDTO;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Path("/")
@Produces("application/vnd.collection+json")
@Consumes("application/vnd.collection+json")
public class SensorResource {

    @Inject
    SensorStore sensorStore;

    private static final URI COLLECTION_URI = Main.BASE_URI;

    @GET
    public Response list(@Context Request request, @Context UriInfo info) {

        Template tpl = Template.create(asList(Property.template("name"),Property.template("location") ));

        Collection collection = Collection.create(info.getRequestUri(), loadLinks(), loadItems(), loadQueries(), tpl, null);

        EntityTag tag = new EntityTag(sensorStore.generateEtag());

        CacheControl cacheControl = new CacheControl();
        cacheControl.setMaxAge(1200);

        Response.ResponseBuilder rb = request.evaluatePreconditions(tag);
        if (rb != null) {
            return rb.cacheControl(cacheControl).tag(tag).build();
        } else {
            return Response.ok(collection).cacheControl(cacheControl).tag(tag).build();
        }
    }

    private List<Item> loadItems() {
        return sensorStore.getAllSensors().stream().map(r ->createItem(r)).collect(toList());
    }

    private Item createItem(DTO2O r) {
        SensorDTO sensor = (SensorDTO) r;

        String itemUrl =  "sensor/" + sensor.name;

        String readingsUrl = itemUrl + "/readings";
        Link readings = Link.create(COLLECTION_URI.resolve(readingsUrl), "details", Optional.of("Sensor details"));

        List<Property> props = new ArrayList<>();
        props.add(Property.value("name", sensor.name));
        props.add(Property.value("location", sensor.location));

        if(sensor.latestHumidity.isPresent()) {
            props.add(Property.value("humidity", sensor.latestHumidity));
        }

        if(sensor.latestTemperature.isPresent()) {
            props.add(Property.value("temperature", sensor.latestTemperature));

        }

        if(sensor.latestReading.isPresent()) {
            props.add(Property.value("time", sensor.latestReading));
        }

        return Item.create(COLLECTION_URI.resolve(itemUrl), props, asList(readings));

    }

    private List<Query> loadQueries() {
        return emptyList();
    }


    private List<Link> loadLinks() {
        return emptyList();
    }

}
