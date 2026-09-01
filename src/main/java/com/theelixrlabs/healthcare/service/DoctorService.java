package com.theelixrlabs.healthcare.service;

import com.theelixrlabs.healthcare.constants.MessageConstants;
import com.theelixrlabs.healthcare.exceptionHandler.DoctorException;
import com.theelixrlabs.healthcare.exceptionHandler.DoctorNotFoundException;
import com.theelixrlabs.healthcare.repository.DoctorPatientAssignmentRepository;
import com.theelixrlabs.healthcare.model.DoctorModel;
import com.theelixrlabs.healthcare.repository.DoctorRepository;
import com.theelixrlabs.healthcare.dto.DoctorDto;
import com.theelixrlabs.healthcare.utility.MessageUtil;
import com.theelixrlabs.healthcare.validation.Validator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Service class responsible for handling operations related to doctor.
 */
@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorPatientAssignmentRepository doctorPatientAssignmentRepository;
    private final MessageUtil messageUtil;
    private final Validator validator;

    public DoctorService(DoctorRepository doctorRepository, DoctorPatientAssignmentRepository doctorPatientAssignmentRepository, MessageUtil messageUtil, Validator validator) {
        this.doctorRepository = doctorRepository;
        this.doctorPatientAssignmentRepository = doctorPatientAssignmentRepository;
        this.messageUtil = messageUtil;
        this.validator = validator;
    }

    /**
     * Checks if a doctor is assigned to any patients.
     *
     * @param validDoctorId The unique identifier of the doctor to check.
     * @return true if the doctor is assigned to at least one patient, false otherwise.
     */
    private boolean isDoctorAssignedToPatient(UUID validDoctorId) {
        return doctorPatientAssignmentRepository.existsByDoctorIdAndDateOfUnassignmentNull(validDoctorId);
    }

    /**
     * Saves a new doctor document based on the provided DoctorDto.
     *
     * @param doctorDto DoctorDto object containing doctor information.
     * @return The saved dto object.
     */
    public DoctorDto saveDoctor(DoctorDto doctorDto) throws Exception {
        validator.validateDoctor(doctorDto);
        String aadhaarNumber = doctorDto.getAadhaarNumber();
        String formattedAadhaarNumber = aadhaarNumber.substring(0, 4) + StringUtils.SPACE + aadhaarNumber.substring(4, 8) + StringUtils.SPACE + aadhaarNumber.substring(8, 12);
        if (doctorRepository.findByAadhaarNumber(formattedAadhaarNumber).isPresent()) {
            throw new DoctorException(messageUtil.getMessage(MessageConstants.DOCTOR_AADHAAR_ALREADY_PRESENT));
        }
        UUID uuid = UUID.randomUUID();
        DoctorModel doctorModel = DoctorModel.builder().id(uuid).firstName(doctorDto.getFirstName()).lastName(doctorDto.getLastName()).department(doctorDto.getDepartment()).aadhaarNumber(formattedAadhaarNumber).build();
        doctorRepository.save(doctorModel);
        DoctorDto savedDoctorDto = DoctorDto.builder().id(doctorModel.getId()).firstName(doctorModel.getFirstName()).lastName(doctorModel.getLastName()).department(doctorModel.getDepartment()).aadhaarNumber(doctorModel.getAadhaarNumber()).build();
        return savedDoctorDto;
    }

    /**
     * @param doctorId UUID of doctor in String format.
     * @return DoctorDto object containing doctor information.
     */
    public DoctorDto getDoctorById(String doctorId) throws Exception {
        UUID validDoctorId = validator.validateAndConvertToUUID(doctorId, MessageConstants.INVALID_UUID);
        Optional<DoctorModel> doctorModelOptional = doctorRepository.findById(validDoctorId);
        if (doctorModelOptional.isEmpty()) {
            throw new DoctorNotFoundException(messageUtil.getMessage(MessageConstants.DOCTOR_ID_NOT_FOUND));
        }
        DoctorModel doctorModel = doctorModelOptional.get();
        return DoctorDto.builder().id(doctorModel.getId()).firstName(doctorModel.getFirstName()).lastName(doctorModel.getLastName()).department(doctorModel.getDepartment()).aadhaarNumber(doctorModel.getAadhaarNumber()).build();
    }

    /**
     * Deletes a doctor by their id.
     *
     * @param doctorId The string representation of the doctor's UUID.
     * @return A success message upon successful deletion.
     */
    public String deleteDoctorById(String doctorId) throws Exception {
        UUID validDoctorId = validator.validateAndConvertToUUID(doctorId, MessageConstants.INVALID_UUID);

        // Check if a doctor with the validDoctorId exists in the repository.
        if (!doctorRepository.existsById(validDoctorId))
            throw new DoctorNotFoundException(messageUtil.getMessage(MessageConstants.DOCTOR_ID_NOT_FOUND));

        // Check if the doctor is assigned to any patients before deletion.
        if (isDoctorAssignedToPatient(validDoctorId)) {
            throw new DoctorException(messageUtil.getMessage(MessageConstants.DOCTOR_DELETION_FAILED_ASSIGNED_TO_PATIENT));
        }

        // If no patients are assigned to the doctor, proceed with deletion.
        doctorRepository.deleteById(validDoctorId);
        return messageUtil.getMessage(MessageConstants.DOCTOR_DELETE_SUCCESS_MESSAGE, new Object[]{validDoctorId});
    }

    /**
     * Searches for doctors by their name and returns a list of DoctorDto objects.
     *
     * @param doctorName doctorName the name of the doctor to search for
     * @return a list of DoctorDto objects representing the matching doctors
     */
    public List<DoctorDto> getDoctorsByName(String doctorName) throws Exception {
        validator.validateNonEmptyString(doctorName, messageUtil.getMessage(MessageConstants.DOCTOR_NAME_CANNOT_BE_EMPTY));
        List<DoctorModel> doctorModelList = doctorRepository.searchByDoctorName(doctorName);
        if (doctorModelList.isEmpty()) {
            throw new DoctorNotFoundException(messageUtil.getMessage(MessageConstants.DOCTOR_NAME_NOT_FOUND));
        }
        List<DoctorDto> doctorDtoList = new ArrayList<>();
        for (DoctorModel doctorModel : doctorModelList) {
            DoctorDto doctorDto = DoctorDto.builder()
                    .id(doctorModel.getId())
                    .firstName(doctorModel.getFirstName())
                    .lastName(doctorModel.getLastName())
                    .department(doctorModel.getDepartment())
                    .aadhaarNumber(doctorModel.getAadhaarNumber())
                    .build();
            doctorDtoList.add(doctorDto);
        }
        return doctorDtoList;
    }
}
