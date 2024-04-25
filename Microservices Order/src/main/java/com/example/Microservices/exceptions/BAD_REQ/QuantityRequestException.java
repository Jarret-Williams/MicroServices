package com.example.Microservices.exceptions.BAD_REQ;

public class QuantityRequestException extends RuntimeException{

    public QuantityRequestException(String message) {
        super(message);
    }
    public QuantityRequestException(String message, Throwable cause) {
        super(message, cause);
    }

}
