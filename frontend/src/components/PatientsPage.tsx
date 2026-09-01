import { useState, useCallback } from 'react'
import toast from 'react-hot-toast'
import type { PatientDto } from '../types'
import {
  createPatient,
  deletePatientById,
  getPatientById,
  getPatientsByName,
  patchPatient,
} from '../api/api'
import Modal from './Modal'

const emptyForm = (): Omit<PatientDto, 'id' | 'dateOfAdmission'> => ({
  patientFirstName: '',
  patientLastName: '',
  patientAadhaarNumber: '',
})

export default function PatientsPage() {
  const [patients, setPatients] = useState<PatientDto[]>([])
  const [searchName, setSearchName] = useState('')
  const [searchId, setSearchId] = useState('')
  const [loading, setLoading] = useState(false)

  const [addOpen, setAddOpen] = useState(false)
  const [editOpen, setEditOpen] = useState(false)
  const [editTarget, setEditTarget] = useState<PatientDto | null>(null)
  const [form, setForm] = useState(emptyForm())
  const [editForm, setEditForm] = useState<Partial<PatientDto>>({})

  const handleSearchByName = useCallback(async () => {
    if (!searchName.trim()) return toast.error('Enter a name to search')
    setLoading(true)
    try {
      const data = await getPatientsByName(searchName.trim())
      setPatients(data)
      if (data.length === 0) toast('No patients found', { icon: 'ℹ️' })
    } catch (e: unknown) {
      toast.error(e instanceof Error ? e.message : 'Search failed')
    } finally {
      setLoading(false)
    }
  }, [searchName])

  const handleSearchById = useCallback(async () => {
    if (!searchId.trim()) return toast.error('Enter a patient ID')
    setLoading(true)
    try {
      const data = await getPatientById(searchId.trim())
      setPatients([data])
    } catch (e: unknown) {
      toast.error(e instanceof Error ? e.message : 'Not found')
      setPatients([])
    } finally {
      setLoading(false)
    }
  }, [searchId])

  const handleAdd = async () => {
    if (!form.patientFirstName || !form.patientLastName || !form.patientAadhaarNumber) {
      return toast.error('All fields are required')
    }
    setLoading(true)
    try {
      const created = await createPatient(form)
      toast.success(`Patient ${created.patientFirstName} ${created.patientLastName} added!`)
      setAddOpen(false)
      setForm(emptyForm())
      setPatients((prev) => [created, ...prev])
    } catch (e: unknown) {
      toast.error(e instanceof Error ? e.message : 'Failed to add patient')
    } finally {
      setLoading(false)
    }
  }

  const handleDelete = async (patientId: string) => {
    if (!confirm('Delete this patient?')) return
    setLoading(true)
    try {
      await deletePatientById(patientId)
      toast.success('Patient deleted')
      setPatients((prev) => prev.filter((p) => p.id !== patientId))
    } catch (e: unknown) {
      toast.error(e instanceof Error ? e.message : 'Delete failed')
    } finally {
      setLoading(false)
    }
  }

  const openEdit = (patient: PatientDto) => {
    setEditTarget(patient)
    setEditForm({
      patientFirstName: patient.patientFirstName,
      patientLastName: patient.patientLastName,
      patientAadhaarNumber: patient.patientAadhaarNumber,
    })
    setEditOpen(true)
  }

  const handleEdit = async () => {
    if (!editTarget?.id) return
    setLoading(true)
    try {
      const updated = await patchPatient(editTarget.id, editForm)
      toast.success('Patient updated!')
      setPatients((prev) => prev.map((p) => (p.id === updated.id ? updated : p)))
      setEditOpen(false)
    } catch (e: unknown) {
      toast.error(e instanceof Error ? e.message : 'Update failed')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="max-w-7xl mx-auto px-4 py-8">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 mb-6">
        <h1 className="text-2xl font-bold text-gray-800 flex items-center gap-2">
          🩺 Patients
        </h1>
        <button className="btn-primary" onClick={() => { setForm(emptyForm()); setAddOpen(true) }}>
          + Add Patient
        </button>
      </div>

      {/* Search section */}
      <div className="card p-4 mb-6 flex flex-col sm:flex-row gap-3">
        <div className="flex flex-1 gap-2">
          <input
            className="input flex-1"
            placeholder="Search by name…"
            value={searchName}
            onChange={(e) => setSearchName(e.target.value)}
            onKeyDown={(e) => e.key === 'Enter' && handleSearchByName()}
          />
          <button className="btn-primary" onClick={handleSearchByName} disabled={loading}>
            Search
          </button>
        </div>
        <div className="flex flex-1 gap-2">
          <input
            className="input flex-1"
            placeholder="Search by ID…"
            value={searchId}
            onChange={(e) => setSearchId(e.target.value)}
            onKeyDown={(e) => e.key === 'Enter' && handleSearchById()}
          />
          <button className="btn-primary" onClick={handleSearchById} disabled={loading}>
            Find
          </button>
        </div>
        {patients.length > 0 && (
          <button className="btn-secondary" onClick={() => setPatients([])}>
            Clear
          </button>
        )}
      </div>

      {/* Patients table */}
      {patients.length === 0 ? (
        <div className="card p-12 text-center text-gray-400">
          <div className="text-5xl mb-3">🩺</div>
          <p>Search for patients by name or ID, or add a new one.</p>
        </div>
      ) : (
        <div className="card overflow-hidden">
          <table className="w-full text-sm">
            <thead className="bg-gray-50 border-b border-gray-200">
              <tr>
                {['First Name', 'Last Name', 'Aadhaar', 'Date of Admission', 'ID', 'Actions'].map((h) => (
                  <th key={h} className="px-4 py-3 text-left font-medium text-gray-600">{h}</th>
                ))}
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {patients.map((patient) => (
                <tr key={patient.id} className="hover:bg-gray-50 transition-colors">
                  <td className="px-4 py-3 font-medium">{patient.patientFirstName}</td>
                  <td className="px-4 py-3">{patient.patientLastName}</td>
                  <td className="px-4 py-3 font-mono text-xs">{patient.patientAadhaarNumber}</td>
                  <td className="px-4 py-3 text-xs text-gray-500">
                    {patient.dateOfAdmission
                      ? new Date(patient.dateOfAdmission).toLocaleDateString()
                      : '—'}
                  </td>
                  <td className="px-4 py-3 font-mono text-xs text-gray-400 max-w-[140px] truncate">{patient.id}</td>
                  <td className="px-4 py-3">
                    <div className="flex gap-2">
                      <button className="btn-secondary text-xs" onClick={() => openEdit(patient)}>
                        Edit
                      </button>
                      <button
                        className="btn-danger text-xs"
                        onClick={() => patient.id && handleDelete(patient.id)}
                        disabled={loading}
                      >
                        Delete
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {/* Add Modal */}
      <Modal isOpen={addOpen} title="Add New Patient" onClose={() => setAddOpen(false)}>
        <PatientForm form={form} onChange={(f) => setForm(f as Omit<PatientDto, 'id' | 'dateOfAdmission'>)} />
        <div className="flex justify-end gap-3 mt-5">
          <button className="btn-secondary" onClick={() => setAddOpen(false)}>Cancel</button>
          <button className="btn-primary" onClick={handleAdd} disabled={loading}>
            {loading ? 'Saving…' : 'Add Patient'}
          </button>
        </div>
      </Modal>

      {/* Edit Modal */}
      <Modal isOpen={editOpen} title="Edit Patient" onClose={() => setEditOpen(false)}>
        <PatientForm
          form={editForm as Omit<PatientDto, 'id' | 'dateOfAdmission'>}
          onChange={setEditForm}
        />
        <div className="flex justify-end gap-3 mt-5">
          <button className="btn-secondary" onClick={() => setEditOpen(false)}>Cancel</button>
          <button className="btn-primary" onClick={handleEdit} disabled={loading}>
            {loading ? 'Saving…' : 'Update'}
          </button>
        </div>
      </Modal>
    </div>
  )
}

interface PatientFormProps {
  form: Partial<Omit<PatientDto, 'id' | 'dateOfAdmission'>>
  onChange: (f: Partial<Omit<PatientDto, 'id' | 'dateOfAdmission'>>) => void
}

function PatientForm({ form, onChange }: PatientFormProps) {
  return (
    <div className="grid grid-cols-1 gap-4">
      <div className="grid grid-cols-2 gap-4">
        <div>
          <label className="block text-xs font-medium text-gray-600 mb-1">First Name</label>
          <input
            className="input"
            placeholder="Jane"
            value={form.patientFirstName ?? ''}
            onChange={(e) => onChange({ ...form, patientFirstName: e.target.value })}
          />
        </div>
        <div>
          <label className="block text-xs font-medium text-gray-600 mb-1">Last Name</label>
          <input
            className="input"
            placeholder="Doe"
            value={form.patientLastName ?? ''}
            onChange={(e) => onChange({ ...form, patientLastName: e.target.value })}
          />
        </div>
      </div>
      <div>
        <label className="block text-xs font-medium text-gray-600 mb-1">Aadhaar Number</label>
        <input
          className="input"
          placeholder="1234 5678 9012"
          value={form.patientAadhaarNumber ?? ''}
          onChange={(e) => onChange({ ...form, patientAadhaarNumber: e.target.value })}
        />
      </div>
    </div>
  )
}
