package uk.gov.ea.address.services.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class OSPlacesMock implements IOSPlaces {

    private static final String POSTCODES_PATH = "mocks/postcode";
    private static final String UPRN_PATH = "mocks/uprn";

    private static Logger logger = LoggerFactory.getLogger(AddressSearch.class);

    private Integer mockDelay;

    private ClassLoader loader;

    public OSPlacesMock(Integer mockDelay) {
        this.mockDelay = mockDelay;
        this.loader = this.getClass().getClassLoader();
    }

    public String addresses(String postcode) {

        File resource = resourceFile(POSTCODES_PATH, postcode);

        if (resource == null) resource = noResultsFile(POSTCODES_PATH);

        delay();

        return fileToString(resource);
    }

    public String address(String uprn) {

        File resource = resourceFile(UPRN_PATH, uprn);

        if (resource == null) resource = noResultsFile(UPRN_PATH);

        delay();

        return fileToString(resource);
    }

    public String health() {
        return null;
    }

    private File resourceFile(String path, String name) {

        String fullPath = String.format("%s/%s.json", path, name.replaceAll(" ", "").toLowerCase());

        URL url = loader.getResource(fullPath);
        if (url != null) return new File(url.getFile());

        return null;
    }

    private File noResultsFile(String path) {
        String fullPath = String.format("%s/no_results.json", path);

        URL url = loader.getResource(fullPath);

        return new File(url.getFile());
    }

    private void delay() {

        try {
            Thread.sleep(this.mockDelay);
        } catch (InterruptedException ex) {
            logger.debug("OSPlacesMock - error pausing: " + ex);
        }
    }

    private String fileToString(File resource) {

        String content = null;

        try {
            byte[] encoded = Files.readAllBytes(resource.toPath());
            content = new String(encoded, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            logger.debug("OSPlacesMock - error reading mock file: " + ex);
        }

        return content;
    }
}
