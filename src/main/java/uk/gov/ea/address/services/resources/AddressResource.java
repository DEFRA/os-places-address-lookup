package uk.gov.ea.address.services.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.gov.ea.address.services.exception.OSPlacesClientException;
import uk.gov.ea.address.services.util.OSPlacesAddressSearch;
import uk.gov.ea.address.services.util.OSPlacesAddressUtils;

import com.codahale.metrics.annotation.Timed;

@Path("/addresses/{id}.json")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AddressResource
{

    /** The logger */
    private static final Logger logger = LoggerFactory.getLogger(AddressResource.class.getName());

    protected final OSPlacesAddressSearch osAddressSearch;

    public AddressResource(OSPlacesAddressSearch osAddressSearch)
    {
        this.osAddressSearch = osAddressSearch;
    }

    @GET
    @Timed
    public Response getAddress(@PathParam("id") String id) throws OSPlacesClientException
    {
        logger.debug("Get Method Detected at /addresses/{id}.json");
        try
        {
            return Response.ok(osAddressSearch.getAddress(id)).build();
        }
        catch (Exception ex)
        {
            return OSPlacesAddressUtils.getResponseException(ex);
        }
    }

}
