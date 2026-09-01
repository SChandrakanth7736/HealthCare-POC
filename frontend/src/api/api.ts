import type {
  DoctorDto,
  DoctorPatientAssignmentDto,
  DoctorWithAssignedPatientsDto,
  PatientDto,
  PatientWithAssignedDoctorsDto,
  SuccessResponse,
} from '../types';

const BASE_URL = '/api';

function getToken(): string | null {
  return localStorage.getItem('healthcare_token');
}

async function apiFetch<T>(path: string, options: RequestInit = {}): Promise<T> {
  const token = getToken();
  const headers: Record<string, string> = {
    'Content-Type': 'application/json',
    ...(options.headers as Record<string, string>),
  };
  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  const res = await fetch(`${BASE_URL}${path}`, { ...options, headers });

  if (!res.ok) {
    let errorMsg = `HTTP ${res.status}`;
    try {
      const body = await res.json();
      errorMsg = body?.responseMessage || body?.message || body?.error || body?.errors?.[0] || errorMsg;
    } catch {
      // ignore parse error
    }
    throw new Error(errorMsg);
  }

  return res.json() as Promise<T>;
}

function getResponseData<T>(res: SuccessResponse<T>): T {
  const data = res.responseData ?? res.data;
  if (data === undefined || data === null) {
    throw new Error('API response did not include data');
  }
  return data;
}

function getResponseMessage<T>(res: SuccessResponse<T>, fallback: string): string {
  return res.responseMessage ?? res.message ?? fallback;
}

// ─── Doctor API ──────────────────────────────────────────────────────────────

export async function createDoctor(dto: Omit<DoctorDto, 'id' | 'dateOfAssignment'>): Promise<DoctorDto> {
  const res = await apiFetch<SuccessResponse<DoctorDto>>('/addDoctor', {
    method: 'POST',
    body: JSON.stringify(dto),
  });
  return getResponseData(res);
}

export async function getDoctorById(doctorId: string): Promise<DoctorDto> {
  const res = await apiFetch<SuccessResponse<DoctorDto>>(`/doctor/${doctorId}`);
  return getResponseData(res);
}

export async function deleteDoctorById(doctorId: string): Promise<string> {
  const res = await apiFetch<SuccessResponse<string>>(`/doctor/${doctorId}`, { method: 'DELETE' });
  return getResponseMessage(res, 'Deleted successfully');
}

export async function getDoctorsByName(name: string): Promise<DoctorDto[]> {
  const res = await apiFetch<SuccessResponse<DoctorDto[]>>(
    `/doctorByName?name=${encodeURIComponent(name)}`
  );
  return res.responseData ?? res.data ?? [];
}

export async function patchDoctor(doctorId: string, dto: Partial<DoctorDto>): Promise<DoctorDto> {
  const res = await apiFetch<SuccessResponse<DoctorDto>>(`/doctor/${doctorId}`, {
    method: 'PATCH',
    body: JSON.stringify(dto),
  });
  return getResponseData(res);
}

// ─── Patient API ─────────────────────────────────────────────────────────────

export async function createPatient(dto: Omit<PatientDto, 'id' | 'dateOfAdmission'>): Promise<PatientDto> {
  const res = await apiFetch<SuccessResponse<PatientDto>>('/addPatient', {
    method: 'POST',
    body: JSON.stringify(dto),
  });
  return getResponseData(res);
}

export async function getPatientById(patientId: string): Promise<PatientDto> {
  const res = await apiFetch<SuccessResponse<PatientDto>>(`/patient/${patientId}`);
  return getResponseData(res);
}

export async function deletePatientById(patientId: string): Promise<string> {
  const res = await apiFetch<SuccessResponse<string>>(`/patient/${patientId}`, { method: 'DELETE' });
  return getResponseMessage(res, 'Deleted successfully');
}

export async function getPatientsByName(name: string): Promise<PatientDto[]> {
  const res = await apiFetch<SuccessResponse<PatientDto[]>>(
    `/patientByName?name=${encodeURIComponent(name)}`
  );
  return res.responseData ?? res.data ?? [];
}

export async function patchPatient(patientId: string, dto: Partial<PatientDto>): Promise<PatientDto> {
  const res = await apiFetch<SuccessResponse<PatientDto>>(`/patient/${patientId}`, {
    method: 'PATCH',
    body: JSON.stringify(dto),
  });
  return getResponseData(res);
}

// ─── Assignment API ───────────────────────────────────────────────────────────

export async function assignDoctorToPatient(dto: DoctorPatientAssignmentDto): Promise<DoctorPatientAssignmentDto> {
  const res = await apiFetch<SuccessResponse<DoctorPatientAssignmentDto>>('/assignDoctorToPatient', {
    method: 'POST',
    body: JSON.stringify(dto),
  });
  return getResponseData(res);
}

export async function unassignDoctorFromPatient(dto: DoctorPatientAssignmentDto): Promise<void> {
  await apiFetch<SuccessResponse<null>>('/unassignDoctorFromPatient', {
    method: 'POST',
    body: JSON.stringify(dto),
  });
}

export async function getPatientsByDoctorId(doctorId: string): Promise<DoctorWithAssignedPatientsDto> {
  const res = await apiFetch<SuccessResponse<DoctorWithAssignedPatientsDto>>(
    `/patientsByDoctorId?doctorId=${encodeURIComponent(doctorId)}`
  );
  return getResponseData(res);
}

export async function getAssignedDoctorsByPatientId(patientId: string): Promise<PatientWithAssignedDoctorsDto> {
  const res = await apiFetch<SuccessResponse<PatientWithAssignedDoctorsDto>>(
    `/assignedDoctors?patientId=${encodeURIComponent(patientId)}`
  );
  return getResponseData(res);
}
