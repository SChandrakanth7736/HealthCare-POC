package com.theelixrlabs.healthcare.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.theelixrlabs.healthcare.constants.MessageConstants;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.util.Date;
import java.util.UUID;

/**
 * Data transfer object representing doctor information.
 */
@NoArgsConstructor
@Data
@SuperBuilder
public class DoctorDto {
    private UUID id;

    @NotNull(message = MessageConstants.DOCTOR_FIRST_NAME_SHOULD_BE_MANDATORY)
    private String firstName;

    @NotNull(message = MessageConstants.DOCTOR_LAST_NAME_SHOULD_BE_MANDATORY)
    private String lastName;

    @NotNull(message = MessageConstants.DEPARTMENT_SHOULD_BE_MANDATORY)
    private String department;

    @NotNull(message = MessageConstants.DOCTOR_AADHAAR_NUMBER_SHOULD_BE_MANDATORY)
    private String aadhaarNumber;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date dateOfAssignment;
}
