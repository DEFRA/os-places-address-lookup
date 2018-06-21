package uk.gov.ea.address.services.util;

import java.util.List;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import uk.gov.ea.address.services.core.Address;
import uk.gov.ea.address.services.exception.OSPlacesClientException;

public class AddressSearchTest {

    private static OSPlaces osPlaces;
    private static OSPlaces invalidOsPlaces;

    @BeforeClass
    public static void setup() {

        osPlaces = new OSPlaces(
                System.getenv("WCRS_OSPLACES_URL"),
                System.getenv("WCRS_OSPLACES_KEY")
        );

        invalidOsPlaces = new OSPlaces(
                "http://example.com",
                null
        );
    }

    @Test
    public void testCanSearch() throws OSPlacesClientException {
        AddressSearch search = new AddressSearch(osPlaces);
        Assert.assertEquals(true, search.canSearch());
    }

    @Test(expected = Exception.class)
    public void testCanSearchException() throws OSPlacesClientException {

        AddressSearch search = new AddressSearch(invalidOsPlaces);

        search.canSearch();
    }

    @Test
    public void testGetAddresses() throws OSPlacesClientException {
        AddressSearch search = new AddressSearch(osPlaces);

        List<Address> addresses = search.getAddresses("", "SW59BH");

        Assert.assertNotNull(addresses);
        Assert.assertNotSame(0, addresses.size());
    }

    @Test(expected = Exception.class)
    public void testGetAddressesException() throws OSPlacesClientException {

        AddressSearch search = new AddressSearch(osPlaces);

        search.getAddresses("", null);
    }

    @Test(expected = Exception.class)
    public void testGetAddressesInvalid() throws OSPlacesClientException {

        AddressSearch search = new AddressSearch(osPlaces);

        search.getAddresses("", "");
    }

    @Test
    public void testAddressByUPRN() throws OSPlacesClientException {

        AddressSearch search = new AddressSearch(osPlaces);

        Address address = search.getAddress("200010019924");

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
    }

    @Test
    public void testAddressDelimiter() throws OSPlacesClientException {

        AddressSearch search = new AddressSearch(osPlaces);
        Address address = search.getAddress("217026675");

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
        Assert.assertEquals("", address.getOrganisationName());
        Assert.assertEquals("", address.getBuildingNumber());
    }

    @Test(expected = Exception.class)
    public void testAddressInvalid() throws OSPlacesClientException {

        AddressSearch search = new AddressSearch(osPlaces);
        search.getAddress(null);
    }
}
