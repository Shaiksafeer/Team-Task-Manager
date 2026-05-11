import api from './api';

const projectService = {
  getAllProjects: async () => {
    const response = await api.get('/projects');
    return response.data.data;
  },
  
  getProjectById: async (id) => {
    const response = await api.get(`/projects/${id}`);
    return response.data.data;
  },
  
  createProject: async (projectData) => {
    const response = await api.post('/projects', projectData);
    return response.data.data;
  },
  
  updateProject: async (id, projectData) => {
    const response = await api.put(`/projects/${id}`, projectData);
    return response.data.data;
  },
  
  deleteProject: async (id) => {
    const response = await api.delete(`/projects/${id}`);
    return response.data;
  },

  addMemberToProject: async (projectId, userId) => {
    const response = await api.post(`/projects/${projectId}/members/${userId}`);
    return response.data;
  },

  getProjectMembers: async (projectId) => {
    const response = await api.get(`/projects/${projectId}/members`);
    return response.data.data;
  }
};

export default projectService;
