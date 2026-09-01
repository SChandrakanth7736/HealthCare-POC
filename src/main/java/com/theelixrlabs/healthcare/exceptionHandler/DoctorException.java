package com.theelixrlabs.healthcare.exceptionHandler;

public class DoctorException extends Exception{
    /**
     * Constructor to create a DoctorException with a specific exception message.
     *
     * @param exceptionMessage The message describing the exception.
     */
    public DoctorException(String exceptionMessage) {
        // Call the constructor of the superclass (Exception) with the provided exception message
        super(exceptionMessage);
    }
}
