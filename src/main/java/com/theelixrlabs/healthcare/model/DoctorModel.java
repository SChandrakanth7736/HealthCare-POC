package com.theelixrlabs.healthcare.model;

import com.theelixrlabs.healthcare.constants.DoctorConstants;
import com.theelixrlabs.healthcare.constants.MessageConstants;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Entity class representing doctors in the healthcare system.
 * This class is mapped to the MongoDB collection specified by DoctorConstants.DOCTOR_COLLECTION_NAME.
 */
@Data
@Document(collection = DoctorConstants.DOCTORS_COLLECTION_NAME)
@SuperBuilder
@NoArgsConstructor
public class DoctorModel {
    @Id
    private UUID id;

    @NotNull(message = MessageConstants.DOCTOR_FIRST_NAME_SHOULD_BE_MANDATORY)
    private String firstName;

    @NotNull(message = MessageConstants.DOCTOR_LAST_NAME_SHOULD_BE_MANDATORY)
    private String lastName;

    @NotNull(message = MessageConstants.DEPARTMENT_SHOULD_BE_MANDATORY)
    private String department;

    @NotNull(message = MessageConstants.DOCTOR_AADHAAR_NUMBER_SHOULD_BE_MANDATORY)
    private String aadhaarNumber;
}
