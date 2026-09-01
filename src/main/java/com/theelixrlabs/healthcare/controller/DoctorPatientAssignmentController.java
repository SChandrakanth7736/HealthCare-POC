package com.theelixrlabs.healthcare.controller;

import com.theelixrlabs.healthcare.constants.ApiPathsConstant;
import com.theelixrlabs.healthcare.constants.DoctorPatientAssignmentConstants;
import com.theelixrlabs.healthcare.dto.DoctorPatientAssignmentDto;
import com.theelixrlabs.healthcare.dto.DoctorWithAssignedPatientsDto;
import com.theelixrlabs.healthcare.dto.PatientWithAssignedDoctorsDto;
import com.theelixrlabs.healthcare.response.SuccessResponse;
import com.theelixrlabs.healthcare.service.DoctorPatientAssignmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class DoctorPatientAssignmentController {

    private final DoctorPatientAssignmentService doctorPatientAssignmentService;

    public DoctorPatientAssignmentController(DoctorPatientAssignmentService doctorPatientAssignmentService) {
        this.doctorPatientAssignmentService = doctorPatientAssignmentService;
    }

    /**
     * End point to create assign doctor to patient based on provided dto.
     *
     * @param doctorPatientAssignmentDto : DTO object containing doctorId and patientId.
     * @return ResponseEntity containing a success response with created dto.
     */
    @PostMapping(ApiPathsConstant.ASSIGN_DOCTOR_TO_PATIENT_URL)
    public ResponseEntity<SuccessResponse<DoctorPatientAssignmentDto>> assignDoctorToPatient(@Valid @RequestBody DoctorPatientAssignmentDto doctorPatientAssignmentDto) throws Exception {
        return new ResponseEntity<>(new SuccessResponse<>(true, doctorPatientAssignmentService.assignDoctorToPatient(doctorPatientAssignmentDto), null), HttpStatus.OK);
    }

    /**
     * End point to  unassign doctor from patient based on provided dto.
     *
     * @param doctorPatientAssignmentDto : DTO object containing doctorId and patientId.
     * @return ResponseEntity containing a success response with created dto.
     */
    @PostMapping(ApiPathsConstant.UNASSIGN_DOCTOR_FROM_PATIENT_URL)
    public ResponseEntity<SuccessResponse<DoctorPatientAssignmentDto>> unassignDoctorFromPatient(@Valid @RequestBody DoctorPatientAssignmentDto doctorPatientAssignmentDto) throws Exception {
        doctorPatientAssignmentService.unassignDoctorFromPatient(doctorPatientAssignmentDto);
        return new ResponseEntity<>(new SuccessResponse<>(true, null, null), HttpStatus.OK);
    }

    /**
     * Handles the GET request to retrieve a list of patients assigned to a doctor by the doctor's ID.
     *
     * @param doctorId The UUID of the doctor for whom to retrieve the list of assigned patients.
     * @return ResponseEntity containing a SuccessResponse with the DoctorWithPatientsDto.
     * @throws Exception If any error occurs during the retrieval process, an exception is thrown.
     */
    @GetMapping(ApiPathsConstant.PATIENTS_BY_DOCTOR_ID_ENDPOINT)
    public ResponseEntity<SuccessResponse<DoctorWithAssignedPatientsDto>> getPatientsByDoctorId(@RequestParam(DoctorPatientAssignmentConstants.DOCTOR_ID_PARAM) String doctorId) throws Exception {
        DoctorWithAssignedPatientsDto doctorWithPatientsDto = doctorPatientAssignmentService.getPatientsByDoctorId(doctorId);
        return ResponseEntity.ok(new SuccessResponse<>(true, doctorWithPatientsDto, null));
    }

    /**
     * GET endpoint to retrieve assigned doctors for a patient by patient ID.
     *
     * @param patientId The string representation of patient ID obtained from request parameter.
     * @return ResponseEntity containing SuccessResponse with PatientWithAssignedDoctorsDto if successful, or error details if exception occurs.
     * @throws Exception if there's an error during retrieval of assigned doctors.
     */
    @GetMapping(ApiPathsConstant.ASSIGNED_DOCTORS_BY_PATIENT_ID)
    public ResponseEntity<SuccessResponse<PatientWithAssignedDoctorsDto>> getAssignedDoctorsByPatientId(@RequestParam String patientId) throws Exception {
        return new ResponseEntity<>(new SuccessResponse<>(true, doctorPatientAssignmentService.getDoctorsByPatientId(patientId), null), HttpStatus.OK);
    }
}
