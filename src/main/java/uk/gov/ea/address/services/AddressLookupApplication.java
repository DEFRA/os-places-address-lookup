package uk.gov.ea.address.services;

import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import uk.gov.ea.address.services.health.OSPlacesHealthCheck;
import uk.gov.ea.address.services.resources.AddressLookupStatusResource;
import uk.gov.ea.address.services.resources.AddressResource;
import uk.gov.ea.address.services.resources.SearchResultsResource;
import uk.gov.ea.address.services.util.IOSPlaces;
import uk.gov.ea.address.services.util.OSPlaces;
import uk.gov.ea.address.services.util.AddressSearch;
import uk.gov.ea.address.services.util.OSPlacesMock;

import java.util.Map;

public class AddressLookupApplication extends Application<AddressLookupConfiguration> {

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

        IOSPlaces osPlaces = generateOSPlaces(properties);
        AddressSearch osAddressSearch = new AddressSearch(osPlaces);

        environment.healthChecks().register("osAddressSearch", new OSPlacesHealthCheck(osPlaces));
        environment.jersey().register(new AddressResource(osAddressSearch));
        environment.jersey().register(new SearchResultsResource(osAddressSearch));
        environment.jersey().register(new AddressLookupStatusResource(osAddressSearch));
    }

    private IOSPlaces generateOSPlaces(Map<String, String> properties) {

        Boolean mock = Boolean.valueOf(properties.get("mock"));

        if (mock) return new OSPlacesMock(Integer.valueOf(properties.get("mockDelay")));

        return new OSPlaces(properties.get("url"), properties.get("key"));
    }
}
