import { useState, useCallback } from 'react'
import toast from 'react-hot-toast'
import type { DoctorDto } from '../types'
import {
  createDoctor,
  deleteDoctorById,
  getDoctorById,
  getDoctorsByName,
  patchDoctor,
} from '../api/api'
import Modal from './Modal'

const emptyForm = (): Omit<DoctorDto, 'id' | 'dateOfAssignment'> => ({
  firstName: '',
  lastName: '',
  department: '',
  aadhaarNumber: '',
})

export default function DoctorsPage() {
  const [doctors, setDoctors] = useState<DoctorDto[]>([])
  const [searchName, setSearchName] = useState('')
  const [searchId, setSearchId] = useState('')
  const [loading, setLoading] = useState(false)

  const [addOpen, setAddOpen] = useState(false)
  const [editOpen, setEditOpen] = useState(false)
  const [editTarget, setEditTarget] = useState<DoctorDto | null>(null)
  const [form, setForm] = useState(emptyForm())
  const [editForm, setEditForm] = useState<Partial<DoctorDto>>({})

  const handleSearchByName = useCallback(async () => {
    if (!searchName.trim()) return toast.error('Enter a name to search')
    setLoading(true)
    try {
      const data = await getDoctorsByName(searchName.trim())
      setDoctors(data)
      if (data.length === 0) toast('No doctors found', { icon: 'ℹ️' })
    } catch (e: unknown) {
      toast.error(e instanceof Error ? e.message : 'Search failed')
    } finally {
      setLoading(false)
    }
  }, [searchName])

  const handleSearchById = useCallback(async () => {
    if (!searchId.trim()) return toast.error('Enter a doctor ID')
    setLoading(true)
    try {
      const data = await getDoctorById(searchId.trim())
      setDoctors([data])
    } catch (e: unknown) {
      toast.error(e instanceof Error ? e.message : 'Not found')
      setDoctors([])
    } finally {
      setLoading(false)
    }
  }, [searchId])

  const handleAdd = async () => {
    if (!form.firstName || !form.lastName || !form.department || !form.aadhaarNumber) {
      return toast.error('All fields are required')
    }
    setLoading(true)
    try {
      const created = await createDoctor(form)
      toast.success(`Dr. ${created.firstName} ${created.lastName} added!`)
      setAddOpen(false)
      setForm(emptyForm())
      setDoctors((prev) => [created, ...prev])
    } catch (e: unknown) {
      toast.error(e instanceof Error ? e.message : 'Failed to add doctor')
    } finally {
      setLoading(false)
    }
  }

  const handleDelete = async (doctorId: string) => {
    if (!confirm('Delete this doctor?')) return
    setLoading(true)
    try {
      await deleteDoctorById(doctorId)
      toast.success('Doctor deleted')
      setDoctors((prev) => prev.filter((d) => d.id !== doctorId))
    } catch (e: unknown) {
      toast.error(e instanceof Error ? e.message : 'Delete failed')
    } finally {
      setLoading(false)
    }
  }

  const openEdit = (doc: DoctorDto) => {
    setEditTarget(doc)
    setEditForm({
      firstName: doc.firstName,
      lastName: doc.lastName,
      department: doc.department,
      aadhaarNumber: doc.aadhaarNumber,
    })
    setEditOpen(true)
  }

  const handleEdit = async () => {
    if (!editTarget?.id) return
    setLoading(true)
    try {
      const updated = await patchDoctor(editTarget.id, editForm)
      toast.success('Doctor updated!')
      setDoctors((prev) => prev.map((d) => (d.id === updated.id ? updated : d)))
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
          👨‍⚕️ Doctors
        </h1>
        <button className="btn-primary" onClick={() => { setForm(emptyForm()); setAddOpen(true) }}>
          + Add Doctor
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
        {doctors.length > 0 && (
          <button className="btn-secondary" onClick={() => setDoctors([])}>
            Clear
          </button>
        )}
      </div>

      {/* Doctors table */}
      {doctors.length === 0 ? (
        <div className="card p-12 text-center text-gray-400">
          <div className="text-5xl mb-3">👨‍⚕️</div>
          <p>Search for doctors by name or ID, or add a new one.</p>
        </div>
      ) : (
        <div className="card overflow-hidden">
          <table className="w-full text-sm">
            <thead className="bg-gray-50 border-b border-gray-200">
              <tr>
                {['First Name', 'Last Name', 'Department', 'Aadhaar', 'ID', 'Actions'].map((h) => (
                  <th key={h} className="px-4 py-3 text-left font-medium text-gray-600">{h}</th>
                ))}
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {doctors.map((doc) => (
                <tr key={doc.id} className="hover:bg-gray-50 transition-colors">
                  <td className="px-4 py-3 font-medium">{doc.firstName}</td>
                  <td className="px-4 py-3">{doc.lastName}</td>
                  <td className="px-4 py-3">
                    <span className="badge bg-blue-100 text-blue-700">{doc.department}</span>
                  </td>
                  <td className="px-4 py-3 font-mono text-xs">{doc.aadhaarNumber}</td>
                  <td className="px-4 py-3 font-mono text-xs text-gray-400 max-w-[140px] truncate">{doc.id}</td>
                  <td className="px-4 py-3">
                    <div className="flex gap-2">
                      <button className="btn-secondary text-xs" onClick={() => openEdit(doc)}>
                        Edit
                      </button>
                      <button
                        className="btn-danger text-xs"
                        onClick={() => doc.id && handleDelete(doc.id)}
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
      <Modal isOpen={addOpen} title="Add New Doctor" onClose={() => setAddOpen(false)}>
        <DoctorForm form={form} onChange={(f) => setForm(f as Omit<DoctorDto, 'id' | 'dateOfAssignment'>)} />
        <div className="flex justify-end gap-3 mt-5">
          <button className="btn-secondary" onClick={() => setAddOpen(false)}>Cancel</button>
          <button className="btn-primary" onClick={handleAdd} disabled={loading}>
            {loading ? 'Saving…' : 'Add Doctor'}
          </button>
        </div>
      </Modal>

      {/* Edit Modal */}
      <Modal isOpen={editOpen} title="Edit Doctor" onClose={() => setEditOpen(false)}>
        <DoctorForm form={editForm as Omit<DoctorDto, 'id' | 'dateOfAssignment'>} onChange={setEditForm} />
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

interface DoctorFormProps {
  form: Partial<Omit<DoctorDto, 'id' | 'dateOfAssignment'>>
  onChange: (f: Partial<Omit<DoctorDto, 'id' | 'dateOfAssignment'>>) => void
}

function DoctorForm({ form, onChange }: DoctorFormProps) {
  const field = (key: keyof typeof form, label: string, placeholder = '') => (
    <div>
      <label className="block text-xs font-medium text-gray-600 mb-1">{label}</label>
      <input
        className="input"
        placeholder={placeholder}
        value={form[key] ?? ''}
        onChange={(e) => onChange({ ...form, [key]: e.target.value })}
      />
    </div>
  )
  return (
    <div className="grid grid-cols-2 gap-4">
      {field('firstName', 'First Name', 'John')}
      {field('lastName', 'Last Name', 'Smith')}
      {field('department', 'Department', 'Cardiology')}
      {field('aadhaarNumber', 'Aadhaar Number', '1234 5678 9012')}
    </div>
  )
}
