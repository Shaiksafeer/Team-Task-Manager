import api from './api';

const authService = {
  login: async (credentials) => {
    // Backend returns ApiResponse<AuthResponse>
    const response = await api.post('/auth/login', credentials);
    const authData = response.data.data;
    
    if (authData.token) {
      localStorage.setItem('token', authData.token);
      localStorage.setItem('user', JSON.stringify({
        email: authData.email,
        role: authData.role,
        name: authData.name || authData.email.split('@')[0] // Fallback if name is missing
      }));
    }
    return authData;
  },
  
  signup: async (userData) => {
    // Backend returns ApiResponse<AuthResponse>
    const response = await api.post('/auth/register', userData);
    return response.data.data;
  },
  
  logout: () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  },
  
  getCurrentUser: () => {
    const token = localStorage.getItem('token');
    const user = localStorage.getItem('user');
    if (!token || !user) return null;
    
    return JSON.parse(user);
  }
};

export default authService;
