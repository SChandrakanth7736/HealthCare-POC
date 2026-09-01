package com.theelixrlabs.healthcare.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a generic success response structure for API responses.
 * A generic success response class to wrap success API responses.
 *
 * @param <T> Type of data contained in response.
 */
@Data
@AllArgsConstructor
public class SuccessResponse<T> {
    /**
     * Indicates whether the operation was successful.
     */
    private boolean success;
    /**
     * The data payload returned as part of the success response.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T responseData;
    /**
     * Optional message providing additional information about the success.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String responseMessage;
}
