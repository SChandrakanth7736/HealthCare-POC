package com.theelixrlabs.healthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import java.util.List;

/**
 * Data Transfer Object (DTO) representing a doctor along with their assigned patients.
 */
@Data
@AllArgsConstructor
@SuperBuilder
public class DoctorWithAssignedPatientsDto {
    private DoctorDto doctor;
    private List<PatientDto> assignedPatientsList;
}
