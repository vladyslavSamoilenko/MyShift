// src/components/StatCard.jsx
export default function StatCard({ title, value, subtitle, icon, iconColor, bgColor }) {
  return (
    <div className="bg-white p-6 rounded-3xl shadow-sm border border-slate-100 flex items-center justify-between hover:shadow-md transition-shadow">
      <div>
        <p className="text-sm font-medium text-slate-500 mb-1">{title}</p>
        <p className="text-4xl font-black text-slate-800 tracking-tight">{value}</p>
        <p className="text-xs font-medium text-slate-400 mt-1">{subtitle}</p>
      </div>
      <div className={`w-14 h-14 rounded-2xl flex items-center justify-center ${bgColor}`}>
        <svg className={`w-7 h-7 ${iconColor}`} fill="none" stroke="currentColor" viewBox="0 0 24 24" strokeWidth="2.5">
          <path strokeLinecap="round" strokeLinejoin="round" d={icon}></path>
        </svg>
      </div>
    </div>
  );
}