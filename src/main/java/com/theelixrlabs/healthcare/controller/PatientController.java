package com.theelixrlabs.healthcare.controller;

import com.theelixrlabs.healthcare.constants.ApiPathsConstant;
import com.theelixrlabs.healthcare.constants.PatientConstants;
import com.theelixrlabs.healthcare.dto.PatientDto;
import com.theelixrlabs.healthcare.response.SuccessResponse;
import com.theelixrlabs.healthcare.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

/**
 * Patient Controller layer, which interacts with the user.
 * All the requests will be handled by PatientController.
 * Patient Controller sends the responses to the user.
 */
@RestController
public class PatientController {

    private final PatientService patientService;

    //Constructor Injection of PatientService and MessageSource
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    /**
     * Controller method for handling POST requests to create a new patient.
     *
     * @param patientDto    The data transfer object (DTO) containing patient information.
     * @return ResponseEntity containing a SuccessResponse with the created PatientDTO and HTTP status 201 (created).
     */
    @PostMapping(ApiPathsConstant.CREATE_PATIENT_ENDPOINT)
    public ResponseEntity<SuccessResponse<PatientDto>> addPatientDetails(@RequestBody @Valid PatientDto patientDto) throws Exception {
        return new ResponseEntity<>(new SuccessResponse<>(true, patientService.addPatientDetails(patientDto), null), HttpStatus.CREATED);
    }

    /**
     * Controller method for handling GET requests to get a patient by ID
     *
     * @param patientId    Patient ID as String
     * @return ResponseEntity containing a SuccessResponse with the Patient Associated with the ID and HTTP status 200 (OK)
     */
    @GetMapping(ApiPathsConstant.PATIENT_BY_ID_ENDPOINT)
    public ResponseEntity<SuccessResponse<PatientDto>> getPatientById(@PathVariable String patientId) throws Exception {
        return new ResponseEntity<>(new SuccessResponse<>(true, patientService.getPatientById(patientId), null), HttpStatus.OK);
    }

    /**
     * Endpoint to delete a patient by ID.
     *
     * @param patientId    The ID of the patient to delete, provided as a path variable.
     * @return ResponseEntity containing a SuccessResponse indicating success or failure.
     */
    @DeleteMapping(ApiPathsConstant.PATIENT_BY_ID_ENDPOINT)
    public ResponseEntity<SuccessResponse<String>> deletePatientById(@PathVariable String patientId) throws Exception {
        return new ResponseEntity<>(new SuccessResponse<>(true, null, patientService.deletePatientById(patientId)), HttpStatus.OK);
    }

    /**
     * Retrieves a list of patients whose first or last name starts with the specified name.
     *
     * @param patientName The partial name to search for patients. It matches both first and last names.
     * @return A ResponseEntity containing a SuccessResponse with the list of matching PatientDTO objects and HTTP status OK.
     */
    @GetMapping(ApiPathsConstant.PATIENTS_BY_NAME_ENDPOINT)
    public ResponseEntity<SuccessResponse<List<PatientDto>>> getPatientsByName(@RequestParam(PatientConstants.PATIENT_NAME_PARAM) String patientName) throws Exception {
        List<PatientDto> patientDtoList = patientService.getPatientsByName(patientName);
        return new ResponseEntity<>(new SuccessResponse<>(true, patientDtoList, null), HttpStatus.OK);
    }
}
