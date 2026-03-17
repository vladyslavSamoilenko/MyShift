// src/pages/RegisterPage.jsx
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

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
      const response = await fetch('/auth/registerUserOwner', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });
      const data = await response.json();
      if (response.ok) {
        login(data.payload);
        navigate('/admin');
      } else {
        alert('Ошибка регистрации');
      }
    } catch (error) {
      console.error(error);
    }
  };

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  return (
    <div className="flex items-center justify-center min-h-screen py-10">
      <form onSubmit={handleSubmit} className="p-8 bg-white rounded-xl shadow-md w-[500px]">
        <h2 className="text-2xl font-bold mb-6 text-center">Регистрация бизнеса</h2>
        
        <h3 className="font-semibold mt-4 mb-2">Данные аккаунта</h3>
        <input name="email" type="email" placeholder="Email" required className="w-full mb-2 p-2 border rounded" onChange={handleChange} />
        <div className="flex gap-2 mb-2">
          <input name="password" type="password" placeholder="Пароль" required className="w-1/2 p-2 border rounded" onChange={handleChange} />
          <input name="confirmPassword" type="password" placeholder="Повторите пароль" required className="w-1/2 p-2 border rounded" onChange={handleChange} />
        </div>

        <h3 className="font-semibold mt-4 mb-2">Данные руководителя</h3>
        <div className="flex gap-2 mb-2">
          <input name="firstName" placeholder="Имя" required className="w-1/2 p-2 border rounded" onChange={handleChange} />
          <input name="lastName" placeholder="Фамилия" required className="w-1/2 p-2 border rounded" onChange={handleChange} />
        </div>
        <input name="phone" placeholder="Телефон" required className="w-full mb-2 p-2 border rounded" onChange={handleChange} />

        <h3 className="font-semibold mt-4 mb-2">Проект (Бизнес)</h3>
        <input name="projectName" placeholder="Название проекта" required className="w-full mb-2 p-2 border rounded" onChange={handleChange} />
        <textarea name="projectDescription" placeholder="Описание" className="w-full mb-6 p-2 border rounded" onChange={handleChange} />

        <button type="submit" className="w-full bg-green-600 text-white p-2 rounded hover:bg-green-700">Создать бизнес</button>
      </form>
    </div>
  );
}