package uk.gov.ea.address.services;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

public class AddressLookupConfigurationTest
{
    @Test
    public void testPropertyMap()
    {
        AddressLookupConfiguration conf = new AddressLookupConfiguration();
        conf.setProperties(getPropertyMap());

        Assert.assertNotNull(conf.getProperties());
        Assert.assertEquals(1, conf.getProperties().size());
        Assert.assertEquals("testKey", conf.getProperties().get("key"));
    }

    public Map<String, String> getPropertyMap()
    {
        return new HashMap<String, String>()
        {
            private static final long serialVersionUID = 1L;
            {
                put("key", "testKey");
            }
        };
    }
}
