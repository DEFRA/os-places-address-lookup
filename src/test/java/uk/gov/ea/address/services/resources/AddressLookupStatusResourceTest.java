package uk.gov.ea.address.services.resources;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.gov.ea.address.services.core.AddressLookupStatus;
import uk.gov.ea.address.services.exception.OSPlacesClientException;
import uk.gov.ea.address.services.resources.AddressLookupStatusResource;
import uk.gov.ea.address.services.util.OSPlacesAddressSearchImpl;

public class AddressLookupStatusResourceTest
{

    @Mock
    private OSPlacesAddressSearchImpl osPlacesAddressSearchImpl;

    private AddressLookupStatusResource resource;

    @Before
    public void init()
    {
        MockitoAnnotations.initMocks(this);
        resource = new AddressLookupStatusResource(osPlacesAddressSearchImpl);
    }

    @Test
    public void testCanSearch() throws OSPlacesClientException
    {
        Mockito.when(osPlacesAddressSearchImpl.canSearch()).thenReturn(true);
        AddressLookupStatus status = resource.getAddress(null);

        Assert.assertNotNull(status);
        Assert.assertEquals(true, status.isAvailable());
    }

    @Test
    public void testCanSearchInvalid() throws OSPlacesClientException
    {
        Mockito.when(osPlacesAddressSearchImpl.canSearch()).thenReturn(false);
        AddressLookupStatus status = resource.getAddress(null);

        Assert.assertNotNull(status);
        Assert.assertEquals(false, status.isAvailable());
    }

}
