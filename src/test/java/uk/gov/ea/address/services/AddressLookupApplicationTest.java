package uk.gov.ea.address.services;

import java.util.HashMap;
import java.util.Map;

import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Environment;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.codahale.metrics.health.HealthCheckRegistry;

public class AddressLookupApplicationTest
{
    @Mock
    private AddressLookupConfiguration addressLookupConfiguration;

    @Mock
    private AddressLookupApplication application;

    @Mock
    private Environment environment;

    private String key;

    private String url;

    @Before
    public void init()
    {
        MockitoAnnotations.initMocks(this);
        key = System.getenv("WCRS_OSPLACES_KEY");
        url = System.getenv("WCRS_OSPLACES_URL");
        application = new AddressLookupApplication();
    }

    @Test
    public void testGetName()
    {
        Assert.assertNotNull(application.getName());
        Assert.assertEquals("os-places-address-lookup", application.getName());
    }

    @Test
    public void testRun() throws Exception
    {

        Mockito.when(addressLookupConfiguration.getProperties()).thenReturn(getPropertyMap());

        HealthCheckRegistry healthCheckRegistry = Mockito.mock(HealthCheckRegistry.class);
        JerseyEnvironment je = Mockito.mock(JerseyEnvironment.class);
        je.register(Matchers.any());

        Mockito.when(environment.healthChecks()).thenReturn(healthCheckRegistry);
        Mockito.when(environment.jersey()).thenReturn(je);

        application.run(addressLookupConfiguration, environment);
        Mockito.verify(environment, Mockito.times(1)).healthChecks();
        Mockito.verify(environment, Mockito.times(3)).jersey();
    }

    @Test
    public void testRunWithArgs()
    {
        try
        {
            AddressLookupApplication.main(new String[] { "server", "configuration.yml" });
        }
        catch (Exception ex)
        {
            Assert.fail(ex.toString());
        }
    }

    private Map<String, String> getPropertyMap()
    {
        return new HashMap<String, String>()
        {
            private static final long serialVersionUID = 1L;
            {
                put("key", key);
                put("url", url);
                put("delimiter", ",");
            }
        };
    }
}
