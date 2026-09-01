package com.theelixrlabs.healthcare.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theelixrlabs.healthcare.constants.TestConstants;
import com.theelixrlabs.healthcare.dto.DoctorDto;
import com.theelixrlabs.healthcare.exceptionHandler.DoctorException;
import com.theelixrlabs.healthcare.response.FailureResponse;
import com.theelixrlabs.healthcare.response.SuccessResponse;
import com.theelixrlabs.healthcare.service.PatchDoctorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Class contains unit tests for the PatchDoctorController in the application.
 * uses MockMvc to simulate HTTP requests and verify the behavior of the controller methods.
 * The tests cover scenarios - successful patches and handling invalid UUIDs.
 */
@WebMvcTest(controllers = PatchDoctorController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PatchDoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatchDoctorService patchDoctorService;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID uuid;
    private String doctorId;
    private DoctorDto doctorDto;

    /**
     * Initializes common test data and mocks before each test.
     *
     * @throws Exception if setup fails
     */
    @BeforeEach
    void setUp() throws Exception {
        uuid = UUID.fromString("44fdebfc-7f49-455f-b98b-7d5eef4fe4fb");
        doctorId = uuid.toString();
        doctorDto = DoctorDto.builder()
                .id(uuid)
                .firstName("Shahana")
                .lastName("Sha")
                .department("Psychology")
                .aadhaarNumber("956788563467")
                .build();
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Tests a successful PATCH request to update a doctor's details.
     * Mocks the service to return a DoctorDto object and verifies the response.
     *
     * @throws Exception if assertion or request fails
     */
    @Test
    public void patchDoctorById_Successful_Patch_Test() throws Exception {
        when(patchDoctorService.patchDoctorById(doctorId, doctorDto)).thenReturn(doctorDto);
        ResultActions actualResponse = mockMvc.perform(MockMvcRequestBuilders.patch(TestConstants.DOCTOR_ENDPOINT + doctorId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(doctorDto)));
        actualResponse.andExpect(status().isOk());
        String actualResponseData = actualResponse.andReturn().getResponse().getContentAsString();
        SuccessResponse<DoctorDto> expectedSuccessResponse = new SuccessResponse<>(true, doctorDto, null);
        String expectedResponseData = objectMapper.writeValueAsString(expectedSuccessResponse);
        assertEquals(expectedResponseData, actualResponseData);
    }

    /**
     * Tests the handling of an invalid UUID in a PATCH request.
     * Mock the service to throw an exception and verifies the response.
     *
     * @throws Exception if assertion or request fails
     */
    @Test
    public void patchDoctorById_Invalid_UUID_Test() throws Exception {
        String invalidUUId = "6890q372-780h-diu8-3";
        when(patchDoctorService.patchDoctorById(eq(invalidUUId), any())).thenThrow(new DoctorException(TestConstants.INVALID_UUID));
        ResultActions actualResponse = mockMvc.perform(MockMvcRequestBuilders.patch(TestConstants.DOCTOR_ENDPOINT + invalidUUId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new DoctorDto())))
                .andExpect(status().isBadRequest());
        String actualResponseData = actualResponse.andReturn().getResponse().getContentAsString();
        FailureResponse failureResponse = new FailureResponse(false, List.of(TestConstants.INVALID_UUID));
        String expectedResponseData = objectMapper.writeValueAsString(failureResponse);
        assertEquals(expectedResponseData, actualResponseData);
    }
}
