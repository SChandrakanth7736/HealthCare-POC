package com.theelixrlabs.healthcare.constants;

/**
 * Contains query-related constants used in the healthcare system.
 */
public class QueryConstants {
    public static final String SEARCH_DOCTOR_BY_NAME_QUERY = "{$or:[{firstName:{$regex: ?0,$options:'i'}},{lastName:{$regex:?0,$options:'i'}}]}";
    public static final String SEARCH_PATIENT_BY_NAME_QUERY = "{$or:[{patientFirstName:{$regex: ?0,$options:'i'}},{patientLastName:{$regex:?0,$options:'i'}}]}";

}
