package uk.gov.ea.address.services.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.client.ClientResponse;
import org.json.JSONObject;

import uk.gov.ea.address.services.core.Address;
import uk.gov.ea.address.services.exception.OSPlacesClientException;

public class AddressUtils
{
    public static Response getResponseException(Exception ex)
    {
        if (null != ex && !StringUtils.isBlank(ex.getMessage()))
        {
            JSONObject jObject = new JSONObject(ex.getMessage());

            if (jObject.has("error") && jObject.getJSONObject("error").has("statuscode"))
            {
                return Response.status(Integer.valueOf(jObject.getJSONObject("error").get("statuscode").toString()))
                    .entity(jObject.toString()).build();
            }
        }
        return null;
    }

    public static void validateResponse(Response response) throws OSPlacesClientException
    {
        if (response.getStatus() != 200)
        {
            throw new OSPlacesClientException(response.readEntity(String.class));
        }
    }

    public static WebTarget getParams(WebTarget target, Map<String, String> queryStrings)
    {
        for (String key: queryStrings.keySet()){
            String value = queryStrings.get(key);
            target = target.queryParam(key, value);  //It is important to know queryParam method won't update current WebTarget object, but return a new one.
        }
        return target;
    }

    public static JSONObject getExceptionResponse()
    {
        JSONObject jObject = new JSONObject();
        JSONObject jObj = new JSONObject();
        jObj.put("message", "Parameters are not valid");
        jObj.put("statuscode", Response.Status.BAD_REQUEST.getStatusCode());
        jObject.put("error", jObj);
        return jObject;
    }

    public static Address getAddressByJson(JSONObject json)
    {
        List<String> lines = new ArrayList<String>();
        Address address = new Address();

        address.setUprn(json.optString("UPRN"));
        address.setPostcode(json.optString("POSTCODE"));
        address.setTown(json.optString("POST_TOWN"));
        address.setCountry("");
        address.setEasting(String.valueOf(json.optLong("X_COORDINATE")));
        address.setNorthing(String.valueOf(json.optLong("Y_COORDINATE")));
        address.setMoniker(address.getUprn());
        address.setPartial(json.optString("ADDRESS"));
        address.setSubBuildingName(json.optString("SUB_BUILDING_NAME"));
        address.setBuildingName(json.optString("BUILDING_NAME"));
        address.setThoroughfareName(json.optString("THOROUGHFARE_NAME"));
        address.setOrganisationName(json.optString("ORGANISATION_NAME"));
        address.setBuildingNumber(json.optString("BUILDING_NUMBER"));
        address.setDependentLocality(json.optString("DEPENDENT_LOCALITY"));
        address.setDependentThroughfare(json.optString("DEPENDENT_THOROUGHFARE_NAME"));
        address.setAdministrativeArea(json.optString("POST_TOWN"));
        address.setLocalAuthorityUpdateDate("");
        address.setRoyalMailUpdateDate("");
        address.setPostOfficeBoxNumber(json.optString("PO_BOX_NUMBER"));
        address.setDepartmentName(json.optString("DEPARTMENT_NAME"));
        address.setDoubleDependentLocality(json.optString("DOUBLE_DEPENDENT_LOCALITY"));

        if (json.has("DEPARTMENT_NAME"))
        {
            lines.add(json.getString("DEPARTMENT_NAME"));
        }

        if (json.has("ORGANISATION_NAME"))
        {
            lines.add(json.getString("ORGANISATION_NAME"));
        }
        else
        {
            StringBuilder lStr = new StringBuilder();

            if (json.has("SUB_BUILDING_NAME"))
            {
                lStr.append(json.getString("SUB_BUILDING_NAME"));
            }

            if (json.has("BUILDING_NUMBER"))
            {
                lStr.append("," + json.getString("BUILDING_NUMBER"));
            }

            if (json.has("PO_BOX_NUMBER"))
            {
                lStr.append("," + json.getString("PO_BOX_NUMBER"));
            }

            if (json.has("BUILDING_NAME"))
            {
                lStr.append("," + json.getString("BUILDING_NAME"));
            }

            lines.add(lStr.indexOf(",") == 0 ? lStr.substring(1, lStr.length()) : lStr.toString());

        }

        if (json.has("THOROUGHFARE_NAME"))
        {
            lines.add(json.getString("THOROUGHFARE_NAME"));
        }

        address.setLines(lines);
        
        // Calculate a value that will help us if we need to sort this address.
        address.calculateSortingValue();

        return address;
    }
}
