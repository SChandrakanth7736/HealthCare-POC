package com.theelixrlabs.healthcare.repository;

import com.theelixrlabs.healthcare.dto.PatientWithAssignedDoctorsDto;
import java.util.List;
import java.util.UUID;

/**
 * Repository interface for retrieving doctors assigned to a patient by patient ID.
 */
public interface DoctorsByPatientIdRepository {
    /**
     * Retrieves a list of patient details and their assigned doctors based on patient ID.
     *
     * @param patientId    The unique identifier of the patient.
     * @return A list of PatientWithAssignedDoctorsDto objects representing patient details and doctors assigned to that patient.
     */
    List<PatientWithAssignedDoctorsDto> getDoctorsByPatientId(UUID patientId);
}
