package com.sda.projectd.service;

public class CompanyAlreadyExistsException extends ProjectDException {
    public CompanyAlreadyExistsException(String message) {
        super(message);
    }

    public CompanyAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
