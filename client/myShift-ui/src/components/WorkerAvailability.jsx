import { useState, useEffect } from 'react';
import api from '../api/axiosInstance';
import { useAuth } from '../context/AuthContext';

const DAYS_MAP = {
  MONDAY: 'Poniedziałek', 
  TUESDAY: 'Wtorek', 
  WEDNESDAY: 'Środa',
  THURSDAY: 'Czwartek', 
  FRIDAY: 'Piątek', 
  SATURDAY: 'Sobota', 
  SUNDAY: 'Niedziela'
};

export default function WorkerAvailability() {
  const { user } = useAuth();
  const [availabilities, setAvailabilities] = useState([]);
  const [refreshCount, setRefreshCount] = useState(0);
  const [isLoading, setIsLoading] = useState(false);

  const [formData, setFormData] = useState({
    dayOfWeek: 'MONDAY',
    startTime: '08:00',
    endTime: '16:00'
  });

  useEffect(() => {
    const fetchAvailabilities = async () => {
      if (!user?.employeeId) return;
      try {
        const res = await api.get(`/availabilities/employee/${user.employeeId}`);
        setAvailabilities(Array.isArray(res.data) ? res.data : []);
      } catch (error) {
        console.error("Błąd pobierania dyspozycyjności:", error);
      }
    };
    fetchAvailabilities();
  }, [user?.employeeId, refreshCount]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    try {
      await api.post('/availabilities/save', {
        employeeId: user.employeeId,
        dayOfWeek: formData.dayOfWeek,
        startTime: formData.startTime + ':00',
        endTime: formData.endTime + ':00'
      });
      setRefreshCount(prev => prev + 1); 
    } catch (error) {
      console.error("Błąd zapisywania dyspozycyjności:", error);
    } finally {
      setIsLoading(false);
    }
  };

  const getDayAvailability = (day) => {
    return availabilities.find(a => a.dayOfWeek === day);
  };

  return (
    <div className="bg-white rounded-3xl shadow-sm border border-slate-100 p-6 mt-8">
      <h2 className="text-xl font-bold text-slate-800 mb-6">Moja Dyspozycyjność</h2>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
        
        <div className="bg-slate-50 p-6 rounded-2xl border border-slate-100">
          <h3 className="text-sm font-bold text-slate-700 uppercase tracking-wider mb-4">Zaktualizuj godziny</h3>
          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-xs font-bold text-slate-600 mb-1">Dzień tygodnia</label>
              <select 
                value={formData.dayOfWeek} 
                onChange={(e) => setFormData({...formData, dayOfWeek: e.target.value})}
                className="w-full p-3 bg-white rounded-xl border border-slate-200 text-sm focus:ring-2 focus:ring-indigo-100"
              >
                {Object.entries(DAYS_MAP).map(([key, label]) => (
                  <option key={key} value={key}>{label}</option>
                ))}
              </select>
            </div>
            
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-xs font-bold text-slate-600 mb-1">Od godziny</label>
                <input 
                  type="time" 
                  required 
                  value={formData.startTime}
                  onChange={(e) => setFormData({...formData, startTime: e.target.value})}
                  className="w-full p-3 bg-white rounded-xl border border-slate-200 text-sm focus:ring-2 focus:ring-indigo-100" 
                />
              </div>
              <div>
                <label className="block text-xs font-bold text-slate-600 mb-1">Do godziny</label>
                <input 
                  type="time" 
                  required 
                  value={formData.endTime}
                  onChange={(e) => setFormData({...formData, endTime: e.target.value})}
                  className="w-full p-3 bg-white rounded-xl border border-slate-200 text-sm focus:ring-2 focus:ring-indigo-100" 
                />
              </div>
            </div>

            <button 
              type="submit" 
              disabled={isLoading}
              className="w-full py-3 mt-2 bg-indigo-600 text-white rounded-xl font-bold text-sm hover:bg-indigo-700 transition shadow-md disabled:bg-indigo-400"
            >
              {isLoading ? 'Zapisywanie...' : 'Zapisz dyspozycyjność'}
            </button>
          </form>
        </div>

        <div>
          <h3 className="text-sm font-bold text-slate-700 uppercase tracking-wider mb-4">Twój aktualny grafik</h3>
          <div className="space-y-2">
            {Object.keys(DAYS_MAP).map(day => {
              const av = getDayAvailability(day);
              const formatTime = (time) => time ? time.substring(0, 5) : '';
              
              return (
                <div key={day} className="flex items-center justify-between p-3 bg-slate-50 rounded-xl border border-slate-100">
                  <span className="font-semibold text-slate-700 text-sm">{DAYS_MAP[day]}</span>
                  {av ? (
                    <span className="px-3 py-1 bg-emerald-100 text-emerald-700 rounded-lg text-xs font-bold">
                      {formatTime(av.startTime)} - {formatTime(av.endTime)}
                    </span>
                  ) : (
                    <span className="px-3 py-1 bg-slate-200 text-slate-500 rounded-lg text-xs font-bold">
                      Brak danych
                    </span>
                  )}
                </div>
              );
            })}
          </div>
        </div>

      </div>
    </div>
  );
}