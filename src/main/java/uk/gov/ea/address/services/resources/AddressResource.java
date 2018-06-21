package uk.gov.ea.address.services.resources;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.ea.address.services.util.AddressSearch;
import uk.gov.ea.address.services.util.AddressUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/addresses/{id}.json")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AddressResource {

    /** The logger */
    private static final Logger logger = LoggerFactory.getLogger(AddressResource.class.getName());

    protected final AddressSearch osAddressSearch;

    public AddressResource(AddressSearch osAddressSearch) {
        this.osAddressSearch = osAddressSearch;
    }

    @GET
    @Timed
    public Response getAddress(@PathParam("id") String id) {

        logger.debug("Get Method Detected at /addresses/{id}.json");

        try {
            return Response.ok(osAddressSearch.getAddress(id)).build();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return AddressUtils.getResponseException(ex);
        }
    }
}
