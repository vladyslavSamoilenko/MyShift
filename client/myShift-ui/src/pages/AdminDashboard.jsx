// src/pages/AdminDashboard.jsx
import { useState, useEffect, useCallback } from 'react';
import { useAuth } from '../context/AuthContext';
import api from '../api/axiosInstance';
import AdminHeader from '../components/AdminHeader';
import EmployeeModal from '../components/EmployeeModal';
import ShiftModal from '../components/ShiftModal';
import WeeklyCalendar from '../components/WeeklyCalendar';
import LeaveRequestsAdmin from '../components/LeaveRequestsAdmin';

export default function AdminDashboard() {
  const { user, logout } = useAuth();
  const [employeeToEdit, setEmployeeToEdit] = useState(null);
  const [activeTab, setActiveTab] = useState('calendar'); 
  const [isEmployeeModalOpen, setIsEmployeeModalOpen] = useState(false);
  const [isShiftModalOpen, setIsShiftModalOpen] = useState(false);
  
  const [employees, setEmployees] = useState([]);
  const [shifts, setShifts] = useState([]); 

  const fetchEmployees = useCallback(async () => {
    if (!user?.projectId) return; 
    try {
      const res = await api.get(`/employees/project/${user.projectId}`); 
      if (res.data && res.data.payload) {
        setEmployees(res.data.payload.content || res.data.payload); 
      }
    } catch (error) {
      console.error('Błąd pobierania pracowników:', error);
    }
  }, [user]);

  const fetchProjectShifts = useCallback(async () => {
    if (!user?.projectId) return;
    try {
      const res = await api.post('/shifts/search', { projectId: user.projectId });
      if (res.data && res.data.payload) {
        setShifts(res.data.payload.content || res.data.payload);
      }
    } catch (error) {
      console.error('Błąd pobierania zmian:', error);
    }
  }, [user]);

  useEffect(() => {
    const loadData = async () => {
      await fetchEmployees();
      await fetchProjectShifts();
    };
    loadData();
  }, [fetchEmployees, fetchProjectShifts]);

  const getInitials = (first, last) => `${first?.charAt(0) || ''}${last?.charAt(0) || ''}`.toUpperCase();

  return (
    <div className="min-h-screen bg-slate-50 font-sans pb-12">
      <AdminHeader logout={logout} />

      <main className="max-w-6xl mx-auto px-6 mt-8">
        <div className="bg-white p-1.5 rounded-2xl shadow-sm flex mb-8 max-w-2xl mx-auto">
          <button onClick={() => setActiveTab('calendar')} className={`flex-1 py-3.5 rounded-xl text-sm font-semibold flex items-center justify-center gap-2 transition-all ${activeTab === 'calendar' ? 'bg-indigo-600 text-white shadow-md' : 'text-slate-500 hover:bg-slate-50'}`}>
            <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"></path></svg>
            Kalendarz Zmian
          </button>
          <button onClick={() => setActiveTab('employees')} className={`flex-1 py-3.5 rounded-xl text-sm font-semibold flex items-center justify-center gap-2 transition-all ${activeTab === 'employees' ? 'bg-indigo-600 text-white shadow-md' : 'text-slate-500 hover:bg-slate-50'}`}>
            <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z"></path></svg>
            Pracownicy
          </button>
          <button onClick={() => setActiveTab('leaves')} className={`flex-1 py-3.5 rounded-xl text-sm font-semibold flex items-center justify-center gap-2 transition-all ${activeTab === 'leaves' ? 'bg-indigo-600 text-white shadow-md' : 'text-slate-500 hover:bg-slate-50'}`}>
            <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M3 15a4 4 0 004 4h9a5 5 0 10-.1-9.999 5.002 5.002 0 10-9.78 2.096A4.001 4.001 0 003 15z"></path></svg>
            Wnioski Urlopowe
          </button>
        </div>

        <div className="bg-white rounded-3xl shadow-sm border border-slate-100 p-8 min-h-[600px]">
          
          {activeTab === 'employees' && (
            <div className="animate-fade-in">
              <div className="flex justify-between items-center mb-8">
                <h2 className="text-2xl font-bold text-slate-800">Zarządzanie Pracownikami</h2>
                <button onClick={() => { setEmployeeToEdit(null); setIsEmployeeModalOpen(true); }} className="bg-emerald-500 hover:bg-emerald-600 text-white px-5 py-2.5 rounded-xl text-sm font-semibold transition-colors flex items-center shadow-sm">
                    + Dodaj Pracownika
                </button>
              </div>

              <div className="mb-8 relative">
                <input type="text" placeholder="Szukaj pracownika..." className="w-full bg-slate-50 border-none rounded-2xl py-4 pl-4 text-sm focus:ring-2 focus:ring-indigo-100" />
              </div>

              <div className="flex flex-col gap-4">
                {employees.map(emp => (
                <div key={emp.id} className="border border-slate-100 rounded-2xl p-5 flex items-start justify-between hover:shadow-md transition-shadow">
                  <div className="flex gap-5">
                    <div className="w-14 h-14 rounded-xl bg-rose-400 text-white flex items-center justify-center font-bold text-xl shadow-sm">
                        {getInitials(emp.firstName, emp.lastName)}
                      </div>
                     <div>
                        <h3 className="font-bold text-slate-800 text-lg">{emp.firstName} {emp.lastName}</h3>
                        <p className="text-slate-500 text-sm mb-3">{emp.role === 'MANAGER' ? 'Manager' : 'Pracownik'}</p>
                        <p className="text-sm text-slate-600"><span className="text-slate-400 w-16 inline-block">Telefon:</span> {emp.phone}</p>
                      </div>
                  </div>
        
                   <button 
                      onClick={() => { setEmployeeToEdit(emp); setIsEmployeeModalOpen(true); }}
                     className="text-indigo-500 hover:bg-indigo-50 p-2.5 rounded-xl transition-colors"
                     title="Edytuj"
                    >
                     <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                       <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z" />
                      </svg>
                    </button>
                  </div>
                ))}
              </div>
            </div>
          )}

          {activeTab === 'calendar' && (
            <div className="animate-fade-in h-full flex flex-col">
              <div className="flex justify-between items-center mb-8">
                <h2 className="text-2xl font-bold text-slate-800">Kalendarz Zmian</h2>
                <button onClick={() => setIsShiftModalOpen(true)} className="bg-emerald-500 hover:bg-emerald-600 text-white px-5 py-2.5 rounded-xl text-sm font-semibold transition-colors flex items-center shadow-sm">
                  + Dodaj Zmianę
                </button>
              </div>
              
              <WeeklyCalendar shifts={shifts} employees={employees} />
            </div>
          )}
          {activeTab === 'leaves' && (
            <div className="animate-fade-in">
              <LeaveRequestsAdmin />
            </div>
          )}
        </div>
      </main>

      <EmployeeModal 
        isOpen={isEmployeeModalOpen} 
        onClose={() => { setIsEmployeeModalOpen(false); setEmployeeToEdit(null); }} 
        projectId={user?.projectId}
        onSuccess={fetchEmployees}
        employeeToEdit={employeeToEdit} 
      />
      
      <ShiftModal 
        isOpen={isShiftModalOpen} 
        onClose={() => setIsShiftModalOpen(false)} 
        projectId={user?.projectId}
        employees={employees} 
        onSuccess={fetchProjectShifts}
      />
    </div>
  );
}