import api from './api';

const taskService = {
  getAllTasks: async () => {
    const response = await api.get('/tasks');
    return response.data.data;
  },
  
  getTaskById: async (id) => {
    const response = await api.get(`/tasks/${id}`);
    return response.data.data;
  },
  
  createTask: async (taskData) => {
    const response = await api.post('/tasks', taskData);
    return response.data.data;
  },
  
  updateTask: async (id, taskData) => {
    const response = await api.put(`/tasks/${id}`, taskData);
    return response.data.data;
  },
  
  deleteTask: async (id) => {
    const response = await api.delete(`/tasks/${id}`);
    return response.data;
  },

  updateTaskStatus: async (id, status) => {
    const response = await api.patch(`/tasks/${id}/status`, { status });
    return response.data.data;
  },

  getMyTasks: async () => {
    const response = await api.get('/tasks/my-tasks');
    return response.data.data;
  },

  getOverdueTasks: async () => {
    const response = await api.get('/tasks/overdue');
    return response.data.data;
  }
};

export default taskService;
