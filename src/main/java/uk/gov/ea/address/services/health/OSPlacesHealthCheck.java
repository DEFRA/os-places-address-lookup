package uk.gov.ea.address.services.health;

import uk.gov.ea.address.services.util.OSPlacesAddressSearch;

import com.codahale.metrics.health.HealthCheck;

public class OSPlacesHealthCheck extends HealthCheck{
    
    protected final OSPlacesAddressSearch osAddressSearch;

    public OSPlacesHealthCheck(OSPlacesAddressSearch osAddressSearch) {
        this.osAddressSearch = osAddressSearch;
    }

    @Override
    protected Result check() throws Exception {
        String message = osAddressSearch.checkHealth();
        return message == null ? Result.healthy() : Result.unhealthy(message);
    }
	
}
