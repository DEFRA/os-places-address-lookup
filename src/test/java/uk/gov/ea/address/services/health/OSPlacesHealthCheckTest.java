package uk.gov.ea.address.services.health;

import com.codahale.metrics.health.HealthCheck.Result;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import uk.gov.ea.address.services.util.OSPlaces;

public class OSPlacesHealthCheckTest
{
    @Mock
    private OSPlaces osPlaces;
    
    private OSPlacesHealthCheck osPlacesHealthCheck;
    
    @Before
    public void init() {

        MockitoAnnotations.initMocks(this);
        osPlacesHealthCheck = new OSPlacesHealthCheck(osPlaces);
    }

    @Test
    public void testCheck() throws Exception {
        
        Mockito.when(osPlaces.health()).thenReturn(null);
        Assert.assertEquals(
                Result.healthy().isHealthy(),
                osPlacesHealthCheck.check().isHealthy()
        );
    }

    @Test
    public void testCheckInvalid() throws Exception {
        
        Mockito.when(osPlaces.health()).thenReturn("");

        Assert.assertEquals(
                Result.unhealthy("").isHealthy(),
                osPlacesHealthCheck.check().isHealthy()
        );
    }
}
