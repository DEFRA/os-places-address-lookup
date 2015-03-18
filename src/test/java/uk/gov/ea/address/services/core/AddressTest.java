package uk.gov.ea.address.services.core;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

public class AddressTest
{
    @Test
    public void testSettersGetters()
    {
        Address address = getAddress();

        Assert.assertNotNull(address.getCountry());
        Assert.assertNotNull(address.getAdministrativeArea());
        Assert.assertNotNull(address.getDependentLocality());
        Assert.assertNotNull(address.getDependentThroughfare());
        Assert.assertNotNull(address.getEasting());
        Assert.assertNotNull(address.getLines());
        Assert.assertNotNull(address.getLocalAuthorityUpdateDate());
        Assert.assertNotNull(address.getNorthing());
        Assert.assertNotNull(address.getPostcode());
        Assert.assertNotNull(address.getRoyalMailUpdateDate());
        Assert.assertNotNull(address.getTown());
        Assert.assertNotNull(address.getUprn());
        Assert.assertNotNull(address.getMoniker());
        Assert.assertNotNull(address.getPartial());
        Assert.assertNotNull(address.getBuildingName());
        Assert.assertNotNull(address.getBuildingNumber());
        Assert.assertNotNull(address.getSubBuildingName());
        Assert.assertNotNull(address.getThoroughfareName());
        Assert.assertNotNull(address.getOrganisationName());
    }

    @Test
    public void testToString()
    {
        Address address = getAddress();
        Assert.assertNotNull(address.toString());
        Assert.assertNotNull(address.getLines());
        Assert.assertNotSame(0, address.getLines());
    }

    @Test
    public void testToStringWithOutPartials()
    {
        Address address = getAddress();
        address.setPartial(null);
        Assert.assertNotNull(address.toString());
    }

    private Address getAddress()
    {
        Address address = new Address();

        address.setCountry("APR");
        address.setAdministrativeArea("administrativeArea");
        address.setDependentLocality("dependentLocality");
        address.setDependentThroughfare("dependentThroughfare");
        address.setEasting("easting");
        List<String> lines = new ArrayList<String>();
        lines.add("building_name");
        lines.add("building_number");
        lines.add("throughfare_name");
        address.setLines(lines);
        address.setLocalAuthorityUpdateDate("localAuthorityUpdateDate");
        address.setNorthing("northing");
        address.setPostcode("postcode");
        address.setRoyalMailUpdateDate("royalMailUpdateDate");
        address.setTown("town");
        address.setUprn("uprn");
        address.setMoniker("moniker");
        address.setPartial("partial");
        address.setOrganisationName("organisationName");
        address.setThoroughfareName("thoroughfareName");
        address.setBuildingName("buildingName");
        address.setBuildingNumber("buildingNumber");
        address.setSubBuildingName("subBuildingName");

        return address;
    }
}
