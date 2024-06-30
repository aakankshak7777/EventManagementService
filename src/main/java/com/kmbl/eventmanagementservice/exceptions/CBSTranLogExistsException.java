package com.kmbl.eventmanagementservice.exceptions;

public class CBSTranLogExistsException extends RuntimeException {

    public CBSTranLogExistsException(String id, Throwable cause) {
        super(String.format("CBSTranLog: %s already exists", id), cause);
    }

    public CBSTranLogExistsException(Exception cause, String msg) {
        super(msg, cause);
    }

    public CBSTranLogExistsException(Exception cause) {
        super(cause);
    }

    public CBSTranLogExistsException(String message) {
        super(message);
    }
}
