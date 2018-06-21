package uk.gov.ea.address.services.exception;

import org.junit.Test;

import static org.junit.Assert.*;

public class OSPlacesClientExceptionTest {

    @Test
    public void testOSPlacesExceptionWithMessage() {

        OSPlacesClientException ex = new OSPlacesClientException("test");

        assertEquals("test", ex.getMessage());
        assertEquals("test", ex.toString());
        assertNull(ex.getCause());
    }

    @Test
    public void testOSPlacesExceptionWithCauseAndMessage() {

        Throwable cause = new Exception("message");
        OSPlacesClientException ex = new OSPlacesClientException("test", cause);

        assertNotNull(ex.getCause());
        assertEquals("test", ex.getMessage());
    }
}
