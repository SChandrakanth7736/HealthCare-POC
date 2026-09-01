package com.theelixrlabs.healthcare.constants;

/**
 * Contains endpoint constants used throughout the healthcare application.
 */
public class ApiPathsConstant {
    public static final String ASSIGNED_DOCTORS_BY_PATIENT_ID = "/assignedDoctors";
    public static final String ASSIGN_DOCTOR_TO_PATIENT_URL = "/assignDoctorToPatient";
    public static final String CREATE_DOCTOR_END_POINT = "/addDoctor";
    public static final String CREATE_PATIENT_ENDPOINT = "/addPatient";
    public static final String DOCTOR_BY_ID_ENDPOINT = "/doctor/{doctorId}";
    public static final String DOCTORS_BY_NAME_ENDPOINT = "/doctorByName";
    public static final String PATIENTS_BY_DOCTOR_ID_ENDPOINT = "/patientsByDoctorId";
    public static final String PATIENTS_BY_NAME_ENDPOINT = "/patientByName";
    public static final String PATIENT_BY_ID_ENDPOINT = "/patient/{patientId}";
    public static final String UNASSIGN_DOCTOR_FROM_PATIENT_URL = "/unassignDoctorFromPatient";
}
