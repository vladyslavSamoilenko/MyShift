import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { useAuth } from './context/AuthContext';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage'; 
import AdminDashboard from './pages/AdminDashboard';
import WorkerDashboard from './pages/WorkerDashboard';

const ProtectedRoute = ({ children, allowedRoles }) => {
  const { user } = useAuth();
  if (!user) return <Navigate to="/login" replace />;
  
  const safeRole = user.role ? user.role.replace('ROLE_', '').toUpperCase() : '';
  
  if (allowedRoles && !allowedRoles.includes(safeRole)) {
    return <Navigate to="/" replace />;
  }
  return children;
};

export default function App() {
  const { user } = useAuth();
  
  const safeRole = user?.role ? user.role.replace('ROLE_', '').toUpperCase() : '';

  return (
    <Router>
      <div className="min-h-screen bg-slate-50 text-slate-900">
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          
          <Route path="/" element={
            user ? (
              (safeRole === 'ADMIN' || safeRole === 'MANAGER') 
                ? <Navigate to="/admin" /> 
                : (safeRole === 'WORKER')
                  ? <Navigate to="/worker" />
                  : (
                    <div className="min-h-screen flex flex-col items-center justify-center p-4">
                      <div className="bg-white p-8 rounded-3xl shadow-xl max-w-md text-center">
                        <h1 className="text-2xl font-bold text-rose-500 mb-2">Błąd roli systemu</h1>
                        <p className="text-slate-600 mb-6">Serwer zwrócił nieznaną rolę: <strong>{user.role || 'Brak roli'}</strong></p>
                        <p className="text-sm text-slate-400">Skontaktuj się z administratorem lub wyczyść Local Storage i zaloguj się ponownie.</p>
                      </div>
                    </div>
                  )
            ) : <Navigate to="/login" />
          } />

          <Route path="/admin" element={
            <ProtectedRoute allowedRoles={['ADMIN', 'MANAGER']}>
              <AdminDashboard />
            </ProtectedRoute>
          } />

          <Route path="/worker" element={
            <ProtectedRoute allowedRoles={['WORKER']}>
              <WorkerDashboard />
            </ProtectedRoute>
          } />
        </Routes>
      </div>
    </Router>
  );
}