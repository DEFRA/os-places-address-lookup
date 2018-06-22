package uk.gov.ea.address.services;

import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.ea.address.services.health.OSPlacesHealthCheck;
import uk.gov.ea.address.services.resources.SearchResource;
import uk.gov.ea.address.services.util.IOSPlaces;
import uk.gov.ea.address.services.util.OSPlaces;
import uk.gov.ea.address.services.util.AddressSearch;
import uk.gov.ea.address.services.util.OSPlacesMock;

import java.util.Map;

public class AddressLookupApplication extends Application<AddressLookupConfiguration> {

    private static Logger logger = LoggerFactory.getLogger(AddressLookupApplication.class);
    private static final String APPLICATION_NAME = "os-places-address-lookup";

    @Override
    public void initialize(Bootstrap<AddressLookupConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );
    }

    public static void main(String[] args) throws Exception {
        new AddressLookupApplication().run(args);
    }

    @Override
    public String getName() {
        return APPLICATION_NAME;
    }

    @Override
    public void run(AddressLookupConfiguration configuration, Environment environment) throws Exception {

        Map<String, String> properties = configuration.getProperties();

        if (Boolean.valueOf(properties.get("mock"))) logger.info("Mocking is enabled!");

        IOSPlaces osPlaces = generateOSPlaces(properties);
        AddressSearch osAddressSearch = new AddressSearch(osPlaces);

        environment.healthChecks().register("osAddressSearch", new OSPlacesHealthCheck(osPlaces));
        environment.jersey().register(new SearchResource(osAddressSearch));
    }

    private IOSPlaces generateOSPlaces(Map<String, String> properties) {

        Boolean mock = Boolean.valueOf(properties.get("mock"));

        if (mock) return new OSPlacesMock(Integer.valueOf(properties.get("mockDelay")));

        return new OSPlaces(properties.get("url"), properties.get("key"));
    }
}
