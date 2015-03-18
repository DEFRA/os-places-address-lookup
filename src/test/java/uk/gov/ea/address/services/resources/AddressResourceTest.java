package uk.gov.ea.address.services.resources;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.gov.ea.address.services.core.Address;
import uk.gov.ea.address.services.exception.OSPlacesClientException;
import uk.gov.ea.address.services.util.OSPlacesAddressSearchImpl;

public class AddressResourceTest
{

    @Mock
    private OSPlacesAddressSearchImpl osPlacesAddressSearchImpl;

    @Mock
    private Address address;

    private AddressResource resource;

    @Before
    public void init()
    {
        MockitoAnnotations.initMocks(this);
        resource = new AddressResource(osPlacesAddressSearchImpl);
    }

    @Test
    public void testGetAddress() throws OSPlacesClientException
    {
        Mockito.when(address.getUprn()).thenReturn("20001");
        Mockito.when(osPlacesAddressSearchImpl.getAddress("20001")).thenReturn(address);

        Address expectedAddress = (Address) resource.getAddress("20001").getEntity();
        Assert.assertNotNull(expectedAddress);
        Assert.assertEquals("20001", expectedAddress.getUprn());
    }

    @Test
    public void testGetAddressInvalid() throws OSPlacesClientException
    {
        try
        {
            Mockito.when(address.getUprn()).thenReturn("");
            Mockito.when(osPlacesAddressSearchImpl.getAddress("")).thenThrow(OSPlacesClientException.class);
            resource.getAddress("");
        }
        catch (OSPlacesClientException e)
        {
            Assert.assertNotNull(e.getMessage());
        }
    }
    
    @Test
    public void testGetAddressNull() throws OSPlacesClientException
    {
        try
        {
            Mockito.when(address.getUprn()).thenReturn(null);
            Mockito.when(osPlacesAddressSearchImpl.getAddress(null)).thenThrow(OSPlacesClientException.class);
            resource.getAddress(null);
        }
        catch (OSPlacesClientException e)
        {
            Assert.assertNotNull(e.getMessage());
        }
    }
}
