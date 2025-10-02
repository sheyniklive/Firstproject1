package org.example.exception;

public class LoadConfigException extends RuntimeException {
    public LoadConfigException(String message) {
        super(message);
    }

    public LoadConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
