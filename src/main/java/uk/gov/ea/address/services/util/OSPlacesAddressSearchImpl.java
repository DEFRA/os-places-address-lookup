package uk.gov.ea.address.services.util;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.gov.ea.address.services.core.Address;
import uk.gov.ea.address.services.exception.OSPlacesClientException;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class OSPlacesAddressSearchImpl implements OSPlacesAddressSearch
{
    private Client client;

    private WebResource resource;

    private String key;

    private String delimiter;

    private static final String ORDNANCE_SURVEY = "ordnance survey";

    private static Logger logger = LoggerFactory.getLogger(OSPlacesAddressSearchImpl.class);

    public OSPlacesAddressSearchImpl(String url, String key, String delimiter)
    {
        this.key = key;
        this.delimiter = StringUtils.isBlank(delimiter) ? "," : delimiter;
        client = Client.create(new DefaultClientConfig());
        resource = client.resource(url);
    }

    @Override
    public String checkHealth() throws OSPlacesClientException
    {
        logger.info("Heath check method");

        try
        {
            MultivaluedMap<String, String> queryParams = OSPlacesAddressUtils.getMultivaluedMap(key);
            queryParams.add("query", ORDNANCE_SURVEY);

            ClientResponse response = resource.path("find").queryParams(queryParams).get(ClientResponse.class);

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

            MultivaluedMap<String, String> queryParams = OSPlacesAddressUtils.getMultivaluedMap(key);

            if (null != postcode)
            {
                queryParams.add("postcode", postcode);
            }

            ClientResponse response = resource.path("postcode").queryParams(queryParams).get(ClientResponse.class);

            OSPlacesAddressUtils.validateResponse(response);

            logger.info("response - {}", response);

            JSONArray jArray = new JSONObject(response.getEntity(String.class)).optJSONArray("results");

            if (null != jArray && jArray.length() > 0)
            {
                for (int i = 0; i < jArray.length(); i++)
                {
                    addresses.add(OSPlacesAddressUtils.getAddressByJson(jArray.optJSONObject(i).optJSONObject("DPA"), delimiter));
                }
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
            MultivaluedMap<String, String> queryParams = OSPlacesAddressUtils.getMultivaluedMap(key);

            if (null != moniker)
            {
                queryParams.add("uprn", moniker);
            }

            ClientResponse response = resource.path("uprn").queryParams(queryParams).get(ClientResponse.class);

            OSPlacesAddressUtils.validateResponse(response);

            logger.info("response - {}", response);

            JSONArray jArray = new JSONObject(response.getEntity(String.class)).optJSONArray("results");

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
