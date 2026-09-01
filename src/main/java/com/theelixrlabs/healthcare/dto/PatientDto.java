package com.theelixrlabs.healthcare.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.theelixrlabs.healthcare.constants.MessageConstants;
import com.theelixrlabs.healthcare.constants.PatientConstants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.UUID;

/**
 * DTO class for Patient
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class PatientDto {

    private UUID id;

    @NotNull(message = MessageConstants.PATIENT_FIRST_NAME_NOT_NULL)
    private String patientFirstName;

    @NotNull(message = MessageConstants.PATIENT_LAST_NAME_NOT_NULL)
    private String patientLastName;

    @NotNull(message = MessageConstants.PATIENT_AADHAAR_NUMBER_NOTNULL)
    @Pattern(regexp = PatientConstants.AADHAAR_NUMBER_REGEX, message = MessageConstants.PATIENT_AADHAAR_NUMBER_INVALID)
    private String patientAadhaarNumber;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date dateOfAdmission;
}
