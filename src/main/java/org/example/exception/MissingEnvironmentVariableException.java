package org.example.exception;

public class MissingEnvironmentVariableException extends RuntimeException {

    public MissingEnvironmentVariableException(String variableName) {
        super("Required environment variable " + variableName + " is not set");
    }
}
