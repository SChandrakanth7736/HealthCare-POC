package com.theelixrlabs.healthcare.exceptionHandler;

/**
 * Doctor exception thrown when a requested resource is not found.
 */
public class DoctorNotFoundException extends Exception {
    /**
     * Constructs a new DoctorNotFound with the specified detail message
     *
     * @param message the detail message.
     */
    public DoctorNotFoundException(String message) {
        super(message);
    }
}
