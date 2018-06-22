package uk.gov.ea.address.services.resources;

import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.ea.address.services.util.AddressSearch;
import uk.gov.ea.address.services.util.AddressUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/addresses")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SearchResource {

    private static final Logger logger = LoggerFactory.getLogger(SearchResource.class);

    private final AddressSearch osAddressSearch;

    public SearchResource(AddressSearch osAddressSearch) {
        this.osAddressSearch = osAddressSearch;
    }

    @GET
    public Response addresses(
            @QueryParam("postcode") @NotEmpty String postcode,
            @QueryParam("houseNumber") String houseNumber
    ) {

        logger.debug("Get Method Detected at /addresses?postcode=" + postcode + "&houseNumber=" + houseNumber);

        try {
            return Response.ok(osAddressSearch.getAddresses(postcode)).build();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return AddressUtils.getResponseException(ex);
        }
    }

    @GET
    @Path("/{id}")
    public Response address(@PathParam("id") @NotEmpty String id) {

        logger.debug("Get Method Detected at /addresses/" + id);

        try {
            return Response.ok(osAddressSearch.getAddress(id)).build();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return AddressUtils.getResponseException(ex);
        }
    }
}
