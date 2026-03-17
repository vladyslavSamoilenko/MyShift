import { useState } from 'react';

const getStartOfWeek = (date) => {
  const d = new Date(date);
  const day = d.getDay();
  const diff = d.getDate() - day + (day === 0 ? -6 : 1);
  d.setDate(diff);
  d.setHours(0, 0, 0, 0);
  return d;
};

const addDays = (date, days) => {
  const result = new Date(date);
  result.setDate(result.getDate() + days);
  return result;
};

const formatDateForApi = (date) => {
  const yyyy = date.getFullYear();
  const mm = String(date.getMonth() + 1).padStart(2, '0');
  const dd = String(date.getDate()).padStart(2, '0');
  return `${yyyy}-${mm}-${dd}`;
};

const dayNames = ['Pon', 'Wt', 'Śr', 'Czw', 'Pt', 'Sob', 'Nie'];

export default function WeeklyCalendar({ shifts, employees }) {
  const [currentWeekStart, setCurrentWeekStart] = useState(getStartOfWeek(new Date()));

  const nextWeek = () => setCurrentWeekStart(addDays(currentWeekStart, 7));
  const prevWeek = () => setCurrentWeekStart(addDays(currentWeekStart, -7));

  const weekDays = Array.from({ length: 7 }).map((_, i) => addDays(currentWeekStart, i));

  const getEmployee = (id) => employees.find(emp => emp.id === id) || {};

  const getBorderColor = (id) => {
    const colors = ['border-rose-400', 'border-blue-400', 'border-emerald-400', 'border-amber-400', 'border-purple-400'];
    return colors[(id || 0) % colors.length];
  };

  return (
    <div className="flex flex-col h-full bg-white rounded-2xl shadow-sm border border-slate-100 overflow-hidden animate-fade-in">
      <div className="flex items-center justify-between px-6 py-4 border-b border-slate-100 bg-slate-50/50">
        <button onClick={prevWeek} className="px-4 py-2 text-sm font-semibold text-slate-600 hover:bg-slate-100 rounded-lg transition-colors">
          &larr; Poprzedni
        </button>
        <h3 className="text-lg font-bold text-slate-800 capitalize">
          {currentWeekStart.toLocaleDateString('pl-PL', { month: 'long', year: 'numeric' })}
        </h3>
        <button onClick={nextWeek} className="px-4 py-2 text-sm font-semibold text-slate-600 hover:bg-slate-100 rounded-lg transition-colors">
          Następny &rarr;
        </button>
      </div>

      <div className="grid grid-cols-7 flex-1 divide-x divide-slate-100 min-h-[500px]">
        {weekDays.map((day, index) => {
          const dateString = formatDateForApi(day);
          const dayShifts = shifts.filter(s => s.shiftDate === dateString);

          return (
            <div key={dateString} className="flex flex-col bg-white">
              <div className="py-4 text-center border-b border-slate-100 bg-white">
                <p className="text-sm font-bold text-slate-800">{dayNames[index]}</p>
                <p className="text-2xl font-black text-slate-800 tracking-tight mt-1">{day.getDate()}</p>
              </div>

              <div className="flex-1 p-2 bg-slate-50/30">
                {dayShifts.length === 0 ? (
                  <div className="h-full flex flex-col items-center justify-center text-slate-300 gap-2 py-8">
                    <svg className="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
                    <p className="text-xs font-semibold">Brak zmian</p>
                  </div>
                ) : (
                  <div className="space-y-3 mt-2">
                    {dayShifts.map(shift => {
                      const emp = getEmployee(shift.employeeId);
                      const colorClass = getBorderColor(emp.id);

                      return (
                        <div key={shift.id} className={`bg-white p-3 rounded-xl shadow-sm border border-slate-100 border-l-4 ${colorClass} hover:shadow-md transition-all cursor-pointer`}>
                          <p className="text-sm font-bold text-slate-800 leading-tight">
                            {emp.firstName || 'Nieznany'} {emp.lastName || ''}
                          </p>
                          <p className="text-xs text-slate-500 mt-1">{emp.role === 'MANAGER' ? 'Manager' : 'Pracownik'}</p>
                          <div className="mt-3 text-xs font-semibold text-slate-600 bg-slate-50 inline-block px-2 py-1 rounded-md">
                            {shift.startTime} - {shift.endTime}
                          </div>
                        </div>
                      );
                    })}
                  </div>
                )}
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}