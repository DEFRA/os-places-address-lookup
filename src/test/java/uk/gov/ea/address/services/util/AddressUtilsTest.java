package uk.gov.ea.address.services.util;

import javax.ws.rs.core.Response;

import junit.framework.Assert;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.gov.ea.address.services.core.Address;
import uk.gov.ea.address.services.exception.OSPlacesClientException;


public class AddressUtilsTest
{
    @Mock
    private Exception exception;

    @Mock
    private Response clientResponse;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetResponse()
    {
        Mockito.when(exception.getMessage()).thenReturn(getExceptionResponse().toString());
        Response response = AddressUtils.getResponseException(exception);
        Assert.assertEquals(400, response.getStatus());
        Assert.assertNotNull(response.getEntity());
    }

    @Test
    public void testGetResponseInvalid()
    {
        Mockito.when(exception.getMessage()).thenReturn(getExceptionResponse().toString());
        Assert.assertNotNull(AddressUtils.getResponseException(exception));
    }

    @Test
    public void testGetResponseNoErrorKey()
    {
        Mockito.when(exception.getMessage()).thenReturn(new JSONObject().toString());
        Assert.assertNull(AddressUtils.getResponseException(exception));
    }

    @Test
    public void testGetResponseNoStatusKey()
    {
        Mockito.when(exception.getMessage()).thenReturn(getEmptyStatusResponse().toString());
        Assert.assertNull(AddressUtils.getResponseException(exception));
    }

    @Test
    public void testGetResponseWithNull()
    {
        Mockito.when(exception.getMessage()).thenReturn(null);
        Assert.assertNull(AddressUtils.getResponseException(exception));
    }

    @Test
    public void testGetNullResponse()
    {
        Assert.assertNull(AddressUtils.getResponseException(null));
    }

    @Test
    public void testGetResponseWithEmpty()
    {
        Mockito.when(exception.getMessage()).thenReturn("");
        Response response = AddressUtils.getResponseException(exception);
        Assert.assertNull(response);
    }

    @Test
    public void testValidateResponse() throws OSPlacesClientException
    {
        Mockito.when(clientResponse.getStatus()).thenReturn(200);
        AddressUtils.validateResponse(clientResponse);
        Assert.assertEquals(200, clientResponse.getStatus());
        Assert.assertNotNull(clientResponse);
    }

    @Test
    public void testValidateResponseInvalid() throws OSPlacesClientException
    {
        Mockito.when(clientResponse.getStatus()).thenReturn(400);
        Mockito.when(clientResponse.readEntity(String.class)).thenReturn(getExceptionResponse().toString());
        try
        {
            AddressUtils.validateResponse(clientResponse);
        }
        catch (OSPlacesClientException e)
        {
            Assert.assertNotNull(e.toString());
        }
    }

    @Test
    public void testAddressByJSON()
    {
        JSONObject jObject = testJson();

        Address address = AddressUtils.getAddressByJson(jObject);

        Assert.assertNotNull(address.getUprn());
        Assert.assertNotNull(address.getPostcode());
        Assert.assertNotNull(address.getTown());

        Assert.assertNotNull(address.getCountry());
        Assert.assertEquals(0, address.getCountry().length());

        Assert.assertNotNull(address.getEasting());
        Assert.assertNotNull(address.getNorthing());
        Assert.assertNotNull(address.getMoniker());
        Assert.assertNotNull(address.getPartial());
        Assert.assertNotNull(address.getSubBuildingName());
        Assert.assertNotNull(address.getBuildingName());
        Assert.assertNotNull(address.getThoroughfareName());
        Assert.assertNotNull(address.getOrganisationName());
        Assert.assertNotNull(address.getBuildingNumber());
        Assert.assertNotNull(address.getDependentLocality());
        Assert.assertNotNull(address.getDependentThroughfare());
        Assert.assertNotNull(address.getAdministrativeArea());
        Assert.assertNotNull(address.getLocalAuthorityUpdateDate());
        Assert.assertNotNull(address.getRoyalMailUpdateDate());

        Assert.assertEquals(0, address.getLocalAuthorityUpdateDate().length());
        Assert.assertEquals(0, address.getRoyalMailUpdateDate().length());

        Assert.assertNotNull(address.getPostOfficeBoxNumber());
        Assert.assertNotNull(address.getDepartmentName());
        Assert.assertNotNull(address.getDoubleDependentLocality());
        Assert.assertNotNull(address.getLines());
    }

    @Test
    public void testAddressWithOrganization()
    {
        JSONObject jObject = testJson();

        Mockito.when(jObject.has("DEPARTMENT_NAME")).thenReturn(true);
        Mockito.when(jObject.has("ORGANISATION_NAME")).thenReturn(true);
        Mockito.when(jObject.has("THOROUGHFARE_NAME")).thenReturn(true);

        Mockito.when(jObject.getString("DEPARTMENT_NAME")).thenReturn("DEPARTMENT_NAME");
        Mockito.when(jObject.getString("ORGANISATION_NAME")).thenReturn("ORGANISATION_NAME");
        Mockito.when(jObject.getString("THOROUGHFARE_NAME")).thenReturn("THOROUGHFARE_NAME");

        Address address = AddressUtils.getAddressByJson(jObject);

        Assert.assertNotNull(address.getThoroughfareName());
        Assert.assertNotNull(address.getOrganisationName());
        Assert.assertNotNull(address.getDepartmentName());
        Assert.assertNotNull(address.getLines());

        Assert.assertEquals(true, address.getLines().contains("DEPARTMENT_NAME"));
        Assert.assertEquals(true, address.getLines().contains("ORGANISATION_NAME"));
        Assert.assertEquals(true, address.getLines().contains("THOROUGHFARE_NAME"));

    }

    @Test
    public void testAddressWithNoOrganization()
    {
        JSONObject jObject = testJson();

        Mockito.when(jObject.has("DEPARTMENT_NAME")).thenReturn(false);
        Mockito.when(jObject.has("ORGANISATION_NAME")).thenReturn(false);
        Mockito.when(jObject.has("THOROUGHFARE_NAME")).thenReturn(false);

        Mockito.when(jObject.has("SUB_BUILDING_NAME")).thenReturn(true);
        Mockito.when(jObject.has("BUILDING_NUMBER")).thenReturn(true);
        Mockito.when(jObject.has("PO_BOX_NUMBER")).thenReturn(true);
        Mockito.when(jObject.has("BUILDING_NAME")).thenReturn(true);

        Mockito.when(jObject.getString("SUB_BUILDING_NAME")).thenReturn("SUB_BUILDING_NAME");
        Mockito.when(jObject.getString("BUILDING_NUMBER")).thenReturn("BUILDING_NUMBER");
        Mockito.when(jObject.getString("PO_BOX_NUMBER")).thenReturn("PO_BOX_NUMBER");
        Mockito.when(jObject.getString("BUILDING_NAME")).thenReturn("BUILDING_NAME");

        Address address = AddressUtils.getAddressByJson(jObject);

        Assert.assertNotNull(address.getLines());

        Assert.assertEquals("[SUB_BUILDING_NAME,BUILDING_NUMBER,PO_BOX_NUMBER,BUILDING_NAME]", address.getLines().toString());
    }

    @Test
    public void testAddressWithNoSubBuilding()
    {
        JSONObject jObject = testJson();

        Mockito.when(jObject.has("DEPARTMENT_NAME")).thenReturn(false);
        Mockito.when(jObject.has("ORGANISATION_NAME")).thenReturn(false);
        Mockito.when(jObject.has("THOROUGHFARE_NAME")).thenReturn(false);

        Mockito.when(jObject.has("SUB_BUILDING_NAME")).thenReturn(false);
        Mockito.when(jObject.has("BUILDING_NUMBER")).thenReturn(true);
        Mockito.when(jObject.has("PO_BOX_NUMBER")).thenReturn(true);
        Mockito.when(jObject.has("BUILDING_NAME")).thenReturn(true);

        Mockito.when(jObject.getString("BUILDING_NUMBER")).thenReturn("BUILDING_NUMBER");
        Mockito.when(jObject.getString("PO_BOX_NUMBER")).thenReturn("PO_BOX_NUMBER");
        Mockito.when(jObject.getString("BUILDING_NAME")).thenReturn("BUILDING_NAME");

        Address address = AddressUtils.getAddressByJson(jObject);

        Assert.assertNotNull(address.getLines());

        Assert.assertEquals("[BUILDING_NUMBER,PO_BOX_NUMBER,BUILDING_NAME]", address.getLines().toString());
    }

    @Test
    public void testGetExceptionResp()
    {
        JSONObject jObject = AddressUtils.getExceptionResponse();

        Assert.assertNotNull(jObject);
        Assert.assertEquals(true, jObject.has("error"));
        Assert.assertNotNull(jObject.getJSONObject("error"));

        JSONObject jObj = jObject.getJSONObject("error");

        Assert.assertEquals("Parameters are not valid", jObj.get("message"));
        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), jObj.get("statuscode"));
    }

    private JSONObject getExceptionResponse()
    {
        JSONObject jObject = new JSONObject();
        JSONObject jObj = new JSONObject();
        jObj.put("statuscode", Response.Status.BAD_REQUEST.getStatusCode());
        jObj.put("message", "Parameters cannot be empty.");
        jObject.put("error", jObj);

        return jObject;
    }

    private JSONObject getEmptyStatusResponse()
    {
        JSONObject jObject = new JSONObject();
        JSONObject jObj = new JSONObject();
        jObj.put("message", "Parameters cannot be empty.");
        jObject.put("error", jObj);
        return jObject;
    }

    private JSONObject testJson()
    {
        JSONObject jObject = Mockito.mock(JSONObject.class);

        Mockito.when(jObject.optString("UPRN")).thenReturn(Matchers.anyString());
        Mockito.when(jObject.optString("POSTCODE")).thenReturn(Matchers.anyString());
        Mockito.when(jObject.optString("POST_TOWN")).thenReturn(Matchers.anyString());
        Mockito.when(jObject.optLong("X_COORDINATE")).thenReturn(Matchers.anyLong());
        Mockito.when(jObject.optLong("Y_COORDINATE")).thenReturn(Matchers.anyLong());
        Mockito.when(jObject.optString("ADDRESS")).thenReturn(Matchers.anyString());
        Mockito.when(jObject.optString("SUB_BUILDING_NAME")).thenReturn(Matchers.anyString());
        Mockito.when(jObject.optString("BUILDING_NAME")).thenReturn(Matchers.anyString());

        Mockito.when(jObject.optString("THOROUGHFARE_NAME")).thenReturn(Matchers.anyString());
        Mockito.when(jObject.optString("ORGANISATION_NAME")).thenReturn(Matchers.anyString());
        Mockito.when(jObject.optString("BUILDING_NUMBER")).thenReturn(Matchers.anyString());
        Mockito.when(jObject.optString("DEPENDENT_LOCALITY")).thenReturn(Matchers.anyString());
        Mockito.when(jObject.optString("DEPENDENT_THOROUGHFARE_NAME")).thenReturn(Matchers.anyString());
        Mockito.when(jObject.optString("POST_TOWN")).thenReturn(Matchers.anyString());
        Mockito.when(jObject.optString("PO_BOX_NUMBER")).thenReturn(Matchers.anyString());
        Mockito.when(jObject.optString("DEPARTMENT_NAME")).thenReturn(Matchers.anyString());
        Mockito.when(jObject.optString("DOUBLE_DEPENDENT_LOCALITY")).thenReturn(Matchers.anyString());

        return jObject;
    }
}
