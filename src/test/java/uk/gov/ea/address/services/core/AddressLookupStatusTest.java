package uk.gov.ea.address.services.core;

import junit.framework.Assert;

import org.junit.Test;

public class AddressLookupStatusTest
{
    @Test
    public void testAvailable()
    {
        AddressLookupStatus status = new AddressLookupStatus();
        status.setAvailable(true);

        Assert.assertEquals(true, status.isAvailable());
    }

    @Test
    public void testUnAvailable()
    {
        AddressLookupStatus status = new AddressLookupStatus();
        status.setAvailable(false);

        Assert.assertEquals(false, status.isAvailable());
    }
}
