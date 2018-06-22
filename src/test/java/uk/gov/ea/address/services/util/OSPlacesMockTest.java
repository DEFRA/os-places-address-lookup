package uk.gov.ea.address.services.util;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OSPlacesMockTest {

    private static OSPlacesMock osPlaces;

    @BeforeClass
    public static void setup() {
        osPlaces = new OSPlacesMock(200);
    }

    @Test
    public void addresses() {
        String result = osPlaces.addresses("BS1 5AH");

        assertTrue(result.contains("\"UDPRN\" : \"54940150\","));
        assertTrue(result.contains("\"UDPRN\" : \"52044525\","));
        assertTrue(result.contains("\"UDPRN\" : \"54233529\","));
    }

    @Test
    public void addressesNotFound() {
        String result = osPlaces.addresses("WA4 1HT");

        assertTrue(result.contains("\"query\": \"postcode=BS1 5HA\","));
    }

    @Test
    public void address() {
        String result = osPlaces.address("340116");

        assertTrue(result.contains("\"UDPRN\" : \"54940150\","));
    }

    @Test
    public void addressNotFound() {
        String result = osPlaces.address("12345");

        assertTrue(result.contains("\"query\" : \"uprn=0\","));
    }

    @Test
    public void health() {
        String result = osPlaces.health();

        assertNull(result);
    }
}
