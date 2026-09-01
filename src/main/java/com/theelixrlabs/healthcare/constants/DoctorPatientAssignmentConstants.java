package com.theelixrlabs.healthcare.constants;

/**
 * Contains the string constants used for the doctor patient assignment module implementation
 */
public class DoctorPatientAssignmentConstants {
    public static final String DB_COLLECTION_NAME = "doctor_patient_assignment";
    public static final String DOCTOR_ALREADY_ASSIGNED_KEY = "doctor.already.assigned";
    public static final String DOCTOR_ALREADY_UNASSIGNED_KEY = "doctor.already.unassigned";
    public static final String DOCTOR_ID_PARAM = "doctorId";
    public static final String DOCTOR_NOT_FOUND_KEY = "doctor.id.not.found";
    public static final String INVALID_DOCTOR_ID_KEY = "doctor.id.invalid";
    public static final String INVALID_PATIENT_ID_KEY = "patient.id.invalid";
    public static final String NO_ASSIGNMENT_EXISTS_KEY = "assignment.not.exists";
    public static final String PATIENT_ID_NOT_FOUND_KEY = "patient.id.not.found";
}
