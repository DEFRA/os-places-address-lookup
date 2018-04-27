package uk.gov.ea.address.services.util;

import java.util.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.client.ClientResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.gov.ea.address.services.core.Address;
import uk.gov.ea.address.services.exception.OSPlacesClientException;

public class OSPlacesAddressSearchImpl implements OSPlacesAddressSearch
{
    private Client client;

    private WebTarget resource;

    private String key;

    private String delimiter;

    private static final String ORDNANCE_SURVEY = "ordnance survey";
    
    private static Logger logger = LoggerFactory.getLogger(OSPlacesAddressSearchImpl.class);

    public OSPlacesAddressSearchImpl(String url, String key, String delimiter)
    {
        this.key = key;
        this.delimiter = StringUtils.isBlank(delimiter) ? "," : delimiter;
        client = ClientBuilder.newClient();
        resource = client.target(url);
    }

    @Override
    public String checkHealth() throws OSPlacesClientException
    {
        logger.info("Heath check method");

        try
        {
            WebTarget target = resource.path("find");
            Map<String, String> queryStrings = new HashMap<>();
            queryStrings.put("key", key);
            queryStrings.put("query", ORDNANCE_SURVEY);
            target = OSPlacesAddressUtils.getParams(target, queryStrings);

            Invocation.Builder invocationBuilder =
                    target.request(MediaType.APPLICATION_JSON_TYPE);
            Response response = invocationBuilder.get();

            OSPlacesAddressUtils.validateResponse(response);

            return response.getStatus() != 200 ? "" : null;
        }
        catch (Exception ex)
        {
            throw new OSPlacesClientException(ex.toString(), ex);
        }
    }

    @Override
    public boolean canSearch() throws OSPlacesClientException
    {
        logger.info("can search method");
        if (checkHealth() == null)
        {
            return true;
        }
        return false;
    }

    @Override
    public List<Address> getAddresses(String houseNumber, String postcode) throws OSPlacesClientException
    {
        logger.info("get addresses method");
        logger.info("houseNumber - {}", houseNumber);
        logger.info("postcode - {}", postcode);

        List<Address> addresses = new ArrayList<Address>();

        try
        {
            WebTarget target = resource.path("postcode");
            Map<String, String> queryStrings = new HashMap<>();
            queryStrings.put("key", key);

            if (null != postcode)
            {
                queryStrings.put("postcode", postcode);
            }

            target = OSPlacesAddressUtils.getParams(target, queryStrings);

            Invocation.Builder invocationBuilder =
                    target.request(MediaType.APPLICATION_JSON_TYPE);
            Response response = invocationBuilder.get();

            OSPlacesAddressUtils.validateResponse(response);

            logger.info("response - {}", response);

            JSONArray jArray = new JSONObject(response.readEntity(String.class)).optJSONArray("results");

            if (null != jArray && jArray.length() > 0)
            {
                // Add each returned address to our list of results.
                for (int i = 0; i < jArray.length(); i++)
                {
                    // It seems that OS Places sometimes includes 'duplicates' (entries
                    // which have the same UPRN but are not binrary identical).  We
                    // choose to filter them out.
                    Address thisAddress = OSPlacesAddressUtils.getAddressByJson(jArray.optJSONObject(i).optJSONObject("DPA"), delimiter);
                    boolean duplicateFound = false;
                    for (int j = 0; j < addresses.size() && !duplicateFound; j++)
                    {
                        if (thisAddress.getUprn().equals(addresses.get(j).getUprn()))
                        {
                            duplicateFound = true;
                        }
                    }
                    if (!duplicateFound)
                    {
                        addresses.add(thisAddress);
                    }
                }
                
                // The data from OS Places doesn't appear to be sorted by house number (for example),
                // so lets make an attempt to sort the results in a useful order.
                java.util.Collections.sort(addresses);
            }
            else
            {
                throw new OSPlacesClientException(OSPlacesAddressUtils.getExceptionResponse().toString());
            }
        }
        catch (Exception ex)
        {
            throw new OSPlacesClientException(ex.toString(), ex);
        }

        return addresses;
    }

    @Override
    public Address getAddress(String moniker) throws OSPlacesClientException
    {
        logger.info("get address by postcode method");
        logger.info("moniker - {}", moniker);

        try
        {
            WebTarget target = resource.path("uprn");
            Map<String, String> queryStrings = new HashMap<>();
            queryStrings.put("key", key);

            if (null != moniker)
            {
                queryStrings.put("uprn", moniker);
            }

            target = OSPlacesAddressUtils.getParams(target, queryStrings);

            Invocation.Builder invocationBuilder =
                    target.request(MediaType.APPLICATION_JSON_TYPE);
            Response response = invocationBuilder.get();

            OSPlacesAddressUtils.validateResponse(response);

            logger.info("response - {}", response);

            JSONArray jArray = new JSONObject(response.readEntity(String.class)).optJSONArray("results");

            if (jArray == null)
            {
                throw new OSPlacesClientException(OSPlacesAddressUtils.getExceptionResponse().toString());
            }

            return OSPlacesAddressUtils.getAddressByJson(jArray.optJSONObject(0).optJSONObject("DPA"), delimiter);
        }
        catch (Exception ex)
        {
            throw new OSPlacesClientException(ex.toString(), ex);
        }
    }
}
