package com.theelixrlabs.healthcare.service;

import com.theelixrlabs.healthcare.constants.MessageConstants;
import com.theelixrlabs.healthcare.dto.DoctorDto;
import com.theelixrlabs.healthcare.exceptionHandler.DoctorException;
import com.theelixrlabs.healthcare.exceptionHandler.DoctorNotFoundException;
import com.theelixrlabs.healthcare.model.DoctorModel;
import com.theelixrlabs.healthcare.repository.DoctorRepository;
import com.theelixrlabs.healthcare.utility.MessageUtil;
import com.theelixrlabs.healthcare.utility.PatchUtil;
import com.theelixrlabs.healthcare.validation.Validator;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class for facilitating partial updates to doctor details.
 */
@Service
public class PatchDoctorService {

    private final DoctorRepository doctorRepository;
    private final Validator validator;
    private final MessageUtil messageUtil;
    private final PatchUtil patchUtil;

    public PatchDoctorService(DoctorRepository doctorRepository, Validator validator, MessageUtil messageUtil, PatchUtil patchUtil) {
        this.doctorRepository = doctorRepository;
        this.validator = validator;
        this.messageUtil = messageUtil;
        this.patchUtil = patchUtil;
    }

    /**
     * Replace existing doctor details based on request.
     *
     * @param doctorId  Doctor unique UUID as a String
     * @param doctorDto Data transfer object containing doctor information.
     * @return The replaced doctor details.
     */
    public DoctorDto patchDoctorById(String doctorId, DoctorDto doctorDto) throws Exception {
        UUID validDoctorId = validator.validateAndConvertToUUID(doctorId, MessageConstants.INVALID_UUID);
        validator.validateDoctor(doctorDto);
        Optional<DoctorModel> optionalDoctor = doctorRepository.findById(validDoctorId);
        if (optionalDoctor.isEmpty()) {
            throw new DoctorNotFoundException(messageUtil.getMessage(MessageConstants.DOCTOR_ID_NOT_FOUND));
        }
        DoctorModel existingDoctor = optionalDoctor.get();
        if (patchUtil.hasValueForUpdate(doctorDto.getAadhaarNumber(), existingDoctor.getAadhaarNumber())) {
            String aadhaarNumber = doctorDto.getAadhaarNumber();
            String formattedAadhaarNumber = patchUtil.formatAadhaarNumber(aadhaarNumber);
            if (doctorRepository.findByAadhaarNumber(formattedAadhaarNumber).isPresent()) {
                throw new DoctorException(messageUtil.getMessage(MessageConstants.DOCTOR_AADHAAR_ALREADY_PRESENT));
            }
            existingDoctor.setAadhaarNumber(formattedAadhaarNumber);
        }
        if (patchUtil.hasValueForUpdate(doctorDto.getFirstName(), existingDoctor.getFirstName())) {
            existingDoctor.setFirstName(doctorDto.getFirstName());
        }
        if (patchUtil.hasValueForUpdate(doctorDto.getLastName(), existingDoctor.getLastName())) {
            existingDoctor.setLastName(doctorDto.getLastName());
        }
        if (patchUtil.hasValueForUpdate(doctorDto.getDepartment(), existingDoctor.getDepartment())) {
            existingDoctor.setDepartment(doctorDto.getDepartment());
        }
        doctorRepository.save(existingDoctor);
        return DoctorDto.builder()
                .id(existingDoctor.getId())
                .firstName(existingDoctor.getFirstName())
                .lastName(existingDoctor.getLastName())
                .department(existingDoctor.getDepartment())
                .aadhaarNumber(existingDoctor.getAadhaarNumber())
                .build();
    }
}
