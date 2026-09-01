package com.theelixrlabs.healthcare.repository;

import com.theelixrlabs.healthcare.constants.QueryConstants;
import com.theelixrlabs.healthcare.model.DoctorModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository layer for the Doctor module which interacts with the database.
 * Provides methods for CRUD operations and custom queries.
 */
@Repository
public interface DoctorRepository extends MongoRepository<DoctorModel, UUID> {
    /**
     * Finds a doctor by their Aadhaar number.
     *
     * @param aadhaarNumber The Aadhaar number of the doctor.
     * @return An Optional containing the DoctorModel if found, otherwise empty.
     */
    Optional<DoctorModel> findByAadhaarNumber(String aadhaarNumber);

    /**
     * Searches for doctors whose first name or last name starts with the specified letters.
     * The search is case-insensitive.
     *
     * @param doctorName The starting letters of the doctor name to search for.
     * @return List of DoctorModel objects that match the search criteria.
     */
    @Query(QueryConstants.SEARCH_DOCTOR_BY_NAME_QUERY)
    List<DoctorModel> searchByDoctorName(String doctorName);
}
