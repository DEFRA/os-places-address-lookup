package uk.gov.ea.address.services.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.ea.address.services.core.Address;
import uk.gov.ea.address.services.exception.OSPlacesClientException;

import java.util.ArrayList;
import java.util.List;

public class AddressSearch {

    private IOSPlaces osPlaces;
    
    private static Logger logger = LoggerFactory.getLogger(AddressSearch.class);

    public AddressSearch(IOSPlaces osPlaces) {
        this.osPlaces = osPlaces;
    }

    public boolean canSearch() throws OSPlacesClientException {

        logger.info("can search method");

        if (this.osPlaces.health() == null) return true;

        return false;
    }

    public List<Address> getAddresses(String postcode) throws OSPlacesClientException {

        List<Address> addresses = new ArrayList<>();

        String result = osPlaces.addresses(postcode);

        JSONArray jArray = new JSONObject(result).optJSONArray("results");

        if (null != jArray && jArray.length() > 0) {

            // Add each returned address to our list of results.
            for (int i = 0; i < jArray.length(); i++) {

                // It seems that OS Places sometimes includes 'duplicates' (entries
                // which have the same UPRN but are not binary identical). We
                // choose to filter them out.
                Address thisAddress = AddressUtils.getAddressByJson(jArray.optJSONObject(i).optJSONObject("DPA"));
                boolean duplicateFound = false;
                for (int j = 0; j < addresses.size() && !duplicateFound; j++) {
                    if (thisAddress.uprn.equals(addresses.get(j).uprn))
                    {
                        duplicateFound = true;
                    }
                }
                if (!duplicateFound) {
                    addresses.add(thisAddress);
                }
            }

            // The data from OS Places doesn't appear to be sorted by house number (for example),
            // so lets make an attempt to sort the results in a useful order.
            java.util.Collections.sort(addresses);
        } else {
            throw new OSPlacesClientException(AddressUtils.getExceptionResponse().toString());
        }

        return addresses;
    }

    public Address getAddress(String uprn) throws OSPlacesClientException {

        logger.info("get address by postcode method");
        logger.info("uprn - {}", uprn);

        String result = osPlaces.address(uprn);

        JSONArray jArray = new JSONObject(result).optJSONArray("results");

        if (jArray == null) {
            throw new OSPlacesClientException(AddressUtils.getExceptionResponse().toString());
        }

        return AddressUtils.getAddressByJson(jArray.optJSONObject(0).optJSONObject("DPA"));
    }
}
