import { useState, useEffect } from 'react';
import api from '../api/axiosInstance';
import { useAuth } from '../context/AuthContext';

export default function LeaveRequestsWorker() {
  const { user } = useAuth();
  const [leaves, setLeaves] = useState([]);
  const [showModal, setShowModal] = useState(false);
  
  const [refreshCount, setRefreshCount] = useState(0); 

  const [formData, setFormData] = useState({
    startDate: '', endDate: '', type: 'HOLIDAY', reason: ''
  });

  useEffect(() => {
    const fetchLeaves = async () => {
      if (!user?.employeeId) return;
      try {
        const res = await api.get(`/leaves/employee/${user.employeeId}`);
        setLeaves(res.data);
      } catch (error) {
        console.error("Błąd pobierania urlopów:", error);
      }
    };

    fetchLeaves();
  }, [user?.employeeId, refreshCount]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.post('/leaves/create', {
        userId: user.id,
        employeeId: user.employeeId,
        projectId: user.projectId,
        ...formData
      });
      
      setShowModal(false);
      setFormData({ startDate: '', endDate: '', type: 'HOLIDAY', reason: '' });
      
      setRefreshCount(prev => prev + 1); 
      
    } catch (error) {
      console.error("Błąd wysyłania wniosku:", error);
    }
  };

  const getStatusBadge = (status) => {
    switch (status) {
      case 'APPROVED': return <span className="px-3 py-1 bg-emerald-100 text-emerald-700 rounded-full text-xs font-bold">Zatwierdzony</span>;
      case 'REJECTED': return <span className="px-3 py-1 bg-rose-100 text-rose-700 rounded-full text-xs font-bold">Odrzucony</span>;
      default: return <span className="px-3 py-1 bg-amber-100 text-amber-700 rounded-full text-xs font-bold">Oczekujący</span>;
    }
  };

  const getTypeLabel = (type) => {
    switch (type) {
      case 'SICK_LEAVE': return 'L4 (Zwolnienie)';
      case 'ON_DEMAND': return 'Na żądanie';
      default: return 'Wypoczynkowy';
    }
  };

  return (
    <div className="bg-white rounded-3xl shadow-sm border border-slate-100 p-6">
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-xl font-bold text-slate-800">Moje Wnioski Urlopowe</h2>
        <button onClick={() => setShowModal(true)} className="bg-indigo-600 text-white px-4 py-2 rounded-xl text-sm font-bold hover:bg-indigo-700 transition">
          + Złóż wniosek
        </button>
      </div>

      <div className="space-y-3">
        {leaves.length === 0 ? (
          <p className="text-slate-500 text-sm text-center py-4">Brak złożonych wniosków.</p>
        ) : (
          leaves.map(leave => (
            <div key={leave.id} className="flex items-center justify-between p-4 bg-slate-50 rounded-2xl border border-slate-100">
              <div>
                <p className="font-bold text-slate-800">{getTypeLabel(leave.type)}</p>
                <p className="text-xs text-slate-500 mt-1">{leave.startDate} — {leave.endDate}</p>
                {leave.reason && <p className="text-xs text-slate-400 mt-1 italic">"{leave.reason}"</p>}
              </div>
              <div>{getStatusBadge(leave.status)}</div>
            </div>
          ))
        )}
      </div>

      {showModal && (
        <div className="fixed inset-0 bg-slate-900/50 backdrop-blur-sm flex items-center justify-center p-4 z-50">
          <div className="bg-white rounded-3xl p-6 w-full max-w-md shadow-2xl">
            <h3 className="text-lg font-bold text-slate-800 mb-4">Nowy wniosek urlopowy</h3>
            <form onSubmit={handleSubmit} className="space-y-4">
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-xs font-bold text-slate-600 mb-1">Od kiedy</label>
                  <input type="date" required value={formData.startDate} onChange={e => setFormData({...formData, startDate: e.target.value})} className="w-full p-3 bg-slate-50 rounded-xl border border-slate-200 text-sm" />
                </div>
                <div>
                  <label className="block text-xs font-bold text-slate-600 mb-1">Do kiedy</label>
                  <input type="date" required value={formData.endDate} onChange={e => setFormData({...formData, endDate: e.target.value})} className="w-full p-3 bg-slate-50 rounded-xl border border-slate-200 text-sm" />
                </div>
              </div>
              <div>
                <label className="block text-xs font-bold text-slate-600 mb-1">Typ urlopu</label>
                <select value={formData.type} onChange={e => setFormData({...formData, type: e.target.value})} className="w-full p-3 bg-slate-50 rounded-xl border border-slate-200 text-sm">
                  <option value="HOLIDAY">Wypoczynkowy</option>
                  <option value="SICK_LEAVE">L4 (Zwolnienie lekarskie)</option>
                  <option value="ON_DEMAND">Na żądanie</option>
                </select>
              </div>
              <div>
                <label className="block text-xs font-bold text-slate-600 mb-1">Powód / Komentarz (opcjonalnie)</label>
                <textarea value={formData.reason} onChange={e => setFormData({...formData, reason: e.target.value})} className="w-full p-3 bg-slate-50 rounded-xl border border-slate-200 text-sm h-24" placeholder="Dodatkowe informacje dla przełożonego..."></textarea>
              </div>
              <div className="flex gap-3 pt-2">
                <button type="button" onClick={() => setShowModal(false)} className="flex-1 py-3 bg-slate-100 text-slate-700 rounded-xl font-bold text-sm hover:bg-slate-200 transition">Anuluj</button>
                <button type="submit" className="flex-1 py-3 bg-indigo-600 text-white rounded-xl font-bold text-sm hover:bg-indigo-700 transition">Wyślij wniosek</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}