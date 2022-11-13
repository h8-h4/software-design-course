package client;

public class InvalidApiResponseException extends RuntimeException {
    public InvalidApiResponseException(String message) {
        super(message);
    }

    public InvalidApiResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidApiResponseException(Throwable cause) {
        super(cause);
    }
}
