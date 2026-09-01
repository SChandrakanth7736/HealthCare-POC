package com.theelixrlabs.healthcare.constants;

/**
 * Constant class containing error messages and endpoints of doctor.
 */
public class TestConstants {
    public static final String DELETION_FAILED = "Deletion Failed. Patient is currently assigned to a Doctor.";
    public static final String DOCTOR_ENDPOINT = "/doctor/";
    public static final String DOCTOR_NAME = "ABCDEFGH";
    public static final String DOCTOR_NAME_NOT_FOUND = "doctor.name.not.found";
    public static final String DOCTOR_NOT_FOUND_MESSAGE = "doctor not found";
    public static final String ERRORS_ARRAY_EXPRESSION = "$.errors";
    public static final String INVALID_UUID = "Invalid uuid";
    public static final String PATIENT_BY_ID = "/patient/{patientId}";
    public static final String PATIENT_DELETED_SUCCESSFULLY = "Patient deleted successfully";
    public static final String PATIENT_ID = "1a3d5906-fa50-4f2e-b02c-5ebc76ccfb30";
    public static final String PATIENT_NOT_FOUND = "Patient not found.";
    public static final String RESPONSE_MESSAGE_EXPRESSION = "$.responseMessage";
    public static final String SUCCESS_EXPRESSION = "$.success";
    public static final String TEST_RESULT_ARRAY_EXPRESSION = "$.errors[0]";
}
