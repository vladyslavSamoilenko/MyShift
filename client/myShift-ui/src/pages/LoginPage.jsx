// src/pages/LoginPage.jsx
import { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import api from '../api/axiosInstance'; // <-- ДОБАВИЛИ ИМПОРТ API!

export default function LoginPage() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    
    try {
      // 1. Делаем реальный запрос к бэкенду. 
      // ВНИМАНИЕ: Проверь путь! Обычно в Spring это '/auth/login' или просто '/login'
      const response = await api.post('/auth/login', { email, password });
      
      // 2. Достаем данные юзера из ответа бэкенда. 
      // Зависит от структуры твоего JSON. У тебя обычно данные лежат в payload
      const userData = response.data.payload || response.data;
      
      // 3. Сохраняем реальный объект с ролью в LocalStorage и стейт!
      login(userData);
      
      // 4. Переходим на главную, где App.jsx нас правильно раскидает по ролям
      navigate('/');
      
    } catch (err) {
      console.error('Błąd logowania:', err);
      setError('Nieprawidłowy email lub hasło');
    }
  };

  return (
    <div className="min-h-screen bg-slate-50 flex items-center justify-center p-4 font-sans">
      <div className="bg-white max-w-md w-full rounded-[2rem] shadow-2xl p-10 animate-fade-in">
        
        {/* Логотип и заголовок */}
        <div className="flex flex-col items-center mb-10">
          <div className="bg-indigo-600 text-white w-16 h-16 rounded-2xl flex items-center justify-center shadow-lg mb-5">
            <svg className="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          </div>
          <h1 className="text-2xl font-black text-slate-800 tracking-tight">ShiftManager Pro</h1>
          <p className="text-sm text-slate-500 font-medium mt-2">Zaloguj się, aby kontynuować</p>
        </div>

        {/* Форма */}
        <form onSubmit={handleSubmit} className="space-y-5">
          
          {error && (
            <div className="bg-rose-50 text-rose-600 p-4 rounded-xl text-sm font-bold text-center border border-rose-100">
              {error}
            </div>
          )}

          <div>
            <label className="block text-xs font-bold text-slate-600 mb-2 uppercase tracking-wide">Email</label>
            <input 
              type="email" 
              required 
              value={email} 
              onChange={(e) => setEmail(e.target.value)} 
              className="w-full px-5 py-3.5 bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:border-indigo-500 focus:ring-2 focus:ring-indigo-100 transition-all text-sm text-slate-800 font-medium placeholder-slate-400" 
              placeholder="twoj@email.com" 
            />
          </div>
          
          <div>
            <label className="block text-xs font-bold text-slate-600 mb-2 uppercase tracking-wide">Hasło</label>
            <input 
              type="password" 
              required 
              value={password} 
              onChange={(e) => setPassword(e.target.value)} 
              className="w-full px-5 py-3.5 bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:border-indigo-500 focus:ring-2 focus:ring-indigo-100 transition-all text-sm text-slate-800 font-medium placeholder-slate-400" 
              placeholder="••••••••" 
            />
          </div>

          <button 
            type="submit" 
            className="w-full py-4 bg-indigo-600 text-white rounded-xl font-bold hover:bg-indigo-700 transition-colors shadow-lg shadow-indigo-200 flex items-center justify-center gap-2 mt-4"
          >
            Zaloguj się
          </button>
        </form>
      </div>
    </div>
  );
}