package uk.gov.ea.address.services.util;

import uk.gov.ea.address.services.exception.OSPlacesClientException;

public interface IOSPlaces {
    String addresses(String postcode) throws OSPlacesClientException;
    String address(String uprn) throws OSPlacesClientException;
    String health() throws OSPlacesClientException;
}
