package uk.gov.ea.address.services;

import java.util.Map;

import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.gov.ea.address.services.health.OSPlacesHealthCheck;
import uk.gov.ea.address.services.resources.AddressLookupStatusResource;
import uk.gov.ea.address.services.resources.AddressResource;
import uk.gov.ea.address.services.resources.SearchResultsResource;
import uk.gov.ea.address.services.util.OSPlacesAddressSearchImpl;

public class AddressLookupApplication extends Application<AddressLookupConfiguration>
{
    private static final String APPLICATION_NAME = "address-lookup-osplaces";

    private static Logger logger = LoggerFactory.getLogger(AddressLookupApplication.class);

    @Override
    public void initialize(Bootstrap<AddressLookupConfiguration> bootstrap)
    {
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );
    }

    public static void main(String[] args) throws Exception
    {
        new AddressLookupApplication().run(args);
    }

    @Override
    public String getName()
    {
        return APPLICATION_NAME;
    }

    @Override
    public void run(AddressLookupConfiguration configuration, Environment environment) throws Exception
    {
        Map<String, String> properties = configuration.getProperties();

        String url = properties.get("url");
        String delimiter = properties.get("delimiter");

        logger.info("url - {}", url);
        logger.info("delimiter - {}", delimiter);

        OSPlacesAddressSearchImpl osAddressSearch = new OSPlacesAddressSearchImpl(url, properties.get("key"), delimiter);

        environment.healthChecks().register("osAddressSearch", new OSPlacesHealthCheck(osAddressSearch));
        environment.jersey().register(new AddressResource(osAddressSearch));
        environment.jersey().register(new SearchResultsResource(osAddressSearch));
        environment.jersey().register(new AddressLookupStatusResource(osAddressSearch));
    }

}
