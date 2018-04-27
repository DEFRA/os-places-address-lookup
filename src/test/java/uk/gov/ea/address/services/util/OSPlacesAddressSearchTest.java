package uk.gov.ea.address.services.util;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import uk.gov.ea.address.services.core.Address;
import uk.gov.ea.address.services.exception.OSPlacesClientException;

public class OSPlacesAddressSearchTest
{

    private OSPlacesAddressSearchImpl osPlacesAddressSearchImpl;

    private String key;

    private String url;

    private String delimiter = ",";

    @Before
    public void init()
    {
        key = System.getenv("WCRS_OSPLACES_KEY");
        url = System.getenv("WCRS_OSPLACES_URL");
        osPlacesAddressSearchImpl = new OSPlacesAddressSearchImpl(url, key, delimiter);
    }

    @Test
    public void testCanSearch() throws OSPlacesClientException
    {
        Assert.assertEquals(true, osPlacesAddressSearchImpl.canSearch());
    }

    @Test
    public void testCanSearchException() throws OSPlacesClientException
    {
        OSPlacesAddressSearchImpl search = new OSPlacesAddressSearchImpl(url, null, delimiter);
        try
        {
            Assert.assertEquals(false, search.checkHealth());
        }
        catch (OSPlacesClientException e)
        {
            Assert.assertNotNull(e.toString());
        }
    }

    @Test
    public void testHealthCheck() throws OSPlacesClientException
    {
        Assert.assertEquals(null, osPlacesAddressSearchImpl.checkHealth());
    }

    @Test
    public void testHealthCheckException() throws OSPlacesClientException
    {
        OSPlacesAddressSearchImpl search = new OSPlacesAddressSearchImpl(url, null, delimiter);
        try
        {
            search.checkHealth();
        }
        catch (OSPlacesClientException e)
        {
            Assert.assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testGetAddresses() throws OSPlacesClientException
    {
        List<Address> addresses = osPlacesAddressSearchImpl.getAddresses("", "SW59BH");

        Assert.assertNotNull(addresses);
        Assert.assertNotSame(0, addresses.size());
    }

    @Test
    public void testGetAddressesException() throws OSPlacesClientException
    {
        try
        {
            osPlacesAddressSearchImpl.getAddresses("", null);
        }
        catch (OSPlacesClientException e)
        {
            Assert.assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testGetAddressesInvalid() throws OSPlacesClientException
    {
        try
        {
            osPlacesAddressSearchImpl.getAddresses("", "");
        }
        catch (OSPlacesClientException e)
        {
            Assert.assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testAddressByUPRN() throws OSPlacesClientException
    {
        Address address = osPlacesAddressSearchImpl.getAddress("200010019924");

        Assert.assertNotNull(address);
        Assert.assertEquals("200010019924", address.getUprn());
        Assert.assertNotNull(address.getPostcode());
        Assert.assertNotNull(address.getTown());
        Assert.assertNotNull(address.getEasting());
        Assert.assertNotNull(address.getMoniker());
        Assert.assertNotNull(address.getNorthing());
        Assert.assertNotNull(address.getPartial());
        Assert.assertNotNull(address.getLines());
        Assert.assertNotNull(address.getOrganisationName());
        Assert.assertNotNull(address.getBuildingName());
        Assert.assertNotNull(address.getBuildingNumber());
        Assert.assertNotNull(address.getThoroughfareName());
        Assert.assertNotNull(address.getSubBuildingName());
        Assert.assertEquals(false, address.getLines().get(0).contains(delimiter));
    }

    @Test
    public void testAddressDelimiter() throws OSPlacesClientException
    {
        Address address = osPlacesAddressSearchImpl.getAddress("217026675");

        Assert.assertNotNull(address);
        Assert.assertEquals("217026675", address.getUprn());
        Assert.assertNotNull(address.getPostcode());
        Assert.assertNotNull(address.getTown());
        Assert.assertNotNull(address.getEasting());
        Assert.assertNotNull(address.getMoniker());
        Assert.assertNotNull(address.getNorthing());
        Assert.assertNotNull(address.getPartial());
        Assert.assertNotNull(address.getLines());
        Assert.assertNotNull(address.getOrganisationName());
        Assert.assertNotNull(address.getBuildingName());
        Assert.assertNotNull(address.getBuildingNumber());
        Assert.assertNotNull(address.getThoroughfareName());
        Assert.assertNotNull(address.getSubBuildingName());
        Assert.assertEquals(address.getThoroughfareName(), address.getLines().get(1));
        Assert.assertEquals(true, address.getLines().get(0).contains(delimiter));
        Assert.assertEquals("", address.getOrganisationName());
        Assert.assertEquals("", address.getBuildingNumber());
    }

    @Test
    public void testAddressInvalid() throws OSPlacesClientException
    {
        try
        {
            osPlacesAddressSearchImpl.getAddress(null);
        }
        catch (OSPlacesClientException ex)
        {
            Assert.assertNotNull(ex.getMessage());
        }
    }

}
