package com.theelixrlabs.healthcare.service;

import com.theelixrlabs.healthcare.constants.PatientConstants;
import com.theelixrlabs.healthcare.constants.TestConstants;
import com.theelixrlabs.healthcare.dto.PatientDto;
import com.theelixrlabs.healthcare.exceptionHandler.PatientNotFoundException;
import com.theelixrlabs.healthcare.model.PatientModel;
import com.theelixrlabs.healthcare.exceptionHandler.PatientException;
import com.theelixrlabs.healthcare.model.DoctorPatientAssignmentModel;
import com.theelixrlabs.healthcare.repository.DoctorPatientAssignmentRepository;
import com.theelixrlabs.healthcare.repository.PatientRepository;
import com.theelixrlabs.healthcare.utility.MessageUtil;
import com.theelixrlabs.healthcare.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

/**
 * Contains test methods for Patient Service
 * uses mock Repository to handle db requests
 */
public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private MessageUtil messageUtil;

    @Mock
    private Validator validator;

    @Mock
    private DoctorPatientAssignmentRepository doctorPatientAssignmentRepository;

    @InjectMocks
    private PatientService patientService;

    private PatientModel patientModel;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        patientModel = PatientModel.builder()
                .id(UUID.randomUUID())
                .patientFirstName("Sambit")
                .patientLastName("Sahu")
                .patientAadhaarNumber("456783452698")
                .build();
    }

    @Test
    public void testDeletePatientById_Success() throws Exception {
        String patientId = TestConstants.PATIENT_ID;
        UUID validPatientId = UUID.fromString(patientId);
        when(validator.validateAndConvertToUUID(anyString(), anyString())).thenReturn(validPatientId);
        when(patientRepository.existsById(validPatientId)).thenReturn(true);
        when(doctorPatientAssignmentRepository.findByPatientIdAndDateOfUnassignmentNull(validPatientId)).thenReturn(Optional.empty());
        doNothing().when(patientRepository).deleteById(validPatientId);
        when(messageUtil.getMessage(anyString(), any(Object[].class))).thenReturn(TestConstants.PATIENT_DELETED_SUCCESSFULLY);
        String testResult = patientService.deletePatientById(patientId);
        String expectedResult = TestConstants.PATIENT_DELETED_SUCCESSFULLY;
        assertEquals(expectedResult, testResult);
        verify(patientRepository, times(1)).deleteById(validPatientId);
    }

    @Test
    public void testDeletePatientById_PatientAssignedToDoctor() throws Exception {
        String patientId = TestConstants.PATIENT_ID;
        UUID validPatientId = UUID.fromString(patientId);
        when(validator.validateAndConvertToUUID(anyString(), anyString())).thenReturn(validPatientId);
        when(patientRepository.existsById(validPatientId)).thenReturn(true);
        when(messageUtil.getMessage(PatientConstants.PATIENT_DELETION_FAILED_ASSIGNED_TO_DOCTOR)).thenReturn(TestConstants.DELETION_FAILED);
        when(doctorPatientAssignmentRepository.findByPatientIdAndDateOfUnassignmentNull(validPatientId)).thenReturn(Optional.of(new DoctorPatientAssignmentModel()));
        Exception exception = assertThrows(PatientException.class, () -> {
            patientService.deletePatientById(patientId);
        });
        String expectedResult = TestConstants.DELETION_FAILED;
        assertEquals(expectedResult, exception.getMessage());
    }

    /**
     * Test successful get request based on valid id
     * Checks for the service handles valid UUID and return patient dto
     *
     * @throws Exception if anything fails
     */
    @Test
    public void getPatientById_Success() throws Exception {
        String patientId = String.valueOf(patientModel.getId());
        UUID validPatientId = UUID.fromString(patientId);
        when(validator.validateAndConvertToUUID(patientId, PatientConstants.INVALID_UUID_KEY)).thenReturn(validPatientId);
        when(patientRepository.findById(validPatientId)).thenReturn(Optional.of(patientModel));
        PatientDto patientDto = patientService.getPatientById(patientId);
        assertNotNull(patientDto);
        assertEquals(patientId, patientDto.getId().toString());
        assertEquals(patientModel.getPatientFirstName(), patientDto.getPatientFirstName());
        assertEquals(patientModel.getPatientLastName(), patientDto.getPatientLastName());
        assertEquals(patientModel.getPatientAadhaarNumber(), patientDto.getPatientAadhaarNumber());
        verify(patientRepository, Mockito.times(1)).findById(validPatientId);
    }

    /**
     * Test failure get request based on invalid id
     * Verifies for valid exception if patient not found
     *
     * @throws Exception if anything fails
     */
    @Test
    public void getPatientById_Failure() throws Exception {
        String patientId = String.valueOf(UUID.randomUUID());
        UUID validPatientId = UUID.fromString(patientId);
        when(validator.validateAndConvertToUUID(patientId, PatientConstants.INVALID_UUID_KEY)).thenReturn(validPatientId);
        when(patientRepository.findById(validPatientId)).thenReturn(Optional.empty());
        when(messageUtil.getMessage(PatientConstants.PATIENT_NOT_FOUND_KEY)).thenReturn(TestConstants.PATIENT_NOT_FOUND);
        Exception actualExceptionResponse =
                assertThrows(PatientNotFoundException.class, () -> patientService.getPatientById(patientId));
        assertEquals(TestConstants.PATIENT_NOT_FOUND, actualExceptionResponse.getMessage());
    }
}
