package uk.gov.ea.address.services;

import com.codahale.metrics.health.HealthCheckRegistry;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Environment;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

public class AddressLookupApplicationTest {

    @Mock
    private AddressLookupConfiguration addressLookupConfiguration;

    @Mock
    private AddressLookupApplication application;

    @Mock
    private Environment environment;

    private Map<String, String> properties;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        properties = new HashMap<>();
        properties.put("url", System.getenv("WCRS_OSPLACES_URL"));
        properties.put("key", System.getenv("WCRS_OSPLACES_KEY"));
        properties.put("mock", "false");
        properties.put("mockDelay", "150");

        application = new AddressLookupApplication();
    }

    @Test
    public void testGetName() {
        Assert.assertNotNull(application.getName());
        Assert.assertEquals("os-places-address-lookup", application.getName());
    }

    @Test
    public void testRun() throws Exception {
        Mockito.when(addressLookupConfiguration.getProperties()).thenReturn(properties);

        HealthCheckRegistry healthCheckRegistry = Mockito.mock(HealthCheckRegistry.class);
        JerseyEnvironment je = Mockito.mock(JerseyEnvironment.class);
        je.register(Matchers.any());

        Mockito.when(environment.healthChecks()).thenReturn(healthCheckRegistry);
        Mockito.when(environment.jersey()).thenReturn(je);

        application.run(addressLookupConfiguration, environment);
        Mockito.verify(environment, Mockito.times(1)).healthChecks();
        Mockito.verify(environment, Mockito.times(3)).jersey();
    }

    @Test(expected = Test.None.class)
    public void testRunWithArgs() throws Exception {
        AddressLookupApplication.main(new String[] { "server", "configuration.yml" });
    }

}
