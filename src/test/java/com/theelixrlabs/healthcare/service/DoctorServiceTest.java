package com.theelixrlabs.healthcare.service;

import com.theelixrlabs.healthcare.constants.MessageConstants;
import com.theelixrlabs.healthcare.constants.TestConstants;
import com.theelixrlabs.healthcare.dto.DoctorDto;
import com.theelixrlabs.healthcare.exceptionHandler.DoctorNotFoundException;
import com.theelixrlabs.healthcare.model.DoctorModel;
import com.theelixrlabs.healthcare.repository.DoctorRepository;
import com.theelixrlabs.healthcare.utility.MessageUtil;
import com.theelixrlabs.healthcare.validation.Validator;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the DoctorService class.
 * This class contains test cases to verify the behavior of the methods in the DoctorService class.
 */
public class DoctorServiceTest {
    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private Validator validator;
    @Mock
    private MessageUtil messageUtil;
    @InjectMocks
    private DoctorService doctorService;
    private List<DoctorModel> doctorModelList;

    /**
     * Initializes the test data and mocks before each test is run.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        doctorModelList = new ArrayList<>();
        // Adding first doctor model
        DoctorModel doctorModel1 = DoctorModel.builder()
                .id(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .department("Cardiology")
                .aadhaarNumber("2365 5896 4521")
                .build();
        doctorModelList.add(doctorModel1);
        // Adding second doctor model
        DoctorModel doctorModel2 = DoctorModel.builder()
                .id(UUID.randomUUID())
                .firstName("Johnson")
                .lastName("Chris")
                .department("Java")
                .aadhaarNumber("5678 8585 6565")
                .build();
        doctorModelList.add(doctorModel2);
    }

    /**
     * This test verifies that the service correctly retrieves and maps doctor models to DTOs when there are matches in the repository.
     *
     * @throws Exception if any error occurs during the test
     */
    @Test
    public void getDoctorsByName_Success() throws Exception {
        String doctorName = doctorModelList.get(0).getFirstName();
        when(doctorRepository.searchByDoctorName(doctorName)).thenReturn(doctorModelList);
        List<DoctorDto> result = doctorService.getDoctorsByName(doctorName);
        assertNotNull(result);
        assertEquals(2, result.size());
        // Checking the first doctor
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Doe", result.get(0).getLastName());
        assertEquals("Cardiology", result.get(0).getDepartment());
        assertEquals("2365 5896 4521", result.get(0).getAadhaarNumber());
        // Checking the second doctor
        assertEquals("Johnson", result.get(1).getFirstName());
        assertEquals("Chris", result.get(1).getLastName());
        assertEquals("Java", result.get(1).getDepartment());
        assertEquals("5678 8585 6565", result.get(1).getAadhaarNumber());
    }

    /**
     * This test verifies that the service throws a DoctorNotFoundException with the correct message when no matching doctors are found in the repository.
     *
     * @throws Exception if any error occurs during the test
     */
    @Test
    public void getDoctorsByName_ReturnsNotFoundException() throws Exception {
        String doctorName = TestConstants.DOCTOR_NAME;
        when(messageUtil.getMessage(MessageConstants.DOCTOR_NAME_NOT_FOUND)).thenReturn(TestConstants.DOCTOR_NAME_NOT_FOUND);
        when(doctorRepository.searchByDoctorName(doctorName)).thenReturn(new ArrayList<>());
        try {
            doctorService.getDoctorsByName(doctorName);
            Assertions.fail("Expected DoctorNotFoundException was not thrown.");
        } catch (DoctorNotFoundException doctorNotFoundException) {
            assertEquals(TestConstants.DOCTOR_NAME_NOT_FOUND, doctorNotFoundException.getMessage());
        }
    }
}
