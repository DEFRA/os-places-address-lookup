package uk.gov.ea.address.services.util;

public class OSPlacesMock implements IOSPlaces {

    private Integer mockDelay;

    public OSPlacesMock(Integer mockDelay) {
        this.mockDelay = mockDelay;
    }

    public String addresses(String postcode) {
        return "";
    }

    public String address(String uprn) {
        return "";
    }

    public String health() {
        return null;
    }
}
