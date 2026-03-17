import { useState, useEffect, useCallback } from 'react';
import { useAuth } from '../context/AuthContext';
import api from '../api/axiosInstance';
import WorkerSidebar from '../components/WorkerSidebar';
import StatCard from '../components/StatCard';

export default function WorkerDashboard() {
  const { user, logout } = useAuth();
  
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  const [activeTab, setActiveTab] = useState('schedule');
  const [shifts, setShifts] = useState([]);

  const fetchMyShifts = useCallback(async () => {
    if (!user?.id) return;
    try {
      const res = await api.post('/shifts/search', { userId: user.id });
      if (res.data && res.data.payload) {
        setShifts(res.data.payload.content || res.data.payload);
      }
    } catch (error) {
      console.error('Błąd pobierania zmian:', error);
    }
  }, [user]);

  const handleStatusChange = async (shiftId, newStatus) => {
    try {
      await api.put(`/shifts/updateShiftStatus/${shiftId}`, { status: newStatus });
      fetchMyShifts();
    } catch (error) {
      alert(error.response?.data?.message || 'Błąd aktualizacji statusu');
    }
  };

  useEffect(() => {
    const loadData = async () => {
      await fetchMyShifts();
    };
    
    loadData();
  }, [fetchMyShifts]);

  const totalHours = shifts.length * 8; 

  return (
    <div className="min-h-screen bg-slate-50 font-sans pb-12">
      
      <header className="bg-white px-6 py-4 shadow-sm flex items-center justify-between sticky top-0 z-30">
        <div className="flex items-center gap-4">
          <div className="bg-indigo-600 text-white w-12 h-12 rounded-xl flex items-center justify-center shadow-md">
            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path></svg>
          </div>
          <div>
            <h1 className="text-xl font-bold text-slate-800 tracking-tight">Panel Pracownika</h1>
            <p className="text-sm text-slate-500">Witaj, {user?.email?.split('@')[0] || 'Pracownik'}!</p>
          </div>
        </div>
        
        <button onClick={() => setIsSidebarOpen(true)} className="p-2.5 border border-slate-200 text-slate-600 rounded-xl hover:bg-slate-50 transition-colors shadow-sm">
          <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 6h16M4 12h16M4 18h16"></path></svg>
        </button>
      </header>

      <WorkerSidebar 
        isOpen={isSidebarOpen} 
        onClose={() => setIsSidebarOpen(false)} 
        activeTab={activeTab}
        setActiveTab={setActiveTab}
      />

      <main className="max-w-3xl mx-auto px-6 mt-8">
        
        {activeTab === 'schedule' && (
          <div className="space-y-8 animate-fade-in">
            <div className="grid grid-cols-1 gap-4">
              <StatCard 
                title="Ten tydzień" value={shifts.length} subtitle="zmian" 
                icon="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" 
                iconColor="text-blue-500" bgColor="bg-blue-50" 
              />
              <StatCard 
                title="Godziny w tym tygodniu" value={`${totalHours}h`} subtitle="przepracowane" 
                icon="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" 
                iconColor="text-emerald-500" bgColor="bg-emerald-50" 
              />
              <StatCard 
                title="Powiadomienia" value="2" subtitle="nieprzeczytane" 
                icon="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9" 
                iconColor="text-orange-500" bgColor="bg-orange-50" 
              />
            </div>

            <div>
              <h2 className="text-2xl font-bold text-slate-800 mb-6 tracking-tight">Mój Grafik</h2>
              <div className="bg-white rounded-3xl shadow-sm border border-slate-100 p-6 min-h-[200px]">
                <h3 className="text-lg font-semibold text-slate-800 mb-4">Nadchodzące Zmiany</h3>
                
                {shifts.length === 0 ? (
                   <p className="text-slate-500 text-center py-6">Brak zaplanowanych zmian na ten tydzień.</p>
                ) : (
                  <div className="space-y-4">
    {shifts.map(shift => (
      <div key={shift.id} className="p-5 border border-slate-100 rounded-2xl flex justify-between items-center hover:bg-slate-50 transition-colors shadow-sm">
        
        <div className="flex flex-col">
          <p className="font-bold text-slate-800 text-lg mb-1">{shift.shiftDate}</p>
          <p className="text-sm font-semibold text-slate-500 bg-white border border-slate-200 inline-block px-3 py-1 rounded-lg w-max">
            {shift.startTime} - {shift.endTime}
          </p>
        </div>

        <div className="flex flex-col items-end gap-2">
          <span className="text-xs font-bold text-slate-400 uppercase tracking-wider mb-1">
            {shift.status === 'PLANNED' ? 'Zaplanowana' : shift.status}
          </span>

          <div className="flex gap-2">
            {shift.status === 'PLANNED' && (
              <button onClick={() => handleStatusChange(shift.id, 'PRESENT')} className="text-emerald-700 bg-emerald-100 px-4 py-2 rounded-xl text-sm font-bold hover:bg-emerald-200 transition-colors flex items-center gap-1 shadow-sm">
                ▶ Rozpocznij
              </button>
            )}
            
            {shift.status === 'PRESENT' && (
              <>
                <button onClick={() => handleStatusChange(shift.id, 'BREAK_START')} className="text-amber-700 bg-amber-100 px-4 py-2 rounded-xl text-sm font-bold hover:bg-amber-200 transition-colors shadow-sm">
                  ⏸ Przerwa
                </button>
                <button onClick={() => handleStatusChange(shift.id, 'FINISHED')} className="text-rose-700 bg-rose-100 px-4 py-2 rounded-xl text-sm font-bold hover:bg-rose-200 transition-colors shadow-sm">
                  ⏹ Zakończ
                      </button>
                            </>
                              )}

                            {shift.status === 'BREAK_START' && (
                              <button onClick={() => handleStatusChange(shift.id, 'BREAK_END')} className="text-blue-700 bg-blue-100 px-4 py-2 rounded-xl text-sm font-bold hover:bg-blue-200 transition-colors shadow-sm">
                               ▶ Wznów pracę
                              </button>
                              )}
            
                            {shift.status === 'FINISHED' && (
                              <span className="text-emerald-500 font-bold flex items-center gap-1">
                              <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path></svg>
                                Zakończona
                             </span>
                          )}
                          </div>
                        </div>

                    </div>
                    ))}
                  </div>
                )}
              </div>
            </div>
          </div>
        )}

        {activeTab === 'notifications' && (
          <div><h2 className="text-2xl font-bold text-slate-800 mb-6">Powiadomienia</h2><div className="bg-white p-6 rounded-3xl">Brak nowych powiadomień.</div></div>
        )}
        {activeTab === 'profile' && (
          <div>
            <h2 className="text-2xl font-bold text-slate-800 mb-6">Mój Profil</h2>
            <div className="bg-white p-6 rounded-3xl flex flex-col items-center">
               <div className="w-24 h-24 bg-amber-100 rounded-full flex items-center justify-center text-4xl mb-4 shadow-inner">👩‍🍳</div>
               <h3 className="text-xl font-bold">{user?.email}</h3>
               <button onClick={logout} className="mt-6 text-rose-500 font-bold px-4 py-2 hover:bg-rose-50 rounded-xl">Wyloguj się</button>
            </div>
          </div>
        )}
        {activeTab === 'settings' && (
          <div><h2 className="text-2xl font-bold text-slate-800 mb-6">Ustawienia</h2><div className="bg-white p-6 rounded-3xl">Ustawienia konta (w budowie).</div></div>
        )}

      </main>
    </div>
  );
}