package uk.gov.ea.address.services.core;

import java.util.ArrayList;
import java.util.Arrays;
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
    
    @Test
    public void testSortingByBuildingNumber()
    {
        // Where an address contains a parse-able house number, it should be
        // used to sort addresses *numerically*:
        //   * 5 should be before 6
        //   * 6 should be before 50.
        List<Address> addresses = Arrays.asList(
            createAddressForSortingTest("6",  "", "The House, The Road, The Town, AB1 2CD"),
            createAddressForSortingTest("50", "", "The House, The Road, The Town, AB1 2CD"),
            createAddressForSortingTest("5",  "", "The House, The Road, The Town, AB1 2CD")
        );
        
        for (Address address : addresses)
        {
            address.calculateSortingValue();
        }
        
        java.util.Collections.sort(addresses);
        
        Assert.assertEquals("5",  addresses.get(0).getBuildingNumber());
        Assert.assertEquals("6",  addresses.get(1).getBuildingNumber());
        Assert.assertEquals("50", addresses.get(2).getBuildingNumber());
    }
    
    @Test
    public void testSortingByBuildingName()
    {
        // Where an address does not contain a parse-able house number, we should
        // attempt to parse the building name to extract a number to sort by.
        // We should attempt to take account of flat numbers.
        List<Address> addresses = Arrays.asList(
            createAddressForSortingTest("", "15C",  "The House, The Road, The Town, AB1 2CD"),
            createAddressForSortingTest("", "150",  "The House, The Road, The Town, AB1 2CD"),
            createAddressForSortingTest("", "15 A", "The House, The Road, The Town, AB1 2CD"),
            createAddressForSortingTest("", "15",   "The House, The Road, The Town, AB1 2CD"),
            createAddressForSortingTest("", "15b",  "The House, The Road, The Town, AB1 2CD")
        );
        
        for (Address address : addresses)
        {
            address.calculateSortingValue();
        }
        
        java.util.Collections.sort(addresses);
        
        Assert.assertEquals("15",   addresses.get(0).getBuildingName());
        Assert.assertEquals("15 A", addresses.get(1).getBuildingName());
        Assert.assertEquals("15b",  addresses.get(2).getBuildingName());
        Assert.assertEquals("15C",  addresses.get(3).getBuildingName());
        Assert.assertEquals("150",  addresses.get(4).getBuildingName());
    }
    
    @Test
    public void testSortingByNumberAndNameMixed()
    {
        // Sorting should cope with a mix of house numbers, and numbers in house
        // names.
        List<Address> addresses = Arrays.asList(
            createAddressForSortingTest("7",  "",    "The House, The Road, The Town, AB1 2CD"),
            createAddressForSortingTest("50", "",    "The House, The Road, The Town, AB1 2CD"),
            createAddressForSortingTest("",   "5 C", "The House, The Road, The Town, AB1 2CD"),
            createAddressForSortingTest("",   "5A",  "The House, The Road, The Town, AB1 2CD"),
            createAddressForSortingTest("",   "50a", "The House, The Road, The Town, AB1 2CD"),
            createAddressForSortingTest("5",  "",    "The House, The Road, The Town, AB1 2CD")
        );
        
        for (Address address : addresses)
        {
            address.calculateSortingValue();
        }
        
        java.util.Collections.sort(addresses);
        
        Assert.assertEquals("5",   addresses.get(0).getBuildingNumber());
        Assert.assertEquals("5A",  addresses.get(1).getBuildingName());
        Assert.assertEquals("5 C", addresses.get(2).getBuildingName());
        Assert.assertEquals("7",   addresses.get(3).getBuildingNumber());
        Assert.assertEquals("50",  addresses.get(4).getBuildingNumber());
        Assert.assertEquals("50a", addresses.get(5).getBuildingName());
    }
    
    @Test
    public void testSortingAddressesStartingWithFlat()
    {
        // Addresses which don't contain a Building Number or Building Name may
        // start with 'Flat NNN'; in this case, sort numerically by Flat Number.
        List<Address> addresses = Arrays.asList(
            createAddressForSortingTest("", "", "Flat 11, The House, The Road, The Town, AB1 2CD"),
            createAddressForSortingTest("", "", "Flat 2, The House, The Road, The Town, AB1 2CD"),
            createAddressForSortingTest("", "", "Flat 10, The House, The Road, The Town, AB1 2CD"),
            createAddressForSortingTest("", "", "Flat 1 , The House, The Road, The Town, AB1 2CD"), // Notice space after flat number
            createAddressForSortingTest("", "", "Flat 3, The House, The Road, The Town, AB1 2CD")
        );

        for (Address address : addresses)
        {
            address.calculateSortingValue();
        }

        java.util.Collections.sort(addresses);

        Assert.assertEquals("Flat 1 , The House, The Road, The Town, AB1 2CD", addresses.get(0).getPartial());
        Assert.assertEquals("Flat 2, The House, The Road, The Town, AB1 2CD",  addresses.get(1).getPartial());
        Assert.assertEquals("Flat 3, The House, The Road, The Town, AB1 2CD",  addresses.get(2).getPartial());
        Assert.assertEquals("Flat 10, The House, The Road, The Town, AB1 2CD", addresses.get(3).getPartial());
        Assert.assertEquals("Flat 11, The House, The Road, The Town, AB1 2CD", addresses.get(4).getPartial());
    }
    
    @Test
    public void testSortingByAllCriteria()
    {
        // Addresses with no number should be sorted alphabetically and appear
        // before any addresses with a recognised number.  Addresses starting
        // with 'Flat ' should appear last.
        List<Address> addresses = Arrays.asList(
            createAddressForSortingTest("7", "",   "Alpha Business, The Road, The Town, AB1 2CD"),
            createAddressForSortingTest("",  "",   "Gamma Business, The Road, The Town, AB1 2CD"),
            createAddressForSortingTest("",  "",   "Flat 2, The House, The Road, The Town, AB1 2CD"),
            createAddressForSortingTest("",  "6a", "Beta Business, The Road, The Town, AB1 2CD"),
            createAddressForSortingTest("",  "",   "Delta Business, The Road, The Town, AB1 2CD")
        );
        
        for (Address address : addresses)
        {
            address.calculateSortingValue();
        }
        
        java.util.Collections.sort(addresses);
        
        Assert.assertEquals("Delta Business, The Road, The Town, AB1 2CD",    addresses.get(0).getPartial());
        Assert.assertEquals("Gamma Business, The Road, The Town, AB1 2CD",    addresses.get(1).getPartial());
        Assert.assertEquals("Beta Business, The Road, The Town, AB1 2CD",     addresses.get(2).getPartial());
        Assert.assertEquals("Alpha Business, The Road, The Town, AB1 2CD",    addresses.get(3).getPartial());
        Assert.assertEquals("Flat 2, The House, The Road, The Town, AB1 2CD", addresses.get(4).getPartial());
    }
    
    private Address createAddressForSortingTest(String buildingNumber, String buildingName, String partial)
    {
        Address address = new Address();
        address.setBuildingNumber(buildingNumber);
        address.setBuildingName(buildingName);
        address.setPartial(partial);
        return address;
    }
}
