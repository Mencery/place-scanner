package com.eleks.placescanner.common.exception.domain;

public class UnexpectedResponseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UnexpectedResponseException(String msg) {
        super(msg);
    }
}
