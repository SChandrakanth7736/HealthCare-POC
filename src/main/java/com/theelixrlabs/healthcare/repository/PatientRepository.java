package com.theelixrlabs.healthcare.repository;

import com.theelixrlabs.healthcare.constants.QueryConstants;
import com.theelixrlabs.healthcare.model.PatientModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository layer of Patient module which will interact with the database
 */
public interface PatientRepository extends MongoRepository<PatientModel, UUID> {
    /**
     * Checks for any document with specified aadhaarNumber present in db
     *
     * @param patientAadhaarNumber Patient Aadhaar Number as String
     * @return PatientModel object
     */
    Optional<PatientModel> findByPatientAadhaarNumber(String patientAadhaarNumber);

    /**
     *Searches for patients whose first name or last name starts with the specified letters.
     * The search is case-insensitive.
     * @param patientName The starting letters of the patient name to search for.
     * @return List of PatientModel objects that match the search criteria.
     */
    @Query(QueryConstants.SEARCH_PATIENT_BY_NAME_QUERY)
    List<PatientModel> searchByPatientName(String patientName);
}
