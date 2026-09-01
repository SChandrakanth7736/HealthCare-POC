import { useState } from 'react'
import Navbar, { type Tab } from './components/Navbar'
import DoctorsPage from './components/DoctorsPage'
import PatientsPage from './components/PatientsPage'
import AssignmentsPage from './components/AssignmentsPage'

export default function App() {
  const [activeTab, setActiveTab] = useState<Tab>('doctors')

  return (
    <div className="min-h-screen bg-gray-50">
      <Navbar activeTab={activeTab} onTabChange={setActiveTab} />
      <main>
        {activeTab === 'doctors' && <DoctorsPage />}
        {activeTab === 'patients' && <PatientsPage />}
        {activeTab === 'assignments' && <AssignmentsPage />}
      </main>
    </div>
  )
}
