package com.theelixrlabs.healthcare.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theelixrlabs.healthcare.constants.ApiPathsConstant;
import com.theelixrlabs.healthcare.constants.TestConstants;
import com.theelixrlabs.healthcare.dto.PatientDto;
import com.theelixrlabs.healthcare.exceptionHandler.GlobalExceptionHandler;
import com.theelixrlabs.healthcare.exceptionHandler.PatientNotFoundException;
import com.theelixrlabs.healthcare.response.FailureResponse;
import com.theelixrlabs.healthcare.response.SuccessResponse;
import com.theelixrlabs.healthcare.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PatientControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PatientService patientService;

    private ObjectMapper objectMapper;

    private PatientDto patientDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new PatientController(patientService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
        patientDto = PatientDto.builder()
                .id(UUID.randomUUID())
                .patientFirstName("Sambit")
                .patientLastName("Sahu")
                .patientAadhaarNumber("456783452698")
                .build();
    }

    @Test
    public void deletePatientById_Success() throws Exception {
        String patientId = TestConstants.PATIENT_ID;
        when(patientService.deletePatientById(patientId)).thenReturn(TestConstants.PATIENT_DELETED_SUCCESSFULLY);
        String expectedResult = TestConstants.PATIENT_DELETED_SUCCESSFULLY;
        mockMvc.perform(delete(TestConstants.PATIENT_BY_ID, patientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath(TestConstants.SUCCESS_EXPRESSION).value(true))
                .andExpect(jsonPath(TestConstants.RESPONSE_MESSAGE_EXPRESSION).value(expectedResult));
    }

    @Test
    public void deletePatientById_Failure() throws Exception {
        String patientId = TestConstants.PATIENT_ID;
        when(patientService.deletePatientById(patientId)).thenThrow(new PatientNotFoundException(TestConstants.PATIENT_NOT_FOUND));
        String expectedResult = TestConstants.PATIENT_NOT_FOUND;
        mockMvc.perform(delete(TestConstants.PATIENT_BY_ID, patientId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(TestConstants.SUCCESS_EXPRESSION).value(false))
                .andExpect(jsonPath(TestConstants.ERRORS_ARRAY_EXPRESSION).isArray())
                .andExpect(jsonPath(TestConstants.TEST_RESULT_ARRAY_EXPRESSION).value(expectedResult));

    }

    /**
     * It tests a successful get request based on the Valid UUID
     * Mocks the service to return a PatientDto
     *
     * @throws Exception if assertion or request fails
     */
    @Test
    public void getPatientById_Success() throws Exception {
        String patientId = String.valueOf(patientDto.getId());
        when(patientService.getPatientById(patientId)).thenReturn(patientDto);
        ResultActions actualResponse = mockMvc.perform(get(ApiPathsConstant.PATIENT_BY_ID_ENDPOINT, patientId)
                .contentType(MediaType.APPLICATION_JSON));
        actualResponse.andExpect(status().isOk());
        String actualResponseData = actualResponse.andReturn().getResponse().getContentAsString();
        SuccessResponse<PatientDto> successResponse = new SuccessResponse<>(true, patientDto, null);
        String expectedResponseData = objectMapper.writeValueAsString(successResponse);
        assertEquals(expectedResponseData, actualResponseData);
    }

    /**
     * It tests the handling of exception based on the Invalid UUID
     * Mocks the service to return an exception
     *
     * @throws Exception if assertion or request fails
     */
    @Test
    public void getPatientById_Failure() throws Exception {
        String patientId = String.valueOf(UUID.randomUUID());
        when(patientService.getPatientById(patientId)).thenThrow(new PatientNotFoundException(TestConstants.PATIENT_NOT_FOUND));
        ResultActions response = mockMvc.perform(get(ApiPathsConstant.PATIENT_BY_ID_ENDPOINT, patientId)
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(status().isNotFound());
        String actualResponseData = response.andReturn().getResponse().getContentAsString();
        FailureResponse failureResponse = new FailureResponse(false, Arrays.asList(TestConstants.PATIENT_NOT_FOUND));
        String expectedResponseData = objectMapper.writeValueAsString(failureResponse);
        assertEquals(expectedResponseData, actualResponseData);
    }
}
