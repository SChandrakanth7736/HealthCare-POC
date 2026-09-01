export interface DoctorDto {
  id?: string;
  firstName: string;
  lastName: string;
  department: string;
  aadhaarNumber: string;
  dateOfAssignment?: string;
}

export interface PatientDto {
  id?: string;
  patientFirstName: string;
  patientLastName: string;
  patientAadhaarNumber: string;
  dateOfAdmission?: string;
}

export interface DoctorPatientAssignmentDto {
  doctorId: string;
  patientId: string;
}

export interface DoctorWithAssignedPatientsDto {
  id?: string;
  firstName?: string;
  lastName?: string;
  department?: string;
  aadhaarNumber?: string;
  patients?: PatientDto[];
}

export interface PatientWithAssignedDoctorsDto {
  id?: string;
  patientFirstName?: string;
  patientLastName?: string;
  patientAadhaarNumber?: string;
  doctors?: DoctorDto[];
}

export interface SuccessResponse<T> {
  success: boolean;
  data?: T;
  message?: string | null;
  responseData?: T;
  responseMessage?: string | null;
}
