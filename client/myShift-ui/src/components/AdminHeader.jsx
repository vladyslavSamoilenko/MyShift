export default function AdminHeader({ logout }) {
  return (
    <header className="bg-white flex items-center justify-between px-8 py-4 shadow-sm">
      <div className="flex items-center gap-4">
        <div className="bg-indigo-600 text-white w-12 h-12 rounded-xl flex items-center justify-center shadow-md">
          <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
        </div>
        <div>
          <h1 className="text-xl font-bold text-slate-800 tracking-tight">ShiftManager Pro</h1>
          <p className="text-xs text-slate-500">Zarządzanie zmianami pracowników</p>
        </div>
      </div>
      
      <div className="flex items-center gap-6 text-right">
        <div className="hidden md:block">
          <p className="text-xs text-slate-500">Dzisiaj</p>
          <p className="text-sm font-bold text-slate-800">{new Date().toLocaleDateString('pl-PL')}</p>
        </div>
        <button onClick={logout} className="text-slate-500 hover:text-indigo-600 font-medium text-sm transition-colors">
          Wyloguj
        </button>
      </div>
    </header>
  );
}