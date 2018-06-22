package uk.gov.ea.address.services.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
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

        String response = openResourceFile(POSTCODES_PATH, postcode);

        if (response == null || response.isEmpty()) response = openResourceFile(POSTCODES_PATH, "no_results");

        delay();

        return response;
    }

    public String address(String uprn) {

        String response = openResourceFile(UPRN_PATH, uprn);

        if (response == null || response.isEmpty()) response = openResourceFile(UPRN_PATH, "no_results");

        delay();

        return response;
    }

    public String health() {
        return null;
    }

    private String openResourceFile(String path, String name) {

        String content = null;

        String resourcePath = String.format("%s/%s.json", path, name.replaceAll(" ", "").toLowerCase());

        InputStream inputStream = loader.getResourceAsStream(resourcePath);

        if (inputStream != null) content = resourceToString(inputStream);

        return content;
    }

    private String resourceToString(InputStream inputStream) {

        StringBuilder content = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            while((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }

            reader.close();
            inputStream.close();
        } catch (IOException ex) {
            logger.debug("OSPlacesMock - error converting resource to string: " + ex);
        }

        return content.toString();
    }

    private void delay() {

        try {
            Thread.sleep(this.mockDelay);
        } catch (InterruptedException ex) {
            logger.debug("OSPlacesMock - error pausing: " + ex);
        }
    }
}
