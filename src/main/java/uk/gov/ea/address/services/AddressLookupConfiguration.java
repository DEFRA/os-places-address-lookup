package uk.gov.ea.address.services;

import io.dropwizard.Configuration;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AddressLookupConfiguration extends Configuration
{

    @Valid
    @JsonProperty
    private Map<String, String> properties = new HashMap<String, String>();

    public Map<String, String> getProperties()
    {
        return properties;
    }

    public void setProperties(Map<String, String> properties)
    {
        this.properties = properties;
    }
}
