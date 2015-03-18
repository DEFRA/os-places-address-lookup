package uk.gov.ea.address.services.exception;

public class OSPlacesClientException extends Exception
{
    private static final long serialVersionUID = 1L;

    public OSPlacesClientException(String message)
    {
        super(message);
    }

    public OSPlacesClientException(String message, Throwable t)
    {
        super(message, t);
    }

    @Override
    public String toString()
    {
        return getMessage();
    }

}
