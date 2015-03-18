package uk.gov.ea.address.services.health;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.codahale.metrics.health.HealthCheck.Result;

import uk.gov.ea.address.services.util.OSPlacesAddressSearchImpl;

public class OSPlacesHealthCheckTest
{
    @Mock
    private OSPlacesAddressSearchImpl addressSearchImpl;
    
    private OSPlacesHealthCheck osPlacesHealthCheck;
    
    @Before
    public void init()
    {
        MockitoAnnotations.initMocks(this);
        osPlacesHealthCheck = new OSPlacesHealthCheck(addressSearchImpl);
    }
    
    @Test
    public void testCheck() throws Exception{
        
        Mockito.when(addressSearchImpl.checkHealth()).thenReturn(null);
        Assert.assertEquals(Result.healthy(), osPlacesHealthCheck.check());   
    }
    
    @Test
    public void testCheckInvalid() throws Exception{
        
        Mockito.when(addressSearchImpl.checkHealth()).thenReturn("");
        Assert.assertEquals(Result.unhealthy(""), osPlacesHealthCheck.check());   
    }
}
