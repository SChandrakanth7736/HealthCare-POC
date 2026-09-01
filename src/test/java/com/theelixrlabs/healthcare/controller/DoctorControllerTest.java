package com.theelixrlabs.healthcare.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theelixrlabs.healthcare.constants.ApiPathsConstant;
import com.theelixrlabs.healthcare.constants.DoctorConstants;
import com.theelixrlabs.healthcare.constants.TestConstants;
import com.theelixrlabs.healthcare.dto.DoctorDto;
import com.theelixrlabs.healthcare.exceptionHandler.DoctorNotFoundException;
import com.theelixrlabs.healthcare.exceptionHandler.GlobalExceptionHandler;
import com.theelixrlabs.healthcare.response.FailureResponse;
import com.theelixrlabs.healthcare.response.SuccessResponse;
import com.theelixrlabs.healthcare.service.DoctorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Contains test methods for Doctor Controller.
 * Uses MockMvc to simulate HTTP requests and verify the behavior of each method.
 */
public class DoctorControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private DoctorService doctorService;

    private List<DoctorDto> doctorDtoList;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new DoctorController(doctorService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
        doctorDtoList = new ArrayList<>();
        doctorDtoList.add(DoctorDto.builder()
                .id(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .department("Cardiology")
                .aadhaarNumber("8834 5678 9101")
                .build());
        doctorDtoList.add(DoctorDto.builder()
                .id(UUID.randomUUID())
                .firstName("Johnson")
                .lastName("Chris")
                .department("Orthopedics")
                .aadhaarNumber("9876 5432 1098")
                .build());
    }

    /**
     * This test verifies that the controller correctly retrieves and maps doctor DTOs when there are matches in the service.
     *
     * @throws Exception if any error occurs during the test
     */
    @Test
    public void getDoctorsByName_Success() throws Exception {
        String doctorName = TestConstants.DOCTOR_NAME;
        when(doctorService.getDoctorsByName(doctorName)).thenReturn(doctorDtoList);
        ResultActions actualResponse = mockMvc.perform(get(ApiPathsConstant.DOCTORS_BY_NAME_ENDPOINT)
                .param(DoctorConstants.DOCTOR_NAME_PARAM, doctorName)
                .contentType(MediaType.APPLICATION_JSON));
        actualResponse.andExpect(status().isOk());
        String actualResponseData = actualResponse.andReturn().getResponse().getContentAsString();
        SuccessResponse<List<DoctorDto>> expectedSuccessResponse = new SuccessResponse<>(true, doctorDtoList, null);
        String expectedResponseData = objectMapper.writeValueAsString(expectedSuccessResponse);
        assertEquals(expectedResponseData, actualResponseData);
    }

    /**
     * This test verifies that the controller throws a DoctorNotFoundException with the correct message when no matching doctors are found in the service.
     *
     * @throws Exception if any error occurs during the test
     */
    @Test
    public void getDoctorsByName_ReturnsNotFoundException() throws Exception {
        String doctorName = TestConstants.DOCTOR_NAME;
        when(doctorService.getDoctorsByName(doctorName)).thenThrow(new DoctorNotFoundException(TestConstants.DOCTOR_NAME_NOT_FOUND));
        ResultActions response = mockMvc.perform(get(ApiPathsConstant.DOCTORS_BY_NAME_ENDPOINT)
                .param(DoctorConstants.DOCTOR_NAME_PARAM, doctorName)
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(status().isNotFound());
        String actualResponseData = response.andReturn().getResponse().getContentAsString();
        FailureResponse failureResponse = new FailureResponse(false, List.of(TestConstants.DOCTOR_NAME_NOT_FOUND));
        String expectedResponseData = objectMapper.writeValueAsString(failureResponse);
        assertEquals(expectedResponseData, actualResponseData);
    }
}
