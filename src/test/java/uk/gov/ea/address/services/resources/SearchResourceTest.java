package uk.gov.ea.address.services.resources;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.gov.ea.address.services.core.Address;
import uk.gov.ea.address.services.exception.OSPlacesClientException;
import uk.gov.ea.address.services.util.AddressSearch;

public class SearchResultsResourceTest
{

    @Mock
    private AddressSearch osPlacesAddressSearchImpl;

    private SearchResultsResource resource;

    @Mock
    private Address address;

    @Before
    public void init()
    {
        MockitoAnnotations.initMocks(this);
        resource = new SearchResultsResource(osPlacesAddressSearchImpl);
    }

    @Test
    public void testGetAddresses() throws OSPlacesClientException
    {

        List<Address> addresses = new ArrayList<Address>();
        Mockito.when(address.getUprn()).thenReturn("20001");
        Mockito.when(address.getPostcode()).thenReturn("SW59BH");
        addresses.add(address);
        Mockito.when(osPlacesAddressSearchImpl.getAddresses("", "SW59BH")).thenReturn(addresses);

        List<Address> exAddresses = (List<Address>) resource.getAddresses("", "SW59BH").getEntity();
        Assert.assertNotNull(exAddresses);
        Assert.assertEquals(1, exAddresses.size());

        Assert.assertEquals("20001", exAddresses.get(0).getUprn());
        Assert.assertEquals("SW59BH", exAddresses.get(0).getPostcode());
    }

    @Test
    public void testGetNullAddresses() throws OSPlacesClientException
    {
        Mockito.when(osPlacesAddressSearchImpl.getAddresses("", "SW59BH")).thenReturn(null);
        List<Address> exAddresses = (List<Address>) resource.getAddresses("", "SW59BH").getEntity();
        Assert.assertNull(exAddresses);
    }

    @Test
    public void testAddressesWithEmpty()
    {
        try
        {
            Mockito.when(osPlacesAddressSearchImpl.getAddresses("", "")).thenThrow(OSPlacesClientException.class);
            resource.getAddresses("", "");
        }
        catch (OSPlacesClientException e)
        {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testAddressesWithNull()
    {
        try
        {
            Mockito.when(osPlacesAddressSearchImpl.getAddresses(null, null)).thenThrow(OSPlacesClientException.class);
            resource.getAddresses(null, null);
        }
        catch (OSPlacesClientException e)
        {
            Assert.fail(e.getMessage());
        }
    }
}
