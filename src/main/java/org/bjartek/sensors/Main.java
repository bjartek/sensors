package org.bjartek.sensors;

import org.bjartek.sensors.domain.InMemorySensorStore;
import org.bjartek.sensors.domain.SensorStore;
import org.bjartek.sensors.utility.CollectionJsonReaderAndWriter;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.message.DeflateEncoder;
import org.glassfish.jersey.message.GZipEncoder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.EncodingFilter;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Main class.
 *
 */
public class Main {
    // Base URI the Grizzly HTTP server will listen on
    public static final URI BASE_URI = URI.create("http://localhost:8080/sensors/");

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() throws IOException {

        SLF4JBridgeHandler.removeHandlersForRootLogger();
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.install();

        Logger global = Logger.getGlobal();
        global.setLevel(Level.FINEST);
        global.fine("Test JUL to slf4j bridge");


        // create a resource config that scans for JAX-RS resources and providers
        // in com.example package
        final ResourceConfig rc = new ResourceConfig()
                .packages("org.bjartek.sensors.api")
                .register(new AbstractBinder() {
                    @Override
                    protected void configure() {
                        bind(InMemorySensorStore.class).to(SensorStore.class);
                    }
        })
                .register(CollectionJsonReaderAndWriter.class)
                .registerClasses(
                        EncodingFilter.class,
                GZipEncoder.class,
                DeflateEncoder.class);


        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);

    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
        System.in.read();
        server.stop();
    }
}

