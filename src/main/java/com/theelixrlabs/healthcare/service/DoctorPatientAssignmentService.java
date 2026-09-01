package com.theelixrlabs.healthcare.service;

import com.theelixrlabs.healthcare.constants.DoctorPatientAssignmentConstants;
import com.theelixrlabs.healthcare.constants.MessageConstants;
import com.theelixrlabs.healthcare.dto.DoctorDto;
import com.theelixrlabs.healthcare.dto.PatientDto;
import com.theelixrlabs.healthcare.dto.DoctorPatientAssignmentDto;
import com.theelixrlabs.healthcare.dto.DoctorWithAssignedPatientsDto;
import com.theelixrlabs.healthcare.dto.PatientWithAssignedDoctorsDto;
import com.theelixrlabs.healthcare.exceptionHandler.DoctorNotFoundException;
import com.theelixrlabs.healthcare.exceptionHandler.DoctorPatientAssignmentException;
import com.theelixrlabs.healthcare.exceptionHandler.PatientNotFoundException;
import com.theelixrlabs.healthcare.model.DoctorModel;
import com.theelixrlabs.healthcare.model.PatientModel;
import com.theelixrlabs.healthcare.repository.DoctorPatientAssignmentRepository;
import com.theelixrlabs.healthcare.model.DoctorPatientAssignmentModel;
import com.theelixrlabs.healthcare.repository.DoctorRepository;
import com.theelixrlabs.healthcare.repository.PatientRepository;
import com.theelixrlabs.healthcare.utility.MessageUtil;
import com.theelixrlabs.healthcare.validation.Validator;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.ArrayList;

/**
 * Service class for managing doctor-patient assignments.
 * This class provides methods to assign and unassign doctors to patients,
 * and to retrieve patients assigned to a specific doctor.
 */
@Service
public class DoctorPatientAssignmentService {
    private final DoctorPatientAssignmentRepository doctorPatientAssignmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final MessageUtil messageUtil;
    private final Validator validator;

    public DoctorPatientAssignmentService(DoctorPatientAssignmentRepository doctorPatientAssignmentRepository,
                                          PatientRepository patientRepository, DoctorRepository doctorRepository, MessageUtil messageUtil, Validator validator) {
        this.doctorPatientAssignmentRepository = doctorPatientAssignmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.messageUtil = messageUtil;
        this.validator = validator;
    }

    /**
     * Checks if doctor is previously assigned to same patient and if not then saves the doctorPatientAssignmentDto
     *
     * @param doctorPatientAssignmentDto    DTO object containing doctorId and patientId
     * @return DoctorPatientAssignmentDto
     */
    public DoctorPatientAssignmentDto assignDoctorToPatient(DoctorPatientAssignmentDto doctorPatientAssignmentDto) throws Exception {
        UUID doctorId = validator.validateAndConvertToUUID(doctorPatientAssignmentDto.getDoctorId(),
                DoctorPatientAssignmentConstants.INVALID_DOCTOR_ID_KEY);
        UUID patientId = validator.validateAndConvertToUUID(doctorPatientAssignmentDto.getPatientId(),
                DoctorPatientAssignmentConstants.INVALID_PATIENT_ID_KEY);
        validateDoctorPatientExistence(doctorId, patientId);
        if (isDoctorAlreadyAssigned(doctorId, patientId)) {
            throw new DoctorPatientAssignmentException(messageUtil.getMessage(DoctorPatientAssignmentConstants.DOCTOR_ALREADY_ASSIGNED_KEY));
        }
        return mapDoctorPatientAssignment(doctorPatientAssignmentDto);
    }

    /**
     * checks for doctor patient assignment and if present then unassigns doctor from patient
     *
     * @param doctorPatientAssignmentDto    DTO object containing doctorId and patientId
     */
    public void unassignDoctorFromPatient(DoctorPatientAssignmentDto doctorPatientAssignmentDto) throws Exception {
        UUID doctorId = validator.validateAndConvertToUUID(doctorPatientAssignmentDto.getDoctorId(),
                DoctorPatientAssignmentConstants.INVALID_DOCTOR_ID_KEY);
        UUID patientId = validator.validateAndConvertToUUID(doctorPatientAssignmentDto.getPatientId(),
                DoctorPatientAssignmentConstants.INVALID_PATIENT_ID_KEY);
        validateDoctorPatientExistence(doctorId, patientId);
        Optional<DoctorPatientAssignmentModel> optionalDoctorPatientAssignmentModel =
                doctorPatientAssignmentRepository.findByDoctorIdAndPatientIdAndDateOfUnassignmentNull(doctorId, patientId);
        if (!doctorPatientAssignmentRepository.existsByDoctorIdAndPatientId(doctorId, patientId)) {
            throw new DoctorPatientAssignmentException(messageUtil.getMessage(DoctorPatientAssignmentConstants.NO_ASSIGNMENT_EXISTS_KEY));
        }
        if (optionalDoctorPatientAssignmentModel.isEmpty()) {
            throw new DoctorPatientAssignmentException(messageUtil.getMessage(DoctorPatientAssignmentConstants.DOCTOR_ALREADY_UNASSIGNED_KEY));
        }
        DoctorPatientAssignmentModel activeDoctorPatientAssignmentModel = optionalDoctorPatientAssignmentModel.get();
        activeDoctorPatientAssignmentModel.setDateOfUnassignment(Date.from(Instant.now()));
        doctorPatientAssignmentRepository.save(activeDoctorPatientAssignmentModel);
    }

    /**
     * Retrieves patient details and their assigned doctors based on patient ID.
     *
     * @param patientId    The string representation of patient ID.
     * @return PatientWithAssignedDoctorsDto containing patient details and their assigned doctors.
     * @throws Exception    if patient is not found or not assigned to any doctors.
     */
    public PatientWithAssignedDoctorsDto getDoctorsByPatientId(String patientId) throws Exception {
        UUID validPatientId = validator.validateAndConvertToUUID(patientId, MessageConstants.INVALID_UUID);
        if (!patientRepository.existsById(validPatientId)) {
            throw new PatientNotFoundException(messageUtil.getMessage(DoctorPatientAssignmentConstants.PATIENT_ID_NOT_FOUND_KEY));
        }
        List<PatientWithAssignedDoctorsDto> patientWithAssignedDoctorsDtoList = doctorPatientAssignmentRepository.getDoctorsByPatientId(validPatientId);
        if (patientWithAssignedDoctorsDtoList.isEmpty()) {
            throw new DoctorPatientAssignmentException(messageUtil.getMessage(MessageConstants.PATIENT_NOT_ASSIGNED_TO_DOCTORS, new Object[]{validPatientId}));
        }
        return patientWithAssignedDoctorsDtoList.get(0);
    }

    private void validateDoctorPatientExistence(UUID doctorId, UUID patientId) throws Exception {
        if (!doctorRepository.existsById(doctorId)) {
            throw new DoctorNotFoundException(messageUtil.getMessage(DoctorPatientAssignmentConstants.DOCTOR_NOT_FOUND_KEY));
        }
        if (!patientRepository.existsById(patientId)) {
            throw new PatientNotFoundException(messageUtil.getMessage(DoctorPatientAssignmentConstants.PATIENT_ID_NOT_FOUND_KEY));
        }
    }

    private boolean isDoctorAlreadyAssigned(UUID doctorId, UUID patientId) {
        Optional<DoctorPatientAssignmentModel> activeDoctorPatientAssignmentModel = doctorPatientAssignmentRepository
                .findByDoctorIdAndPatientIdAndDateOfUnassignmentNull(doctorId, patientId);
        return activeDoctorPatientAssignmentModel.isPresent();
    }

    private DoctorPatientAssignmentDto mapDoctorPatientAssignment(DoctorPatientAssignmentDto doctorPatientAssignmentDTO) {
        DoctorPatientAssignmentDto responseDoctorPatientAssignmentDto;
        DoctorPatientAssignmentModel doctorPatientAssignmentModel = DoctorPatientAssignmentModel.builder()
                .id(UUID.randomUUID())
                .doctorId(UUID.fromString(doctorPatientAssignmentDTO.getDoctorId()))
                .patientId(UUID.fromString(doctorPatientAssignmentDTO.getPatientId()))
                .dateOfAssignment(Date.from(Instant.now()))
                .dateOfUnassignment(null)
                .build();
        doctorPatientAssignmentRepository.save(doctorPatientAssignmentModel);
        responseDoctorPatientAssignmentDto = DoctorPatientAssignmentDto.builder()
                .id(doctorPatientAssignmentModel.getId())
                .doctorId(doctorPatientAssignmentModel.getDoctorId().toString())
                .patientId(doctorPatientAssignmentModel.getPatientId().toString())
                .dateOfOperation(doctorPatientAssignmentModel.getDateOfAssignment())
                .build();
        return responseDoctorPatientAssignmentDto;
    }

    /**
     * Retrieves the patients assigned to a doctor by the doctor's ID.
     *
     * @param doctorId    The ID of the doctor for whom the patient list is to be retrieved.
     * @return A DoctorWithPatientsDto object containing the doctor's details and a list of assigned patients.
     * @throws Exception    if the doctor ID is invalid, the doctor is not found, or if any patient assigned to the doctor is not found.
     */
    public DoctorWithAssignedPatientsDto getPatientsByDoctorId(String doctorId) throws Exception {
        validator.validateNonEmptyString(doctorId, messageUtil.getMessage(MessageConstants.DOCTOR_ID_CANNOT_BE_EMPTY));
        UUID validatedDoctorId = validator.validateAndConvertToUUID(doctorId, MessageConstants.INVALID_UUID);
        DoctorModel doctorModel = doctorRepository.findById(validatedDoctorId)
                .orElseThrow(() -> new DoctorNotFoundException(messageUtil.getMessage(MessageConstants.DOCTOR_ID_NOT_FOUND)));
        // Fetch doctorPatientAssignmentsList
        List<DoctorPatientAssignmentModel> doctorPatientAssignmentsList = doctorPatientAssignmentRepository.findByDoctorIdAndDateOfUnassignmentNull(validatedDoctorId);
        if (doctorPatientAssignmentsList.isEmpty()) {
            throw new DoctorNotFoundException(messageUtil.getMessage(DoctorPatientAssignmentConstants.NO_ASSIGNMENT_EXISTS_KEY));
        }
        DoctorDto doctorDto = DoctorDto.builder()
                .id(doctorModel.getId())
                .firstName(doctorModel.getFirstName())
                .lastName(doctorModel.getLastName())
                .department(doctorModel.getDepartment())
                .aadhaarNumber(doctorModel.getAadhaarNumber())
                .build();
        // Extract patients with dateOfAssignment
        List<PatientDto> patientsList = new ArrayList<>();
        for (DoctorPatientAssignmentModel doctorPatientAssignment : doctorPatientAssignmentsList) {
            PatientModel patientModel = patientRepository.findById(doctorPatientAssignment.getPatientId())
                    .orElseThrow(() -> new PatientNotFoundException(messageUtil.getMessage(DoctorPatientAssignmentConstants.PATIENT_ID_NOT_FOUND_KEY)));
            PatientDto patientDto = PatientDto.builder()
                    .id(patientModel.getId())
                    .patientFirstName(patientModel.getPatientFirstName())
                    .patientLastName(patientModel.getPatientLastName())
                    .patientAadhaarNumber(patientModel.getPatientAadhaarNumber())
                    .dateOfAdmission(doctorPatientAssignment.getDateOfAssignment()) //used the dateOfAssignment from the doctorPatientAssignmentsList.
                    .build();
            patientsList.add(patientDto);
        }
        return new DoctorWithAssignedPatientsDto(doctorDto, patientsList);
    }
}
