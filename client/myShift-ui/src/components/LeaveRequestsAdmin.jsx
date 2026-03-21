import { useState, useEffect } from 'react';
import api from '../api/axiosInstance';
import { useAuth } from '../context/AuthContext';

export default function LeaveRequestsAdmin() {
  const { user } = useAuth();
  const [leaves, setLeaves] = useState([]);
  const [refreshCount, setRefreshCount] = useState(0);

  useEffect(() => {
    const fetchLeaves = async () => {
      if (!user?.projectId) return;
      try {
        const res = await api.get(`/leaves/project/${user.projectId}`);
        setLeaves(Array.isArray(res.data) ? res.data : []);
      } catch (error) {
        console.error("Błąd pobierania wniosków:", error);
        setLeaves([]);
      }
    };

    fetchLeaves();
  }, [user?.projectId, refreshCount]);

  const handleStatusChange = async (id, newStatus) => {
    try {
      await api.put(`/leaves/${id}/status`, { status: newStatus });
      setRefreshCount(prev => prev + 1); 
    } catch (error) {
      console.error("Błąd zmiany statusu:", error);
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
      <h2 className="text-xl font-bold text-slate-800 mb-6">Wnioski Urlopowe do Akceptacji</h2>

      <div className="space-y-4">
        {leaves.length === 0 ? (
          <p className="text-slate-500 text-sm text-center py-4">Brak nowych wniosków.</p>
        ) : (
          leaves.map(leave => (
            <div key={leave.id} className="flex items-center justify-between p-4 bg-slate-50 rounded-2xl border border-slate-100">
              <div>
                <div className="flex items-center gap-2 mb-1">
                  <span className="font-bold text-slate-800">Pracownik ID: {leave.employeeId}</span>
                  <span className="px-2 py-0.5 bg-indigo-100 text-indigo-700 rounded text-[10px] font-black uppercase tracking-wider">
                    {getTypeLabel(leave.type)}
                  </span>
                </div>
                <p className="text-sm text-slate-600">{leave.startDate} — {leave.endDate}</p>
                {leave.reason && <p className="text-xs text-slate-400 mt-1 italic">"{leave.reason}"</p>}
              </div>

              {leave.status === 'PENDING' ? (
                <div className="flex gap-2">
                  <button onClick={() => handleStatusChange(leave.id, 'APPROVED')} className="p-2 bg-emerald-100 text-emerald-600 hover:bg-emerald-200 rounded-xl transition tooltip" title="Zatwierdź">
                    <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7" /></svg>
                  </button>
                  <button onClick={() => handleStatusChange(leave.id, 'REJECTED')} className="p-2 bg-rose-100 text-rose-600 hover:bg-rose-200 rounded-xl transition" title="Odrzuć">
                    <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M6 18L18 6M6 6l12 12" /></svg>
                  </button>
                </div>
              ) : (
                <div>
                  <span className={`px-3 py-1 rounded-full text-xs font-bold ${leave.status === 'APPROVED' ? 'bg-emerald-100 text-emerald-700' : 'bg-rose-100 text-rose-700'}`}>
                    {leave.status === 'APPROVED' ? 'Zatwierdzony' : 'Odrzucony'}
                  </span>
                </div>
              )}
            </div>
          ))
        )}
      </div>
    </div>
  );
}