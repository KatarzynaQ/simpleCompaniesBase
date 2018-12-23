package com.sda.projectd.service;

public class ProjectDException extends RuntimeException {
    public ProjectDException(String message) {
        super(message);
    }

    public ProjectDException(String message, Throwable cause) {
        super(message, cause);
    }
}
