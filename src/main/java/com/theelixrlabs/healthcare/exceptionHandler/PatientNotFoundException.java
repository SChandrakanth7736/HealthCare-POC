package com.theelixrlabs.healthcare.exceptionHandler;

/**
 * Patient exception thrown when a requested resource is not found.
 */
public class PatientNotFoundException extends Exception {
    /**
     * Constructs a new PatientNotFound with the specified detail message
     *
     * @param message the detail message.
     */
    public PatientNotFoundException(String message) {
        super(message);
    }
}
