import { useState } from 'react'
import toast from 'react-hot-toast'
import type { DoctorWithAssignedPatientsDto, PatientWithAssignedDoctorsDto } from '../types'
import {
  assignDoctorToPatient,
  getAssignedDoctorsByPatientId,
  getPatientsByDoctorId,
  unassignDoctorFromPatient,
} from '../api/api'

export default function AssignmentsPage() {
  const [assignDoctorId, setAssignDoctorId] = useState('')
  const [assignPatientId, setAssignPatientId] = useState('')
  const [loading, setLoading] = useState(false)

  const [lookupDoctorId, setLookupDoctorId] = useState('')
  const [lookupPatientId, setLookupPatientId] = useState('')
  const [doctorResult, setDoctorResult] = useState<DoctorWithAssignedPatientsDto | null>(null)
  const [patientResult, setPatientResult] = useState<PatientWithAssignedDoctorsDto | null>(null)

  const handleAssign = async () => {
    if (!assignDoctorId.trim() || !assignPatientId.trim()) {
      return toast.error('Both Doctor ID and Patient ID are required')
    }
    setLoading(true)
    try {
      await assignDoctorToPatient({ doctorId: assignDoctorId.trim(), patientId: assignPatientId.trim() })
      toast.success('Doctor assigned to patient!')
      setAssignDoctorId('')
      setAssignPatientId('')
    } catch (e: unknown) {
      toast.error(e instanceof Error ? e.message : 'Assignment failed')
    } finally {
      setLoading(false)
    }
  }

  const handleUnassign = async () => {
    if (!assignDoctorId.trim() || !assignPatientId.trim()) {
      return toast.error('Both Doctor ID and Patient ID are required')
    }
    setLoading(true)
    try {
      await unassignDoctorFromPatient({ doctorId: assignDoctorId.trim(), patientId: assignPatientId.trim() })
      toast.success('Doctor unassigned from patient!')
      setAssignDoctorId('')
      setAssignPatientId('')
    } catch (e: unknown) {
      toast.error(e instanceof Error ? e.message : 'Unassignment failed')
    } finally {
      setLoading(false)
    }
  }

  const handleGetPatientsByDoctor = async () => {
    if (!lookupDoctorId.trim()) return toast.error('Enter a Doctor ID')
    setLoading(true)
    try {
      const data = await getPatientsByDoctorId(lookupDoctorId.trim())
      setDoctorResult(data)
      setPatientResult(null)
    } catch (e: unknown) {
      toast.error(e instanceof Error ? e.message : 'Lookup failed')
    } finally {
      setLoading(false)
    }
  }

  const handleGetDoctorsByPatient = async () => {
    if (!lookupPatientId.trim()) return toast.error('Enter a Patient ID')
    setLoading(true)
    try {
      const data = await getAssignedDoctorsByPatientId(lookupPatientId.trim())
      setPatientResult(data)
      setDoctorResult(null)
    } catch (e: unknown) {
      toast.error(e instanceof Error ? e.message : 'Lookup failed')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="max-w-7xl mx-auto px-4 py-8 space-y-8">
      <h1 className="text-2xl font-bold text-gray-800">🔗 Assignments</h1>

      {/* Assign / Unassign panel */}
      <div className="card p-6">
        <h2 className="text-base font-semibold text-gray-700 mb-4">Assign / Unassign Doctor ↔ Patient</h2>
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 mb-4">
          <div>
            <label className="block text-xs font-medium text-gray-600 mb-1">Doctor ID (UUID)</label>
            <input
              className="input"
              placeholder="e.g. 550e8400-e29b-41d4-a716-…"
              value={assignDoctorId}
              onChange={(e) => setAssignDoctorId(e.target.value)}
            />
          </div>
          <div>
            <label className="block text-xs font-medium text-gray-600 mb-1">Patient ID (UUID)</label>
            <input
              className="input"
              placeholder="e.g. 550e8400-e29b-41d4-a716-…"
              value={assignPatientId}
              onChange={(e) => setAssignPatientId(e.target.value)}
            />
          </div>
        </div>
        <div className="flex gap-3">
          <button className="btn-success" onClick={handleAssign} disabled={loading}>
            ✅ Assign
          </button>
          <button className="btn-danger" onClick={handleUnassign} disabled={loading}>
            ❌ Unassign
          </button>
        </div>
      </div>

      {/* Lookup panels */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Patients by Doctor */}
        <div className="card p-6">
          <h2 className="text-base font-semibold text-gray-700 mb-4">Patients assigned to a Doctor</h2>
          <div className="flex gap-2 mb-4">
            <input
              className="input flex-1"
              placeholder="Doctor ID (UUID)"
              value={lookupDoctorId}
              onChange={(e) => setLookupDoctorId(e.target.value)}
              onKeyDown={(e) => e.key === 'Enter' && handleGetPatientsByDoctor()}
            />
            <button className="btn-primary" onClick={handleGetPatientsByDoctor} disabled={loading}>
              Look up
            </button>
          </div>

          {doctorResult && (
            <div>
              <div className="mb-3 p-3 bg-blue-50 rounded-lg">
                <p className="font-medium text-blue-800">
                  Dr. {doctorResult.firstName} {doctorResult.lastName}
                </p>
                <p className="text-xs text-blue-600">{doctorResult.department}</p>
              </div>
              {doctorResult.patients && doctorResult.patients.length > 0 ? (
                <ul className="divide-y divide-gray-100">
                  {doctorResult.patients.map((p) => (
                    <li key={p.id} className="py-2 flex justify-between items-center">
                      <span className="font-medium text-sm">
                        {p.patientFirstName} {p.patientLastName}
                      </span>
                      <span className="badge bg-gray-100 text-gray-600 font-mono">{p.patientAadhaarNumber}</span>
                    </li>
                  ))}
                </ul>
              ) : (
                <p className="text-sm text-gray-400 italic">No patients assigned</p>
              )}
            </div>
          )}
        </div>

        {/* Doctors by Patient */}
        <div className="card p-6">
          <h2 className="text-base font-semibold text-gray-700 mb-4">Doctors assigned to a Patient</h2>
          <div className="flex gap-2 mb-4">
            <input
              className="input flex-1"
              placeholder="Patient ID (UUID)"
              value={lookupPatientId}
              onChange={(e) => setLookupPatientId(e.target.value)}
              onKeyDown={(e) => e.key === 'Enter' && handleGetDoctorsByPatient()}
            />
            <button className="btn-primary" onClick={handleGetDoctorsByPatient} disabled={loading}>
              Look up
            </button>
          </div>

          {patientResult && (
            <div>
              <div className="mb-3 p-3 bg-green-50 rounded-lg">
                <p className="font-medium text-green-800">
                  {patientResult.patientFirstName} {patientResult.patientLastName}
                </p>
                <p className="text-xs text-green-600 font-mono">{patientResult.patientAadhaarNumber}</p>
              </div>
              {patientResult.doctors && patientResult.doctors.length > 0 ? (
                <ul className="divide-y divide-gray-100">
                  {patientResult.doctors.map((d) => (
                    <li key={d.id} className="py-2 flex justify-between items-center">
                      <span className="font-medium text-sm">
                        Dr. {d.firstName} {d.lastName}
                      </span>
                      <span className="badge bg-blue-100 text-blue-700">{d.department}</span>
                    </li>
                  ))}
                </ul>
              ) : (
                <p className="text-sm text-gray-400 italic">No doctors assigned</p>
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  )
}
