package com.theelixrlabs.healthcare.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

/**
 * A failure response class to wrap failure API responses.
 * Represents a failure response structure for API responses.
 */
@Data
@AllArgsConstructor
public class FailureResponse {
    /**
     * Indicates whether the operation was successful.
     */
    private boolean success;
    /**
     * List of error messages indicating the reasons for failure.
     */
    private List<String> errors;
}
