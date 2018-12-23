package com.sda.projectd.service;

public class CompanyDoesntExistException extends ProjectDException {
    public CompanyDoesntExistException(String message) {
        super(message);
    }

    public CompanyDoesntExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
