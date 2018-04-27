package uk.gov.ea.address.services.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.gov.ea.address.services.exception.OSPlacesClientException;
import uk.gov.ea.address.services.util.OSPlacesAddressSearch;
import uk.gov.ea.address.services.util.OSPlacesAddressUtils;

import com.codahale.metrics.annotation.Timed;

@Path("/addresses.json")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SearchResultsResource
{

    /** The logger */
    private static final Logger logger = LoggerFactory.getLogger(SearchResultsResource.class);

    protected final OSPlacesAddressSearch osAddressSearch;

    public SearchResultsResource(OSPlacesAddressSearch osAddressSearch)
    {
        this.osAddressSearch = osAddressSearch;
    }

    @GET
    @Timed
    public Response getAddresses(@QueryParam("houseNumber") String houseNumber, @QueryParam("postcode") String postcode)
        throws OSPlacesClientException
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("Get Method Detected at /addresses");
            logger.debug("houseNumber=" + houseNumber + ", postcode=" + postcode);
        }
        try
        {
            validatePostcode(postcode);
            return Response.ok(osAddressSearch.getAddresses(houseNumber, postcode)).build();
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage());
            return OSPlacesAddressUtils.getResponseException(ex);
        }
    }

    private Response validatePostcode(String postcode)
    {
        JSONObject jObject = new JSONObject();
        JSONObject jObj = new JSONObject();
        jObj.put("statuscode", Response.Status.BAD_REQUEST.getStatusCode());

        if (postcode == null)
        {
            jObj.put("message", "No postcode parameter provided.");
        }
        else if (postcode != null && StringUtils.isEmpty(postcode))
        {
            jObj.put("message", "Parameter postcode cannot be empty.");
        }

        jObject.put("error", jObj);
        return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(jObject).build();
    }
}
