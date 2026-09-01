package com.theelixrlabs.healthcare.controller;

import com.theelixrlabs.healthcare.constants.ApiPathsConstant;
import com.theelixrlabs.healthcare.constants.DoctorConstants;
import com.theelixrlabs.healthcare.service.DoctorService;
import com.theelixrlabs.healthcare.dto.DoctorDto;
import com.theelixrlabs.healthcare.response.SuccessResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

/**
 * Rest Controller for handling HTTP request and response.
 */
@RestController
public class DoctorController {
    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    /**
     * End point to create new doctor based on provided dto.
     *
     * @param doctorDto : DTO object containing doctor object.
     * @return ResponseEntity containing a success response with created dto.
     */
    @PostMapping(ApiPathsConstant.CREATE_DOCTOR_END_POINT)
    public ResponseEntity<SuccessResponse<DoctorDto>> createDoctor(@Valid @RequestBody DoctorDto doctorDto) throws Exception {
        DoctorDto createdDoctor = doctorService.saveDoctor(doctorDto);
        return new ResponseEntity<>(new SuccessResponse<>(true, createdDoctor, null), HttpStatus.OK);
    }

    /**
     * Retrieves a doctor by their UUID.
     *
     * @param doctorId The ID of the doctor as a String, which will be converted to a UUID.
     * @return ResponseEntity containing a success response with the retrieved DoctorDto.
     */
    @GetMapping(ApiPathsConstant.DOCTOR_BY_ID_ENDPOINT)
    public ResponseEntity<SuccessResponse<DoctorDto>> getDoctorById(@PathVariable(DoctorConstants.PATH_VARIABLE_DOCTOR_ID) String doctorId) throws Exception {
        DoctorDto doctorDto = doctorService.getDoctorById(doctorId);
        return new ResponseEntity<>(new SuccessResponse<>(true, doctorDto, null), HttpStatus.OK);
    }

    /**
     * Deletes a doctor by their ID.
     *
     * @param doctorId The ID of the doctor to delete, obtained from the URL path.
     * @return ResponseEntity containing a SuccessResponse indicating the success status of the deletion operation.
     */
    @DeleteMapping(ApiPathsConstant.DOCTOR_BY_ID_ENDPOINT)
    public ResponseEntity<SuccessResponse<String>> deleteDoctorById(@PathVariable String doctorId) throws Exception {
        return new ResponseEntity<>((new SuccessResponse<>(true, null, doctorService.deleteDoctorById(doctorId))), HttpStatus.OK);
    }

    /**
     * Retrieves a list of doctors by their name.
     *
     * @param doctorName the name of the doctor to search for
     * @return a ResponseEntity containing a SuccessResponse with a list of matching DoctorDto objects
     */
    @GetMapping(ApiPathsConstant.DOCTORS_BY_NAME_ENDPOINT)
    public ResponseEntity<SuccessResponse<List<DoctorDto>>> getDoctorsByName
    (@RequestParam(DoctorConstants.DOCTOR_NAME_PARAM) String doctorName) throws Exception {
        List<DoctorDto> doctorDtoList = doctorService.getDoctorsByName(doctorName);
        return new ResponseEntity<>(new SuccessResponse<>(true, doctorDtoList, null), HttpStatus.OK);
    }
}
