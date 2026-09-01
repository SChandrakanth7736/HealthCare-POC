package com.theelixrlabs.healthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class PatientWithAssignedDoctorsDto {
    private PatientDto patient;
    private List<DoctorDto> assignedDoctors;
}
