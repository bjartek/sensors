package org.bjartek.sensors;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Response;

import net.hamnaberg.json.Collection;
import net.hamnaberg.json.MediaType;
import net.hamnaberg.json.Property;
import net.hamnaberg.json.Template;
import org.bjartek.sensors.utility.CollectionJsonReaderAndWriter;
import org.glassfish.grizzly.http.server.HttpServer;

import org.glassfish.jersey.client.ClientConfig;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class SensorResourceTest {

    static final Logger LOG = LoggerFactory.getLogger(SensorResourceTest.class);

    private HttpServer server;
    private WebTarget target;

    @Before
    public void setUp() throws Exception {
        // start the server
        server = Main.startServer();
        // create the client
        Client c = ClientBuilder.newClient(new ClientConfig()
                .register(CollectionJsonReaderAndWriter.class));

        // uncomment the following line if you want to enable
        // support for JSON in the client (you also have to uncomment
        // dependency on jersey-media-json module in pom.xml and Main.startServer())
        // --
        // c.configuration().enable(new org.glassfish.jersey.media.json.JsonJaxbFeature());

        target = c.target(Main.BASE_URI);
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }


    @Test
    public void testShouldBeAbleToAddNewSensor() {

        List<Property> data = asList(Property.value("name", "TestSensor"), Property.value("location", "TestLocation"));
        Template sensor = Template.create(data);

        Collection col = Collection.builder().withTemplate(sensor).build();

        LOG.info(col.toString());
        Response resp = target.request("application/vnd.collection+json").post(Entity.entity(col, MediaType.COLLECTION_JSON));



    }

    @Test
    public void testRepeatedRequestWithEtagShouldReturn304() throws InterruptedException {
        WebTarget webTarget = target.path("");

        Response head = webTarget.request().get();
        EntityTag eTag = head.getEntityTag();
        Assert.assertEquals(200, head.getStatus());
        Thread.sleep(1000);

        Response head1 = webTarget.request().header("If-None-Match", eTag).get();
        Assert.assertEquals(304, head1.getStatus());
    }

}
