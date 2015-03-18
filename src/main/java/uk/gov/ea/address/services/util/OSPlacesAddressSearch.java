package uk.gov.ea.address.services.util;

import java.util.List;

import uk.gov.ea.address.services.core.Address;
import uk.gov.ea.address.services.exception.OSPlacesClientException;

public interface OSPlacesAddressSearch {
	
	String checkHealth() throws OSPlacesClientException;

	boolean canSearch() throws OSPlacesClientException;

	List<Address> getAddresses(String houseNumber, String postcode) throws OSPlacesClientException;

	Address getAddress(String moniker) throws OSPlacesClientException;
}
