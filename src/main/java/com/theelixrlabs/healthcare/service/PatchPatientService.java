package com.theelixrlabs.healthcare.service;

import com.theelixrlabs.healthcare.constants.MessageConstants;
import com.theelixrlabs.healthcare.constants.PatientConstants;
import com.theelixrlabs.healthcare.dto.PatientDto;
import com.theelixrlabs.healthcare.exceptionHandler.PatientException;
import com.theelixrlabs.healthcare.exceptionHandler.PatientNotFoundException;
import com.theelixrlabs.healthcare.model.PatientModel;
import com.theelixrlabs.healthcare.repository.DoctorRepository;
import com.theelixrlabs.healthcare.repository.PatientRepository;
import com.theelixrlabs.healthcare.utility.MessageUtil;
import com.theelixrlabs.healthcare.utility.PatchUtil;
import com.theelixrlabs.healthcare.validation.Validator;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
public class PatchPatientService {
    private final PatientRepository patientRepository;
    private final Validator validator;
    private final MessageUtil messageUtil;
    private final DoctorRepository doctorRepository;
    private final PatchUtil patchUtil;

    public PatchPatientService(PatientRepository patientRepository, Validator validator, MessageUtil messageUtil, DoctorRepository doctorRepository, PatchUtil patchUtil) {
        this.patientRepository = patientRepository;
        this.validator = validator;
        this.messageUtil = messageUtil;
        this.doctorRepository = doctorRepository;
        this.patchUtil = patchUtil;
    }

    /**
     * Replace existing patient details based on request.
     *
     * @param patientId  Patient unique UUID as a string
     * @param patientDto Data transfer object containing patient information.
     * @return The replaced patient details.
     */
    public PatientDto patchPatientById(String patientId, PatientDto patientDto) throws Exception {
        UUID validPatientId = validator.validateAndConvertToUUID(patientId, MessageConstants.INVALID_UUID);
        validator.validatePatchPatient(patientDto);
        Optional<PatientModel> optionalPatient = patientRepository.findById(validPatientId);
        if (optionalPatient.isEmpty()) {
            throw new PatientNotFoundException(messageUtil.getMessage(PatientConstants.PATIENT_NOT_FOUND_KEY));
        }
        PatientModel existingPatient = optionalPatient.get();
        if (patchUtil.hasValueForUpdate(patientDto.getPatientAadhaarNumber(), existingPatient.getPatientAadhaarNumber())) {
            String aadhaarNumber = patientDto.getPatientAadhaarNumber();
            String formattedAadhaarNumber = patchUtil.formatAadhaarNumber(aadhaarNumber);
            if (patientRepository.findByPatientAadhaarNumber(formattedAadhaarNumber).isPresent() || doctorRepository.findByAadhaarNumber(formattedAadhaarNumber).isPresent()) {
                throw new PatientException(messageUtil.getMessage(MessageConstants.PATIENT_AADHAAR_NUMBER_EXISTS));
            }
            existingPatient.setPatientAadhaarNumber(formattedAadhaarNumber);
        }
        if (patchUtil.hasValueForUpdate(patientDto.getPatientFirstName(), existingPatient.getPatientFirstName())) {
            existingPatient.setPatientFirstName(patientDto.getPatientFirstName());
        }
        if (patchUtil.hasValueForUpdate(patientDto.getPatientLastName(), existingPatient.getPatientLastName())) {
            existingPatient.setPatientLastName(patientDto.getPatientLastName());
        }
        patientRepository.save(existingPatient);
        return PatientDto.builder()
                .id(existingPatient.getId())
                .patientFirstName(existingPatient.getPatientFirstName())
                .patientLastName(existingPatient.getPatientLastName())
                .patientAadhaarNumber(existingPatient.getPatientAadhaarNumber())
                .build();
    }
}
