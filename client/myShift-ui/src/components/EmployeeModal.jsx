import { useState, useEffect } from 'react';
import api from '../api/axiosInstance';

export default function EmployeeModal({ isOpen, onClose, projectId, onSuccess, employeeToEdit }) {
  const [empForm, setEmpForm] = useState({ 
    firstName: '', lastName: '', phone: '', email: '', role: 'WORKER', projectId: projectId 
  });

  useEffect(() => {
    const timer = setTimeout(() => {
      if (employeeToEdit) {
        setEmpForm({
          firstName: employeeToEdit.firstName,
          lastName: employeeToEdit.lastName,
          phone: employeeToEdit.phone,
          email: employeeToEdit.email,
          role: employeeToEdit.role,
          projectId: projectId
        });
      } else {
        setEmpForm({ firstName: '', lastName: '', phone: '', email: '', role: 'WORKER', projectId: projectId });
      }
    }, 0);

    return () => clearTimeout(timer);
  }, [employeeToEdit, projectId, isOpen]);

  if (!isOpen) return null;

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (employeeToEdit) {
        await api.put(`/employees/${employeeToEdit.id}`, empForm);
      } else {
        await api.post('/employees/create', empForm);
      }
      onSuccess();
      onClose();
    } catch {
      alert(employeeToEdit ? 'Błąd podczas edycji' : 'Błąd podczas dodawania pracownika');
    }
  };

  const inputStyle = "w-full px-4 py-2.5 border border-slate-300 rounded-lg focus:outline-none focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 transition-colors text-sm text-slate-800";
  const labelStyle = "block text-xs font-semibold text-slate-600 mb-1.5 mt-4";

  return (
    <div className="fixed inset-0 bg-slate-900/40 backdrop-blur-sm flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-3xl p-8 w-full max-w-md shadow-2xl relative">
        <button onClick={onClose} className="absolute top-6 right-6 text-slate-400 hover:text-slate-600">
          <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M6 18L18 6M6 6l12 12"></path></svg>
        </button>
        <h2 className="text-2xl font-bold text-slate-800 mb-6">
          {employeeToEdit ? 'Edytuj Pracownika' : 'Dodaj Pracownika'}
        </h2>
        
        <form onSubmit={handleSubmit}>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className={labelStyle}>Imię</label>
              <input required className={inputStyle} value={empForm.firstName} onChange={e => setEmpForm({...empForm, firstName: e.target.value})} />
            </div>
            <div>
              <label className={labelStyle}>Nazwisko</label>
              <input required className={inputStyle} value={empForm.lastName} onChange={e => setEmpForm({...empForm, lastName: e.target.value})} />
            </div>
          </div>
          <label className={labelStyle}>Email</label>
          <input type="email" required className={inputStyle} value={empForm.email} onChange={e => setEmpForm({...empForm, email: e.target.value})} />
          <label className={labelStyle}>Telefon</label>
          <input required className={inputStyle} value={empForm.phone} onChange={e => setEmpForm({...empForm, phone: e.target.value})} />
          <label className={labelStyle}>Stanowisko (Rola)</label>
          <select className={inputStyle} value={empForm.role} onChange={e => setEmpForm({...empForm, role: e.target.value})}>
            <option value="WORKER">Pracownik</option>
            <option value="MANAGER">Manager</option>
          </select>

          <div className="flex gap-3 mt-8">
            <button type="button" onClick={onClose} className="flex-1 py-3 bg-slate-100 text-slate-700 rounded-xl font-semibold hover:bg-slate-200 transition-colors">
              Anuluj
            </button>
            <button type="submit" className="flex-1 py-3 bg-indigo-600 text-white rounded-xl font-semibold hover:bg-indigo-700 transition-colors flex items-center justify-center gap-2 shadow-md">
              Zapisz
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}