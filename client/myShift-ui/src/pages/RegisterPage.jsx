import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import api from '../api/axiosInstance';

export default function RegisterPage() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState({
    email: '', password: '', confirmPassword: '',
    firstName: '', lastName: '', phone: '',
    projectName: '', projectDescription: ''
  });

  const handleSubmit = async (e) => {
    e.preventDefault();
    const payload = {
      userData: { email: form.email, password: form.password, confirmPassword: form.confirmPassword },
      employeeData: { firstName: form.firstName, lastName: form.lastName, phone: form.phone, role: "ADMIN" },
      projectData: { name: form.projectName, description: form.projectDescription }
    };

    try {
      const response = await api.post('/auth/registerUserOwner', payload);
      login(response.data.payload || response.data); 
      navigate('/admin');
      
    } catch (error) {
      console.error("Błąd rejestracji:", error);
      alert(error.response?.data?.message || 'Wystąpił błąd podczas rejestracji');
    }
  };

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  return (
    <div className="flex items-center justify-center min-h-screen py-10">
      <form onSubmit={handleSubmit} className="p-8 bg-white rounded-xl shadow-md w-[500px]">
        <h2 className="text-2xl font-bold mb-6 text-center">Założ konto</h2>
        
        <h3 className="font-semibold mt-4 mb-2">Dane konta</h3>
        <input name="email" type="email" placeholder="Email" required className="w-full mb-2 p-2 border rounded" onChange={handleChange} />
        <div className="flex gap-2 mb-2">
          <input name="password" type="password" placeholder="Hasło" required className="w-1/2 p-2 border rounded" onChange={handleChange} />
          <input name="confirmPassword" type="password" placeholder="Powtórz hasło" required className="w-1/2 p-2 border rounded" onChange={handleChange} />
        </div>

        <h3 className="font-semibold mt-4 mb-2">Dane kierownika</h3>
        <div className="flex gap-2 mb-2">
          <input name="firstName" placeholder="Imie" required className="w-1/2 p-2 border rounded" onChange={handleChange} />
          <input name="lastName" placeholder="Nazwisko" required className="w-1/2 p-2 border rounded" onChange={handleChange} />
        </div>
        <input name="phone" placeholder="Nr telefonu" required className="w-full mb-2 p-2 border rounded" onChange={handleChange} />

        <h3 className="font-semibold mt-4 mb-2">Firma</h3>
        <input name="projectName" placeholder="Nazwa firmy" required className="w-full mb-2 p-2 border rounded" onChange={handleChange} />
        <textarea name="projectDescription" placeholder="Opis" className="w-full mb-6 p-2 border rounded" onChange={handleChange} />

        <button type="submit" className="w-full bg-green-600 text-white p-2 rounded hover:bg-green-700">Zarejestruj firmę</button>
      </form>
    </div>
  );
}