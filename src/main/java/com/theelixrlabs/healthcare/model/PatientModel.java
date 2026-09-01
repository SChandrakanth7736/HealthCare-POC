package com.theelixrlabs.healthcare.model;

import com.theelixrlabs.healthcare.constants.PatientConstants;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.UUID;

/**
 * Entity class of Patients
 */
@NoArgsConstructor
@Data
@Document(collection = PatientConstants.PATIENTS_COLLECTION_NAME)
@SuperBuilder
public class PatientModel {

    @Id
    private UUID id;
    private String patientFirstName;
    private String patientLastName;
    private String patientAadhaarNumber;
}
