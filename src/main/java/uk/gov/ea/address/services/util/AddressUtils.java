package uk.gov.ea.address.services.util;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import uk.gov.ea.address.services.core.Address;
import uk.gov.ea.address.services.exception.OSPlacesClientException;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddressUtils {

    public static Response getResponseException(Exception ex) {

        if (null != ex && !StringUtils.isBlank(ex.getMessage())) {
            JSONObject jObject = new JSONObject(ex.getMessage());

            if (jObject.has("error") && jObject.getJSONObject("error").has("statuscode")) {
                return Response.status(Integer.valueOf(jObject.getJSONObject("error").get("statuscode").toString()))
                    .entity(jObject.toString()).build();
            }
        }
        return null;
    }

    public static void validateResponse(Response response) throws OSPlacesClientException {

        if (response.getStatus() != 200) {
            throw new OSPlacesClientException(response.readEntity(String.class));
        }
    }

    public static WebTarget getParams(WebTarget target, Map<String, String> queryStrings) {

        for (String key: queryStrings.keySet()){
            String value = queryStrings.get(key);
            target = target.queryParam(key, value);  //It is important to know queryParam method won't update current WebTarget object, but return a new one.
        }
        return target;
    }

    public static JSONObject getExceptionResponse() {

        JSONObject jObject = new JSONObject();
        JSONObject jObj = new JSONObject();
        jObj.put("message", "Parameters are not valid");
        jObj.put("statuscode", Response.Status.BAD_REQUEST.getStatusCode());
        jObject.put("error", jObj);

        return jObject;
    }

    public static Address getAddressByJson(JSONObject json) {

        List<String> lines = new ArrayList<>();
        Address address = new Address();

        address.uprn = json.optString("UPRN");
        address.postcode = json.optString("POSTCODE");
        address.town = json.optString("POST_TOWN");
        address.country = "";
        address.easting = String.valueOf(json.optLong("X_COORDINATE"));
        address.northing = String.valueOf(json.optLong("Y_COORDINATE"));
        address.moniker = address.uprn;
        address.partial = json.optString("ADDRESS");
        address.subBuildingName = json.optString("SUB_BUILDING_NAME");
        address.buildingName = json.optString("BUILDING_NAME");
        address.thoroughfareName = json.optString("THOROUGHFARE_NAME");
        address.organisationName = json.optString("ORGANISATION_NAME");
        address.buildingNumber = json.optString("BUILDING_NUMBER");
        address.dependentLocality = json.optString("DEPENDENT_LOCALITY");
        address.dependentThroughfare = json.optString("DEPENDENT_THOROUGHFARE_NAME");
        address.administrativeArea = json.optString("POST_TOWN");
        address.localAuthorityUpdateDate = "";
        address.royalMailUpdateDate = "";
        address.postOfficeBoxNumber = json.optString("PO_BOX_NUMBER");
        address.departmentName = json.optString("DEPARTMENT_NAME");
        address.doubleDependentLocality = json.optString("DOUBLE_DEPENDENT_LOCALITY");

        if (json.has("DEPARTMENT_NAME")) lines.add(json.getString("DEPARTMENT_NAME"));

        if (json.has("ORGANISATION_NAME")) {
            lines.add(json.getString("ORGANISATION_NAME"));
        } else {
            StringBuilder lStr = new StringBuilder();

            if (json.has("SUB_BUILDING_NAME")) lStr.append(json.getString("SUB_BUILDING_NAME"));


            if (json.has("BUILDING_NUMBER")) lStr.append("," + json.getString("BUILDING_NUMBER"));


            if (json.has("PO_BOX_NUMBER")) lStr.append("," + json.getString("PO_BOX_NUMBER"));


            if (json.has("BUILDING_NAME")) lStr.append("," + json.getString("BUILDING_NAME"));

            lines.add(lStr.indexOf(",") == 0 ? lStr.substring(1, lStr.length()) : lStr.toString());
        }

        if (json.has("THOROUGHFARE_NAME")) lines.add(json.getString("THOROUGHFARE_NAME"));

        address.lines = lines;
        
        // Calculate a value that will help us if we need to sort this address.
        address.calculateSortingValue();

        return address;
    }
}
