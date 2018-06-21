package uk.gov.ea.address.services.util;

import uk.gov.ea.address.services.exception.OSPlacesClientException;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

public class OSPlaces implements IOSPlaces {

    private WebTarget resource;

    private String key;

    private static final String ORDNANCE_SURVEY = "ordnance survey";

    public OSPlaces(String url, String key) {
        this.key = key;

        resource = ClientBuilder.newClient().target(url);
    }

    public String addresses(String postcode) throws OSPlacesClientException {

        Map<String, String> queryStrings = key();
        queryStrings.put("postcode", postcode);

        WebTarget target = AddressUtils.getParams(resource.path("postcode"), queryStrings);

        return call(target);
    }

    public String address(String uprn) throws OSPlacesClientException {

        Map<String, String> queryStrings = key();
        queryStrings.put("uprn", uprn);

        WebTarget target = AddressUtils.getParams(resource.path("uprn"), queryStrings);

        return call(target);
    }

    public String health() throws OSPlacesClientException {

        Map<String, String> queryStrings = key();
        queryStrings.put("query", ORDNANCE_SURVEY);

        WebTarget target = AddressUtils.getParams(resource.path("find"), queryStrings);

        Invocation.Builder invocationBuilder =
                target.request(MediaType.APPLICATION_JSON_TYPE);

        Response response = invocationBuilder.get();

        AddressUtils.validateResponse(response);

        return response.getStatus() != 200 ? "" : null;
    }

    private Map<String, String> key() {

        Map<String, String> queryStrings = new HashMap<>();
        queryStrings.put("key", key);
        return queryStrings;
    }

    private String call(WebTarget target) throws OSPlacesClientException {

        Invocation.Builder invocationBuilder =
                target.request(MediaType.APPLICATION_JSON_TYPE);

        Response response = invocationBuilder.get();

        AddressUtils.validateResponse(response);

        return response.readEntity(String.class);
    }
}
