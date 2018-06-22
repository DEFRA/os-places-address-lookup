package uk.gov.ea.address.services.resources;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import uk.gov.ea.address.services.core.Address;
import uk.gov.ea.address.services.exception.OSPlacesClientException;
import uk.gov.ea.address.services.util.AddressSearch;
import uk.gov.ea.address.services.util.AddressUtils;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SearchResourceTest {

    @Mock
    private AddressSearch search;

    private SearchResource resource;

    @Mock
    private Address address;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        resource = new SearchResource(search);
    }

    @Test
    public void addresses() throws OSPlacesClientException {

        address.uprn = "20001";
        address.postcode = "SW59BH";

        List<Address> addresses = new ArrayList<Address>();
        addresses.add(address);

        Mockito.when(search.getAddresses("SW59BH")).thenReturn(addresses);

        List<?> exAddresses = (List<?>) resource.addresses("SW59BH", "").getEntity();
        assertNotNull(exAddresses);
        assertEquals(1, exAddresses.size());

        Address address = ((Address)exAddresses.get(0));

        assertEquals("20001", address.uprn);
        assertEquals("SW59BH", address.postcode);
    }

    @Test
    public void addressesReturnsNoResults() throws OSPlacesClientException {

        Mockito.when(search.getAddresses("BS15HA")).thenReturn(new ArrayList<>());
        List<?> exAddresses = (List<?>) resource.addresses("BS15HA", "").getEntity();

        assertTrue(exAddresses.isEmpty());
    }

    @Test
    public void address() throws OSPlacesClientException {
        address.uprn = "20001";

        Mockito.when(search.getAddress("20001")).thenReturn(address);

        Address expectedAddress = (Address) resource.address("20001").getEntity();

        assertNotNull(expectedAddress);
        assertEquals("20001", expectedAddress.uprn);
    }

    @Test
    public void addressWithEmptyUprn() throws OSPlacesClientException {
        Mockito.when(search.getAddress("")).thenThrow(new OSPlacesClientException(AddressUtils.getExceptionResponse().toString()));
        Response response = resource.address("");

        assertEquals(400, response.getStatus());
    }
}
