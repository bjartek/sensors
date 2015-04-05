package org.bjartek;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SensorResourceTest {

    private HttpServer server;
    private WebTarget target;

    @Before
    public void setUp() throws Exception {
        // start the server
        server = Main.startServer();
        // create the client
        Client c = ClientBuilder.newClient();

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
