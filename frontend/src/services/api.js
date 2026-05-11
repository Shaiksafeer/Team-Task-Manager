import axios from 'axios';
const BACKEND_URL = import.meta.env.VITE_API_URL;

if (!BACKEND_URL) {
  console.error('VITE_API_URL is not defined! Please check your .env file.');
  throw new Error('VITE_API_URL is missing. The application cannot connect to the backend.');
}

console.log('Backend URL:', BACKEND_URL);
const api = axios.create({
  baseURL: BACKEND_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor for adding JWT token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor for handling 401 Unauthorized
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      localStorage.removeItem('token');
      // Use window.location.pathname to avoid infinite redirect if already on login
      if (window.location.pathname !== '/login' && window.location.pathname !== '/signup') {
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  }
);

export default api;
