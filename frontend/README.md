# HealthCare POC - Frontend

A React + TypeScript + Vite frontend for the HealthCare POC REST API.

## Features

- 🔐 Google OAuth2 login (matching the Spring Boot backend)
- 👨‍⚕️ **Doctors** — Create, search by name, search by ID, edit (PATCH), delete
- 🩺 **Patients** — Create, search by name, search by ID, edit (PATCH), delete
- 🔗 **Assignments** — Assign/unassign doctor ↔ patient, view all patients for a doctor, view all doctors for a patient

## Setup

1. **Install dependencies:**
   ```bash
   cd frontend
   npm install
   ```

2. **Run backend in dev profile (recommended for local development):**
   ```bash
   ./gradlew bootRun --args='--spring.profiles.active=dev'
   ```
   In `dev` profile, backend auth is disabled, so frontend login is not required.

3. **Run the dev server:**
   ```bash
   npm run dev
   ```
   The app runs on `http://localhost:3000` and proxies API calls to `http://localhost:8080`.

4. **Build for production:**
   ```bash
   npm run build
   ```

## Optional: Google OAuth mode

If you switch backend to a secured profile, configure Google OAuth in frontend:

```bash
cp .env.example .env.local
# set VITE_GOOGLE_CLIENT_ID to match backend client-id
```

## API Proxy

The Vite dev server proxies all `/api/*` requests to `http://localhost:8080`, stripping the `/api` prefix. No CORS issues during development.

## Backend Endpoints Covered

| Method | Path | Description |
|--------|------|-------------|
| POST | /addDoctor | Create doctor |
| GET | /doctor/{id} | Get doctor by ID |
| DELETE | /doctor/{id} | Delete doctor |
| PATCH | /doctor/{id} | Update doctor fields |
| GET | /doctorByName?doctorName= | Search doctors by name |
| POST | /addPatient | Create patient |
| GET | /patient/{id} | Get patient by ID |
| DELETE | /patient/{id} | Delete patient |
| PATCH | /patient/{id} | Update patient fields |
| GET | /patientByName?patientName= | Search patients by name |
| POST | /assignDoctorToPatient | Assign doctor to patient |
| POST | /unassignDoctorFromPatient | Unassign doctor from patient |
| GET | /patientsByDoctorId?doctorId= | Get patients for a doctor |
| GET | /assignedDoctors?patientId= | Get doctors for a patient |
