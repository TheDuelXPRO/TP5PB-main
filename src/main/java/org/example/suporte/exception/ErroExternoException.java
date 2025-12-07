package org.example.suporte.exception;

public class ErroExternoException extends RuntimeException {

    public ErroExternoException(String message, Throwable cause) {
        super(message, cause);
    }
}
