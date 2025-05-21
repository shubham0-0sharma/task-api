package com.accenture.starter_test.exception;

public class ResourceNotFoundException extends RuntimeException {

    public  ResourceNotFoundException(String msg) {
        super(msg);
    }

}
