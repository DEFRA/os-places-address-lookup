package uk.gov.ea.address.services.exception;

import junit.framework.Assert;

import org.junit.Test;

public class OSPlacesClientExceptionTest
{
    @Test
    public void testOSPlacesExceptionWithMessage()
    {
        OSPlacesClientException ex = new OSPlacesClientException("test");
        Assert.assertEquals("test", ex.getMessage());
        Assert.assertEquals("test", ex.toString());
        Assert.assertNull(ex.getCause());
    }

    @Test
    public void testOSPlacesExceptionWithCauseAndMessage()
    {
        Throwable cause = new Exception("message");
        OSPlacesClientException ex = new OSPlacesClientException("test", cause);
        Assert.assertNotNull(ex.getCause());
        Assert.assertEquals("test", ex.getMessage());
    }
}
