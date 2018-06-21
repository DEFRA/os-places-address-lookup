package uk.gov.ea.address.services.util;

import org.junit.BeforeClass;
import org.junit.Test;
import uk.gov.ea.address.services.core.Address;
import uk.gov.ea.address.services.exception.OSPlacesClientException;

import java.util.List;

import static org.junit.Assert.*;

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

        assertEquals(true, search.canSearch());
    }

    @Test(expected = Exception.class)
    public void testCanSearchException() throws OSPlacesClientException {

        AddressSearch search = new AddressSearch(invalidOsPlaces);

        search.canSearch();
    }

    @Test
    public void testGetAddresses() throws OSPlacesClientException {
        AddressSearch search = new AddressSearch(osPlaces);

        List<Address> addresses = search.getAddresses("SW59BH");

        assertNotNull(addresses);
        assertNotSame(0, addresses.size());
    }

    @Test(expected = Exception.class)
    public void testGetAddressesException() throws OSPlacesClientException {

        AddressSearch search = new AddressSearch(osPlaces);

        search.getAddresses(null);
    }

    @Test(expected = Exception.class)
    public void testGetAddressesInvalid() throws OSPlacesClientException {

        AddressSearch search = new AddressSearch(osPlaces);

        search.getAddresses("");
    }

    @Test
    public void testAddressByUPRN() throws OSPlacesClientException {

        AddressSearch search = new AddressSearch(osPlaces);

        Address address = search.getAddress("200010019924");

        assertNotNull(address);
        assertEquals("200010019924", address.uprn);
        assertNotNull(address.postcode);
        assertNotNull(address.town);
        assertNotNull(address.easting);
        assertNotNull(address.moniker);
        assertNotNull(address.northing);
        assertNotNull(address.partial);
        assertNotNull(address.lines);
        assertNotNull(address.organisationName);
        assertNotNull(address.buildingName);
        assertNotNull(address.buildingNumber);
        assertNotNull(address.thoroughfareName);
        assertNotNull(address.subBuildingName);
    }

    @Test
    public void testAddressDelimiter() throws OSPlacesClientException {

        AddressSearch search = new AddressSearch(osPlaces);
        Address address = search.getAddress("217026675");

        assertNotNull(address);
        assertEquals("217026675", address.uprn);
        assertNotNull(address.postcode);
        assertNotNull(address.town);
        assertNotNull(address.easting);
        assertNotNull(address.moniker);
        assertNotNull(address.northing);
        assertNotNull(address.partial);
        assertNotNull(address.lines);
        assertNotNull(address.organisationName);
        assertNotNull(address.buildingName);
        assertNotNull(address.buildingNumber);
        assertNotNull(address.thoroughfareName);
        assertNotNull(address.subBuildingName);
        assertEquals(address.thoroughfareName, address.lines.get(1));
        assertEquals("", address.organisationName);
        assertEquals("", address.buildingNumber);
    }

    @Test(expected = Exception.class)
    public void testAddressInvalid() throws OSPlacesClientException {

        AddressSearch search = new AddressSearch(osPlaces);
        search.getAddress(null);
    }
}
