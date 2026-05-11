import api from './api';

const dashboardService = {
  getAdminDashboard: async () => {
    const response = await api.get('/dashboard/admin');
    return response.data.data;
  },
  
  getMemberDashboard: async () => {
    const response = await api.get('/dashboard/member');
    return response.data.data;
  },

  getOverdueTasks: async () => {
    const response = await api.get('/dashboard/overdue');
    return response.data.data;
  }
};

export default dashboardService;
