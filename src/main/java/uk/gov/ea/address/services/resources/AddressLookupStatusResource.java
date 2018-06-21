package uk.gov.ea.address.services.resources;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.ea.address.services.core.AddressLookupStatus;
import uk.gov.ea.address.services.exception.OSPlacesClientException;
import uk.gov.ea.address.services.util.AddressSearch;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/status.json")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AddressLookupStatusResource {

    /** The logger */
    private static final Logger logger = LoggerFactory.getLogger(AddressLookupStatusResource.class.getName());

    protected final AddressSearch osAddressSearch;

    public AddressLookupStatusResource(AddressSearch osAddressSearch) {
        this.osAddressSearch = osAddressSearch;
    }

    @GET
    @Timed
    public AddressLookupStatus getAddress(@PathParam("id") String id) throws OSPlacesClientException {
        logger.debug("Get Method Detected at /status.json");
        AddressLookupStatus status = new AddressLookupStatus();
        status.setAvailable(osAddressSearch.canSearch());
        return status;
    }
}
