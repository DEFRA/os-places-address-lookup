package uk.gov.ea.address.services.resources;

import com.codahale.metrics.annotation.Timed;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.ea.address.services.util.AddressSearch;
import uk.gov.ea.address.services.util.AddressUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/addresses.json")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SearchResultsResource {

    private static final Logger logger = LoggerFactory.getLogger(SearchResultsResource.class);

    protected final AddressSearch osAddressSearch;

    public SearchResultsResource(AddressSearch osAddressSearch) {
        this.osAddressSearch = osAddressSearch;
    }

    @GET
    @Timed
    public Response getAddresses(@QueryParam("houseNumber") String houseNumber, @QueryParam("postcode") String postcode) {

        logger.debug("Get Method Detected at /addresses");
        logger.debug("houseNumber=" + houseNumber + ", postcode=" + postcode);

        try {
            validatePostcode(postcode);
            return Response.ok(osAddressSearch.getAddresses(houseNumber, postcode)).build();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return AddressUtils.getResponseException(ex);
        }
    }

    private Response validatePostcode(String postcode) {

        JSONObject jObject = new JSONObject();
        JSONObject jObj = new JSONObject();
        jObj.put("statuscode", Response.Status.BAD_REQUEST.getStatusCode());

        if (postcode == null) {
            jObj.put("message", "No postcode parameter provided.");
        }
        else if (postcode != null && StringUtils.isEmpty(postcode)) {
            jObj.put("message", "Parameter postcode cannot be empty.");
        }

        jObject.put("error", jObj);

        return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(jObject).build();
    }
}
