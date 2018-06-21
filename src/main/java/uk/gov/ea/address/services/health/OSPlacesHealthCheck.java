package uk.gov.ea.address.services.health;

import com.codahale.metrics.health.HealthCheck;
import uk.gov.ea.address.services.util.IOSPlaces;

public class OSPlacesHealthCheck extends HealthCheck{
    
    protected final IOSPlaces osPlaces;

    public OSPlacesHealthCheck(IOSPlaces osPlaces) {
        this.osPlaces = osPlaces;
    }

    @Override
    protected Result check() throws Exception {
        String message = osPlaces.health();
        return message == null ? Result.healthy() : Result.unhealthy(message);
    }
	
}
