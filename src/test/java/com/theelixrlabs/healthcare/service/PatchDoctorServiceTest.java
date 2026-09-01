package com.theelixrlabs.healthcare.service;

import com.theelixrlabs.healthcare.constants.MessageConstants;
import com.theelixrlabs.healthcare.constants.TestConstants;
import com.theelixrlabs.healthcare.dto.DoctorDto;
import com.theelixrlabs.healthcare.exceptionHandler.DataException;
import com.theelixrlabs.healthcare.exceptionHandler.DoctorNotFoundException;
import com.theelixrlabs.healthcare.model.DoctorModel;
import com.theelixrlabs.healthcare.repository.DoctorRepository;
import com.theelixrlabs.healthcare.utility.MessageUtil;
import com.theelixrlabs.healthcare.utility.PatchUtil;
import com.theelixrlabs.healthcare.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Class contains unit tests for the PatchDoctorService in the application.
 * Validates successful updates and handling of non-existent records.
 */
public class PatchDoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private Validator validator;

    @Mock
    private MessageUtil messageUtil;

    @Mock
    private PatchUtil patchUtil;

    @InjectMocks
    private PatchDoctorService patchDoctorService;

    private UUID uuid;
    private String doctorId;
    private DoctorDto doctorDto;
    private DoctorModel doctorModel;

    /**
     * Initializes common test data and mocks before each test.
     *
     * @throws Exception if setup fails
     */
    @BeforeEach
    void setUp() throws Exception {
        uuid = UUID.randomUUID();
        doctorId = uuid.toString();
        doctorModel = DoctorModel.builder()
                .id(uuid)
                .firstName("Shahana")
                .lastName("S")
                .department("Psychology")
                .aadhaarNumber("956788563467")
                .build();
        doctorDto = DoctorDto.builder()
                .id(uuid)
                .firstName("Shahana")
                .lastName("S")
                .department("Psychology")
                .aadhaarNumber("956788563467")
                .build();
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Tests the successful patching of a doctor's details.
     * Verifies that the service correctly updates and returns the doctorDto.
     *
     * @throws Exception if the patch operation fails
     */
    @Test
    public void patchDoctorById_Successful_Patch_Test() throws Exception {
        when(validator.validateAndConvertToUUID(doctorId, MessageConstants.INVALID_UUID)).thenReturn(uuid);
        when(doctorRepository.findById(uuid)).thenReturn(Optional.ofNullable(doctorModel));
        when(doctorRepository.save(Mockito.any(DoctorModel.class))).thenReturn(doctorModel);
        DoctorDto patchedDoctor = patchDoctorService.patchDoctorById(doctorId, doctorDto);
        assertEquals(doctorDto, patchedDoctor);
    }

    /**
     * Tests the negative scenario where the doctor to be patched is not found.
     * Verifies that the service throws DoctorNotFoundException.
     *
     * @throws DataException if the exception is not thrown as expected.
     */
    @Test
    public void patchDoctorById_Doctor_Not_Found_Test() throws Exception {
        UUID uuid = UUID.fromString("44fdebfc-7f49-455f-b98b-7d5eef4fe4fb");
        String doctorId = uuid.toString();
        when(validator.validateAndConvertToUUID(doctorId, MessageConstants.INVALID_UUID))
                .thenReturn(uuid);
        when(doctorRepository.findById(uuid)).thenReturn(Optional.empty());
        when(messageUtil.getMessage(MessageConstants.DOCTOR_ID_NOT_FOUND)).thenReturn(TestConstants.DOCTOR_NOT_FOUND_MESSAGE);
        DoctorNotFoundException doctorNotFoundException = assertThrows(DoctorNotFoundException.class, () -> {
            patchDoctorService.patchDoctorById(doctorId, doctorDto);
        });
        assertEquals(TestConstants.DOCTOR_NOT_FOUND_MESSAGE, doctorNotFoundException.getMessage());
        verify(doctorRepository, times(1)).findById(uuid);
    }
}
