package org.bjartek.api;

import net.hamnaberg.json.*;
import net.hamnaberg.json.Collection;
import net.hamnaberg.json.Link;
import org.bjartek.Main;
import org.bjartek.dto.DTO2O;
import org.bjartek.dto.SensorDTO;

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

    private static final URI COLLECTION_URI = Main.BASE_URI;

    private List<? extends DTO2O> resource;

    public SensorResource() {


        SensorDTO livingRoom = new SensorDTO(){{
            name = "LivingRoom";
            location  = "The shelf above the TV";
            latestHumidity = Optional.of(18.2);
            latestTemperature = Optional.of(20.0);
            latestReading = Optional.of(LocalDateTime.parse("2015-08-04T10:11:30"));
        }};

        SensorDTO outside = new SensorDTO(){{
            name = "Outside";
            location  = "On the wall outside the living room window";
            latestHumidity = Optional.of(17.2);
            latestTemperature = Optional.of(25.0);
            latestReading = Optional.of(LocalDateTime.parse("2015-08-04T10:11:30"));
        }};
        resource =  asList(livingRoom, outside);

    }


    @GET
    public Response list(@Context Request request, @Context UriInfo info) {


        Template tpl = Template.create(asList(Property.template("name"),Property.template("location") ));

        List<Item> items = loadItems();

        SensorDTO last = (SensorDTO) resource.get(resource.size() - 1);

        Collection collection = Collection.create(info.getRequestUri(), loadLinks(), items, loadQueries(), tpl, null);

        System.out.println("start list of sensors");
        EntityTag tag = new EntityTag(last.getEtag());

        CacheControl cacheControl = new CacheControl();
        cacheControl.setMaxAge(1200);

        Response.ResponseBuilder rb;

        rb = request.evaluatePreconditions(tag);
        if (rb != null) {
            return rb.cacheControl(cacheControl).tag(tag).build();
        } else {
            return Response.ok(collection).cacheControl(cacheControl).tag(tag).build();
        }
    }

    private List<Item> loadItems() {
        return resource.stream().map(r ->createItem(r)).collect(toList());
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
