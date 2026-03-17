// src/components/ShiftModal.jsx
import { useState } from 'react';
import api from '../api/axiosInstance';

export default function ShiftModal({ isOpen, onClose, projectId, employees, onSuccess }) {
  const [shiftForm, setShiftForm] = useState({ 
    shiftDate: '', startTime: '', endTime: '', projectId: projectId, userId: '', employeeId: '' 
  });

  if (!isOpen) return null;

  const handleSubmit = async (e) => {
    onSuccess();
    e.preventDefault();
    if (!shiftForm.employeeId) return alert("Wybierz pracownika!");

    try {
      await api.post('/shifts/create', { ...shiftForm });
      setShiftForm({ shiftDate: '', startTime: '', endTime: '', projectId: projectId, userId: '', employeeId: '' });
      onClose();
    } catch (error) {
      alert(error.response?.data?.message || 'Błąd podczas dodawania zmiany');
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
        <h2 className="text-2xl font-bold text-slate-800 mb-6">Dodaj Zmianę</h2>
        
        <form onSubmit={handleSubmit}>
          <label className={labelStyle}>Pracownik</label>
          <select required className={inputStyle} value={shiftForm.employeeId} onChange={e => {
              const id = parseInt(e.target.value);
              setShiftForm({ ...shiftForm, employeeId: id, userId: id });
            }}>
            <option value="" disabled>Wybierz pracownika z listy...</option>
            {employees.map(emp => (
              <option key={emp.id} value={emp.id}>{emp.firstName} {emp.lastName}</option>
            ))}
          </select>

          <label className={labelStyle}>Data</label>
          <input type="date" required className={inputStyle} value={shiftForm.shiftDate} onChange={e => setShiftForm({...shiftForm, shiftDate: e.target.value})} />

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className={labelStyle}>Rozpoczęcie</label>
              <input type="time" required className={inputStyle} value={shiftForm.startTime} onChange={e => setShiftForm({...shiftForm, startTime: e.target.value})} />
            </div>
            <div>
              <label className={labelStyle}>Zakończenie</label>
              <input type="time" required className={inputStyle} value={shiftForm.endTime} onChange={e => setShiftForm({...shiftForm, endTime: e.target.value})} />
            </div>
          </div>

          <div className="flex gap-3 mt-8">
            <button type="button" onClick={onClose} className="flex-1 py-3 bg-slate-100 text-slate-700 rounded-xl font-semibold hover:bg-slate-200 transition-colors">
              Anuluj
            </button>
            <button type="submit" className="flex-1 py-3 bg-emerald-500 text-white rounded-xl font-semibold hover:bg-emerald-600 transition-colors flex items-center justify-center shadow-md">
              Zapisz
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}