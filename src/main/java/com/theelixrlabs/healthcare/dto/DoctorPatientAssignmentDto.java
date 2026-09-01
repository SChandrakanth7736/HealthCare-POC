package com.theelixrlabs.healthcare.dto;

import com.theelixrlabs.healthcare.constants.MessageConstants;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.util.Date;
import java.util.UUID;

/**
 * DTO (Data Transfer Object) representing the assignment of a patient to a doctor.
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class DoctorPatientAssignmentDto {
    private UUID id;
    /**
     * Identifier of the doctor associated with this assignment/unassignment.
     * Required field marked with @NotNull validation.
     */
    @NotNull(message = MessageConstants.DOCTOR_ID_IS_MANDATORY)
    private String doctorId;
    /**
     * Identifier of the patient associated with this assignment/unassignment.
     * Required field marked with @NotNull validation.
     */
    @NotNull(message = MessageConstants.PATIENT_ID_IS_MANDATORY)
    private String patientId;
    //Date when the operation (assign/unassign) occurred.
    private Date dateOfOperation;
}
