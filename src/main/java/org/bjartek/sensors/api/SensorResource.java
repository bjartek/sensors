package org.bjartek.sensors.api;

import net.hamnaberg.json.*;
import net.hamnaberg.json.Collection;
import net.hamnaberg.json.Link;
import net.hamnaberg.json.MediaType;
import org.bjartek.sensors.Main;
import org.bjartek.sensors.domain.Sensor;
import org.bjartek.sensors.domain.SensorReading;
import org.bjartek.sensors.domain.SensorStore;
import org.bjartek.sensors.domain.dto.DTO2O;
import org.bjartek.sensors.domain.dto.SensorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.*;
import java.util.logging.Level;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Path("/")
@Produces("application/vnd.collection+json")
@Consumes("application/vnd.collection+json")
public class SensorResource {
    static final Logger LOG = LoggerFactory.getLogger(SensorResource.class);

    @Inject
    SensorStore sensorStore;

    private static final URI COLLECTION_URI = Main.BASE_URI;

    @GET
    public Response list(@Context Request request, @Context UriInfo info) {

        Template tpl = Template.create(asList(Property.template("name"), Property.template("location")));

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


    @PUT
    @Path("/sensor/{name}")
    public Response update(@Context UriInfo info, @PathParam("name") String name, Template template) {

        if (sensorStore.updateSensor(name, template)) {
            return Response.ok().build();
        } else {
            return Response.serverError().build();
        }

    }

    @DELETE
    @Path("/sensor/{name}")
    public Response delete(@Context UriInfo info, @PathParam("name") String name) {

        if (sensorStore.deleteSensor(name)) {
            return Response.noContent().build();
        } else {
            return Response.serverError().build();
        }

    }

    @POST
    public Response create(@Context UriInfo info, Template template) {

        Optional<Sensor> newSensor = sensorStore.addSensor(template);

        if (newSensor.isPresent()) {
            return Response.created(info.getBaseUriBuilder().path("/sensor/{name}").build(newSensor.get().name)).build();
        }
        LOG.info("Noe galt skjedde.");


        return Response.serverError().build();


    }

    private List<Item> loadItems() {
        return sensorStore.getAllSensors().stream().map(r -> createItem(r)).collect(toList());
    }

    private Item createItem(Sensor sensor) {

        String itemUrl = "sensor/" + sensor.name;

        String readingsUrl = itemUrl + "/readings";
        Link readings = Link.create(COLLECTION_URI.resolve(readingsUrl), "details", Optional.of("Sensor details"));

        List<Property> props = new ArrayList<>();
        props.add(Property.value("name", sensor.name));
        props.add(Property.value("location", sensor.location));

        Optional<SensorReading> currentValue = sensor.getCurrentValue();

        if (currentValue.isPresent()) {
            SensorReading cv = currentValue.get();
            props.add(Property.value("temperature", cv.temperature));
            props.add(Property.value("time", cv.time));
            if (cv.humidity.isPresent()) {
                props.add(Property.value("humidity", cv.humidity.get()));
            }
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
