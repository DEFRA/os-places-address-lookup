package uk.gov.ea.address.services.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import static org.junit.Assert.*;

public class AddressTest {

    @Test
    public void testSettersGetters() {
        Address address = getAddress();

        assertNotNull(address.country);
        assertNotNull(address.administrativeArea);
        assertNotNull(address.dependentLocality);
        assertNotNull(address.dependentThroughfare);
        assertNotNull(address.easting);
        assertNotNull(address.lines);
        assertNotNull(address.localAuthorityUpdateDate);
        assertNotNull(address.northing);
        assertNotNull(address.postcode);
        assertNotNull(address.royalMailUpdateDate);
        assertNotNull(address.town);
        assertNotNull(address.uprn);
        assertNotNull(address.moniker);
        assertNotNull(address.partial);
        assertNotNull(address.buildingName);
        assertNotNull(address.buildingNumber);
        assertNotNull(address.subBuildingName);
        assertNotNull(address.thoroughfareName);
        assertNotNull(address.organisationName);
    }

    @Test
    public void testToString() {
        Address address = getAddress();

        assertNotNull(address.toString());
        assertNotNull(address.lines);
        assertNotSame(0, address.lines);
    }

    @Test
    public void testToStringWithOutPartials() {
        Address address = getAddress();
        address.partial = null;

        assertNotNull(address.toString());
    }
    
    private Address getAddress() {
        Address address = new Address();

        address.country = "APR";
        address.administrativeArea = "administrativeArea";
        address.dependentLocality = "dependentLocality";
        address.dependentThroughfare = "dependentThroughfare";
        address.easting = "easting";
        List<String> lines = new ArrayList<>();
        lines.add("building_name");
        lines.add("building_number");
        lines.add("throughfare_name");
        address.lines = lines;
        address.localAuthorityUpdateDate = "localAuthorityUpdateDate";
        address.northing = "northing";
        address.postcode = "postcode";
        address.royalMailUpdateDate = "royalMailUpdateDate";
        address.town = "town";
        address.uprn = "uprn";
        address.moniker = "moniker";
        address.partial = "partial";
        address.organisationName = "organisationName";
        address.thoroughfareName = "thoroughfareName";
        address.buildingName = "buildingName";
        address.buildingNumber = "buildingNumber";
        address.subBuildingName = "subBuildingName";

        return address;
    }
    
    @Test
    public void testSortingByBuildingNumber() {

        // Where an address contains a parse-able house number, it should be
        // used to sort addresses *numerically*:
        //   * 5 should be before 6
        //   * 6 should be before 50.
        List<Address> addresses = Arrays.asList(
            createAddressForSortingTest("6",  "", "The House, The Road, The Town, AB1 2CD"),
            createAddressForSortingTest("50", "", "The House, The Road, The Town, AB1 2CD"),
            createAddressForSortingTest("5",  "", "The House, The Road, The Town, AB1 2CD")
        );
        
        for (Address address : addresses) {
            address.calculateSortingValue();
        }
        
        java.util.Collections.sort(addresses);
        
        Assert.assertEquals("5",  addresses.get(0).buildingNumber);
        Assert.assertEquals("6",  addresses.get(1).buildingNumber);
        Assert.assertEquals("50", addresses.get(2).buildingNumber);
    }
    
    @Test
    public void testSortingByBuildingName() {

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
        
        for (Address address : addresses) {
            address.calculateSortingValue();
        }
        
        java.util.Collections.sort(addresses);
        
        Assert.assertEquals("15",   addresses.get(0).buildingName);
        Assert.assertEquals("15 A", addresses.get(1).buildingName);
        Assert.assertEquals("15b",  addresses.get(2).buildingName);
        Assert.assertEquals("15C",  addresses.get(3).buildingName);
        Assert.assertEquals("150",  addresses.get(4).buildingName);
    }
    
    @Test
    public void testSortingByNumberAndNameMixed() {

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
        
        for (Address address : addresses) {
            address.calculateSortingValue();
        }
        
        java.util.Collections.sort(addresses);
        
        Assert.assertEquals("5",   addresses.get(0).buildingNumber);
        Assert.assertEquals("5A",  addresses.get(1).buildingName);
        Assert.assertEquals("5 C", addresses.get(2).buildingName);
        Assert.assertEquals("7",   addresses.get(3).buildingNumber);
        Assert.assertEquals("50",  addresses.get(4).buildingNumber);
        Assert.assertEquals("50a", addresses.get(5).buildingName);
    }
    
    @Test
    public void testSortingAddressesStartingWithFlat() {

        // Addresses which don't contain a Building Number or Building Name may
        // start with 'Flat NNN'; in this case, sort numerically by Flat Number.
        List<Address> addresses = Arrays.asList(
            createAddressForSortingTest("", "", "Flat 11, The House, The Road, The Town, AB1 2CD"),
            createAddressForSortingTest("", "", "Flat 2, The House, The Road, The Town, AB1 2CD"),
            createAddressForSortingTest("", "", "Flat 10, The House, The Road, The Town, AB1 2CD"),
            createAddressForSortingTest("", "", "Flat 1 , The House, The Road, The Town, AB1 2CD"), // Notice space after flat number
            createAddressForSortingTest("", "", "Flat 3, The House, The Road, The Town, AB1 2CD")
        );

        for (Address address : addresses) {
            address.calculateSortingValue();
        }

        java.util.Collections.sort(addresses);

        Assert.assertEquals("Flat 1 , The House, The Road, The Town, AB1 2CD", addresses.get(0).partial);
        Assert.assertEquals("Flat 2, The House, The Road, The Town, AB1 2CD",  addresses.get(1).partial);
        Assert.assertEquals("Flat 3, The House, The Road, The Town, AB1 2CD",  addresses.get(2).partial);
        Assert.assertEquals("Flat 10, The House, The Road, The Town, AB1 2CD", addresses.get(3).partial);
        Assert.assertEquals("Flat 11, The House, The Road, The Town, AB1 2CD", addresses.get(4).partial);
    }
    
    @Test
    public void testSortingByAllCriteria() {

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
        
        for (Address address : addresses) {
            address.calculateSortingValue();
        }
        
        java.util.Collections.sort(addresses);
        
        Assert.assertEquals("Delta Business, The Road, The Town, AB1 2CD",    addresses.get(0).partial);
        Assert.assertEquals("Gamma Business, The Road, The Town, AB1 2CD",    addresses.get(1).partial);
        Assert.assertEquals("Beta Business, The Road, The Town, AB1 2CD",     addresses.get(2).partial);
        Assert.assertEquals("Alpha Business, The Road, The Town, AB1 2CD",    addresses.get(3).partial);
        Assert.assertEquals("Flat 2, The House, The Road, The Town, AB1 2CD", addresses.get(4).partial);
    }
    
    private Address createAddressForSortingTest(String buildingNumber, String buildingName, String partial) {

        Address address = new Address();
        address.buildingNumber = buildingNumber;
        address.buildingName = buildingName;
        address.partial = partial;

        return address;
    }
}
