package com.kmbl.eventmanagementservice.exceptions;

public class CollectCallbackExistsException extends RuntimeException {

    public CollectCallbackExistsException(String id, Throwable cause) {
        super(String.format("CollectCallback: %s already exists", id), cause);
    }

    public CollectCallbackExistsException(Exception cause, String msg) {
        super(msg, cause);
    }

    public CollectCallbackExistsException(Exception cause) {
        super(cause);
    }

    public CollectCallbackExistsException(String message) {
        super(message);
    }
}
