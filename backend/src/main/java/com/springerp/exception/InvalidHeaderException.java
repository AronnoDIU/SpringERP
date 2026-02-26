package com.springerp.exception;

import java.io.Serial;

public class InvalidHeaderException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidHeaderException(String message) {
        super(message);
    }
}

