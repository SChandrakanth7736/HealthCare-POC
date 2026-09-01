package com.theelixrlabs.healthcare.controller;

import com.theelixrlabs.healthcare.constants.ApiPathsConstant;
import com.theelixrlabs.healthcare.dto.PatientDto;
import com.theelixrlabs.healthcare.response.SuccessResponse;
import com.theelixrlabs.healthcare.service.PatchPatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PatchPatientController {

    private final PatchPatientService patchPatientService;

    public PatchPatientController(PatchPatientService patchPatientService) {
        this.patchPatientService = patchPatientService;
    }

    /**
     * Controller method for handling incoming Patch request.
     *
     * @param patientId  Patient id as string.
     * @param patientDto Data transfer object containing patient information.
     * @return ResponseEntity containing success response alone with modified patient details.
     */
    @PatchMapping(ApiPathsConstant.PATIENT_BY_ID_ENDPOINT)
    public ResponseEntity<SuccessResponse<PatientDto>> patchPatientById(@PathVariable String patientId, @RequestBody PatientDto patientDto) throws Exception {
        PatientDto updatedPatient = patchPatientService.patchPatientById(patientId, patientDto);
        return new ResponseEntity<>(new SuccessResponse<>(true, updatedPatient, null), HttpStatus.OK);
    }
}
