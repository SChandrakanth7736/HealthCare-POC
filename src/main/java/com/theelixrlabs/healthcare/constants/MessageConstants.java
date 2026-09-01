package com.theelixrlabs.healthcare.constants;

/**
 * Defines constants for validation message keys used in the healthcare system.
 * These keys are mapped to actual messages in the properties file.
 */
public class MessageConstants {
    public static final String DEPARTMENT_SHOULD_BE_MANDATORY = "{doctor.department.mandatory}";
    public static final String DEPARTMENT_SHOULD_NOT_BE_EMPTY = "doctor.department.should.not.be.empty";
    public static final String DOCTOR_AADHAAR_ALREADY_PRESENT = "doctor.aadhaar.number.already.present";
    public static final String DOCTOR_AADHAAR_NUMBER_SHOULD_BE_MANDATORY = "{doctor.aadhaar.number.mandatory}";
    public static final String DOCTOR_AADHAAR_NUMBER_SHOULD_NOT_BE_EMPTY = "doctor.aadhaar.number.should.not.be.empty";
    public static final String DOCTOR_DELETE_SUCCESS_MESSAGE = "doctor.delete.success.message";
    public static final String DOCTOR_DELETION_FAILED_ASSIGNED_TO_PATIENT = "doctor.deletion.failed.assigned.to.patient";
    public static final String DOCTOR_FIRST_NAME_SHOULD_BE_MANDATORY = "{doctor.first.name.mandatory}";
    public static final String DOCTOR_FIRST_NAME_SHOULD_NOT_BE_EMPTY = "doctor.first.name.should.not.be.empty";
    public static final String DOCTOR_ID_CANNOT_BE_EMPTY = "doctor.id.cannot.be.empty";
    public static final String DOCTOR_ID_IS_MANDATORY = "{validation.doctor.id.mandatory}";
    public static final String DOCTOR_ID_NOT_FOUND = "doctor.id.not.found";
    public static final String DOCTOR_INVALID_AADHAAR_NUMBER = "doctor.invalid.aadhaar.number";
    public static final String DOCTOR_INVALID_FIRSTNAME = "doctor.invalid.first.name";
    public static final String DOCTOR_INVALID_LASTNAME = "doctor.invalid.last.name";
    public static final String DOCTOR_LAST_NAME_SHOULD_BE_MANDATORY = "{doctor.last.name.mandatory}";
    public static final String DOCTOR_LAST_NAME_SHOULD_NOT_BE_EMPTY = "doctor.last.name.should.not.be.empty";
    public static final String DOCTOR_NAME_CANNOT_BE_EMPTY = "doctor.name.cannot.be.empty";
    public static final String DOCTOR_NAME_NOT_FOUND = "doctor.name.not.found";
    public static final String INVALID_UUID = "invalid.uuid";
    public static final String NOT_AUTHORISED = "user.not.authorized";
    public static final String PATIENT_AADHAAR_NUMBER_EXISTS = "patient.aadhaar.number.exists";
    public static final String PATIENT_AADHAAR_NUMBER_INVALID = "{patient.aadhaar.number.invalid}";
    public static final String PATIENT_AADHAAR_NUMBER_NOTNULL = "{patient.aadhaar.number.notnull}";
    public static final String PATIENT_AADHAAR_NUMBER_SHOULD_NOT_BE_EMPTY = "patient.aadhaar.number.should.not.be.empty";
    public static final String PATIENT_FIRST_NAME_NOT_NULL = "{patient.first.name.notnull}";
    public static final String PATIENT_ID_IS_MANDATORY = "{validation.patient.id.mandatory}";
    public static final String PATIENT_INVALID_AADHAAR_NUMBER = "patient.invalid.aadhaar.number";
    public static final String PATIENT_LAST_NAME_NOT_NULL = "{patient.last.name.notnull}";
    public static final String PATIENT_NAME_CANNOT_BE_EMPTY = "patient.name.cannot.be.empty";
    public static final String PATIENT_NAME_NOT_FOUND = "patient.name.not.found";
    public static final String PATIENT_NOT_ASSIGNED_TO_DOCTORS = "patient.not.assigned.to.doctors";
}
