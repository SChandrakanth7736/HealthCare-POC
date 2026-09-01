package com.theelixrlabs.healthcare.validation;

import com.theelixrlabs.healthcare.constants.MessageConstants;
import com.theelixrlabs.healthcare.constants.DoctorConstants;
import com.theelixrlabs.healthcare.constants.PatientConstants;
import com.theelixrlabs.healthcare.dto.DoctorDto;
import com.theelixrlabs.healthcare.dto.PatientDto;
import com.theelixrlabs.healthcare.exceptionHandler.DataException;
import com.theelixrlabs.healthcare.exceptionHandler.DoctorException;
import com.theelixrlabs.healthcare.exceptionHandler.PatientException;
import com.theelixrlabs.healthcare.utility.MessageUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import java.util.UUID;

/**
 * Validator class for validating various healthcare-related DTOs and parameters.
 * This class provides methods for validating UUIDs, non-empty strings, doctor details, and patient details.
 */
@Component
public class Validator {

    private final MessageUtil messageUtil;

    public Validator(MessageUtil messageUtil) {
        this.messageUtil = messageUtil;
    }

    /**
     * Validates and converts a given string representation of UUID into a UUID object.
     * Throws a CustomException with a specific error message if the string is not a valid UUID format.
     *
     * @param id           The string representation of UUID to validate and convert.
     * @param errorMessage The error message to use in the CustomException if validation fails.
     * @return The UUID object parsed from the input string.
     */
    public UUID validateAndConvertToUUID(String id, String errorMessage) throws DataException {
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new DataException(messageUtil.getMessage(errorMessage));
        }
        return uuid;
    }

    /**
     * Validates the PatientDTO before adding details.
     *
     * @param patientDto The data transfer object containing patient information.
     */
    public void validatePatientDto(PatientDto patientDto) throws PatientException {

        //Validate first name
        if (patientDto.getPatientFirstName().isEmpty()) {
            throw new PatientException(messageUtil.getMessage(PatientConstants.FIRST_NAME_NOT_EMPTY_KEY));
        } else if (!patientDto.getPatientFirstName().matches(PatientConstants.ALPHA_CHARACTERS_REGEX)) {
            throw new PatientException(messageUtil.getMessage(PatientConstants.INVALID_FIRST_NAME_KEY));
        }

        //Validate last name
        if (patientDto.getPatientLastName().isEmpty()) {
            throw new PatientException(messageUtil.getMessage(PatientConstants.LAST_NAME_SHOULD_NOT_BE_EMPTY_KEY));
        } else if (!patientDto.getPatientLastName().matches(PatientConstants.ALPHA_CHARACTERS_REGEX)) {
            throw new PatientException(messageUtil.getMessage(PatientConstants.INVALID_LAST_NAME_KEY));
        }
    }

    /**
     * Validates that the given input string is not blank.
     * If the input string is blank (null, empty, or whitespace), a CustomException is thrown with the specified error message.
     *
     * @param inputString The string to be validated.
     */
    public void validateNonEmptyString(String inputString, String errorMessage) throws DataException {
        if (StringUtils.isBlank(inputString)) {
            throw new DataException(errorMessage);
        }
    }

    /**
     * Validating method to validate based on incoming request.
     *
     * @param doctorDto Data transfer object containing doctor information.
     */
    public void validateDoctor(DoctorDto doctorDto) throws DoctorException {
        if (doctorDto.getFirstName() != null) {
            if (doctorDto.getFirstName().isEmpty()) {
                throw new DoctorException(messageUtil.getMessage(MessageConstants.DOCTOR_FIRST_NAME_SHOULD_NOT_BE_EMPTY));
            }
            if (!doctorDto.getFirstName().matches(DoctorConstants.ALPHA_CHARACTERS_REGEX)) {
                throw new DoctorException(messageUtil.getMessage(MessageConstants.DOCTOR_INVALID_FIRSTNAME));
            }
        }
        if (doctorDto.getLastName() != null) {
            if (doctorDto.getLastName().isEmpty()) {
                throw new DoctorException(messageUtil.getMessage(MessageConstants.DOCTOR_LAST_NAME_SHOULD_NOT_BE_EMPTY));
            }
            if (!doctorDto.getLastName().matches(DoctorConstants.ALPHA_CHARACTERS_REGEX)) {
                throw new DoctorException(messageUtil.getMessage(MessageConstants.DOCTOR_INVALID_LASTNAME));
            }
        }
        if (doctorDto.getDepartment() != null) {
            if (doctorDto.getDepartment().isEmpty()) {
                throw new DoctorException(messageUtil.getMessage(MessageConstants.DEPARTMENT_SHOULD_NOT_BE_EMPTY));
            }
        }
        if (doctorDto.getAadhaarNumber() != null) {
            if (doctorDto.getAadhaarNumber().isEmpty()) {
                throw new DoctorException(messageUtil.getMessage(MessageConstants.DOCTOR_AADHAAR_NUMBER_SHOULD_NOT_BE_EMPTY));
            }
            if (!(doctorDto.getAadhaarNumber().matches(DoctorConstants.AADHAAR_REGEX_PATTERN))) {
                throw new DoctorException(messageUtil.getMessage(MessageConstants.DOCTOR_INVALID_AADHAAR_NUMBER));
            }
        }
    }

    /**
     * Validating method to validate based on incoming request.
     *
     * @param patientDto Data transfer object containing patient information.
     */
    public void validatePatchPatient(PatientDto patientDto) throws PatientException {
        if (patientDto.getPatientFirstName() != null) {
            if (patientDto.getPatientFirstName().isEmpty()) {
                throw new PatientException(messageUtil.getMessage(PatientConstants.FIRST_NAME_NOT_EMPTY_KEY));
            }
            if (!patientDto.getPatientFirstName().matches(PatientConstants.ALPHA_CHARACTERS_REGEX)) {
                throw new PatientException(messageUtil.getMessage(PatientConstants.INVALID_FIRST_NAME_KEY));
            }
        }
        if (patientDto.getPatientLastName() != null) {
            if (patientDto.getPatientLastName().isEmpty()) {
                throw new PatientException(messageUtil.getMessage(PatientConstants.LAST_NAME_SHOULD_NOT_BE_EMPTY_KEY));
            }
            if (!patientDto.getPatientLastName().matches(PatientConstants.ALPHA_CHARACTERS_REGEX)) {
                throw new PatientException(messageUtil.getMessage(PatientConstants.INVALID_LAST_NAME_KEY));
            }
        }
        if (patientDto.getPatientAadhaarNumber() != null) {
            if (patientDto.getPatientAadhaarNumber().isEmpty()) {
                throw new PatientException(messageUtil.getMessage(MessageConstants.PATIENT_AADHAAR_NUMBER_SHOULD_NOT_BE_EMPTY));
            }
            if (!(patientDto.getPatientAadhaarNumber().matches(PatientConstants.AADHAAR_NUMBER_REGEX))) {
                throw new PatientException(messageUtil.getMessage(MessageConstants.PATIENT_INVALID_AADHAAR_NUMBER));
            }
        }
    }
}
