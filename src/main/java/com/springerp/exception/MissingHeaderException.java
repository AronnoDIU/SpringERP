package com.springerp.exception;

import java.io.Serial;

public class MissingHeaderException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public MissingHeaderException(String message) {
        super(message);
    }
}

