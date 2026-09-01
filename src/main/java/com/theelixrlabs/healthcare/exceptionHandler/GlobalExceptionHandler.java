package com.theelixrlabs.healthcare.exceptionHandler;

import com.theelixrlabs.healthcare.response.FailureResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Global exception handler to handling exceptions thrown by REST controllers.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Exception handler for MethodArgumentNotValidException.
     *
     * @param methodArgumentNotValidException MethodArgumentNotValidException exception instance.
     * @return ResponseEntity failure response with error messages.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FailureResponse> handleValidationException(MethodArgumentNotValidException methodArgumentNotValidException) {
        BindingResult errorResults = methodArgumentNotValidException.getBindingResult();
        List<String> errorMessages = new ArrayList<>();
        for (FieldError fieldError : errorResults.getFieldErrors()) {
            errorMessages.add(fieldError.getDefaultMessage());
        }
        return new ResponseEntity<>(new FailureResponse(false, errorMessages), HttpStatus.BAD_REQUEST);
    }

    /**
     * Exception handler for custom exceptions and validations.
     *
     * @param dataException customException instance thrown during runtime.
     * @return ResponseEntity failure response with error messages.
     */
    @ExceptionHandler(DataException.class)
    public ResponseEntity<FailureResponse> handleCustomException(DataException dataException) {
        List<String> errors = new ArrayList<>();
        errors.add(dataException.getMessage());
        return new ResponseEntity<>(new FailureResponse(false, errors), HttpStatus.BAD_REQUEST);
    }

    /**
     * Exception handler for custom exceptions and validations.
     *
     * @param doctorNotFoundException doctorNotFoundException instance thrown during runtime.
     * @return ResponseEntity failure response with error messages.
     */
    @ExceptionHandler(DoctorNotFoundException.class)
    public ResponseEntity<FailureResponse> handleDoctorNotFoundException(DoctorNotFoundException doctorNotFoundException) {
        List<String> errorMessagesList = Collections.singletonList(doctorNotFoundException.getMessage());
        return new ResponseEntity<>(new FailureResponse(false, errorMessagesList), HttpStatus.NOT_FOUND);
    }

    /**
     * Exception handler for custom exceptions and validations.
     *
     * @param doctorException doctorException instance thrown during runtime.
     * @return ResponseEntity failure response with error messages.
     */
    @ExceptionHandler(DoctorException.class)
    public ResponseEntity<FailureResponse> handleDoctorException(DoctorException doctorException) {
        List<String> errorMessagesList = Collections.singletonList(doctorException.getMessage());
        return new ResponseEntity<>(new FailureResponse(false, errorMessagesList), HttpStatus.BAD_REQUEST);
    }

    /**
     * Exception handler for custom exceptions and validations.
     *
     * @param patientNotFoundException patientNotFoundException instance thrown during runtime.
     * @return ResponseEntity failure response with error messages.
     */
    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<FailureResponse> handlePatientNotFoundException(PatientNotFoundException patientNotFoundException) {
        List<String> errorMessagesList = Collections.singletonList(patientNotFoundException.getMessage());
        return new ResponseEntity<>(new FailureResponse(false, errorMessagesList), HttpStatus.NOT_FOUND);
    }

    /**
     * Exception handler for custom exceptions and validations.
     *
     * @param patientException patientException instance thrown during runtime.
     * @return ResponseEntity failure response with error messages.
     */
    @ExceptionHandler(PatientException.class)
    public ResponseEntity<FailureResponse> handlePatientException(PatientException patientException) {
        List<String> errorMessagesList = Collections.singletonList(patientException.getMessage());
        return new ResponseEntity<>(new FailureResponse(false, errorMessagesList), HttpStatus.BAD_REQUEST);
    }

    /**
     * Exception handler for custom exceptions and validations.
     *
     * @param doctorPatientAssignmentException doctorPatientAssignmentException instance thrown during runtime.
     * @return ResponseEntity failure response with error messages.
     */
    @ExceptionHandler(DoctorPatientAssignmentException.class)
    public ResponseEntity<FailureResponse> handleDoctorPatientAssignmentException(DoctorPatientAssignmentException doctorPatientAssignmentException) {
        List<String> errorMessagesList = Collections.singletonList(doctorPatientAssignmentException.getMessage());
        return new ResponseEntity<>(new FailureResponse(false, errorMessagesList), HttpStatus.BAD_REQUEST);
    }

    /**
     * Exception handler method to handle any Exception thrown within the controller or service layer.
     * It returns a ResponseEntity with a FailureResponse object containing the error messages and HTTP status code.
     *
     * @param exception The Exception that was thrown
     * @return ResponseEntity containing a FailureResponse with error messages and HTTP status code INTERNAL_SERVER_ERROR
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<FailureResponse> handleExternalServerError(Exception exception) {
        List<String> errorMessagesList = Collections.singletonList(exception.getMessage());
        return new ResponseEntity<>(new FailureResponse(false, errorMessagesList), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
