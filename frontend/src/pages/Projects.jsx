import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import projectService from '../services/projectService';
import userService from '../services/userService';
import Loader from '../components/Loader';
import { 
  Plus, 
  MoreVertical, 
  Edit2, 
  Trash2, 
  Users,
  UserPlus,
  X
} from 'lucide-react';
import './Projects.css';

const Projects = () => {
  const { user } = useAuth();
  const [projects, setProjects] = useState([]);
  const [allUsers, setAllUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [showMemberModal, setShowMemberModal] = useState(false);
  const [selectedProject, setSelectedProject] = useState(null);
  const [newProject, setNewProject] = useState({ name: '', description: '' });
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    fetchProjects();
    if (user.role === 'ADMIN') {
      fetchUsers();
    }
  }, []);

  const fetchProjects = async () => {
    try {
      const data = await projectService.getAllProjects();
      setProjects(data);
    } catch (err) {
      console.error('Failed to fetch projects');
    } finally {
      setLoading(false);
    }
  };

  const fetchUsers = async () => {
    try {
      const data = await userService.getAllUsers();
      setAllUsers(data);
    } catch (err) {
      console.error('Failed to fetch users');
    }
  };

  const handleCreateProject = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      await projectService.createProject(newProject);
      setShowModal(false);
      setNewProject({ name: '', description: '' });
      fetchProjects();
    } catch (err) {
      console.error('Failed to create project');
    } finally {
      setSubmitting(false);
    }
  };

  const handleDeleteProject = async (id) => {
    if (window.confirm('Are you sure you want to delete this project?')) {
      try {
        await projectService.deleteProject(id);
        fetchProjects();
      } catch (err) {
        console.error('Failed to delete project');
      }
    }
  };

  const handleAddMember = async (userId) => {
    try {
      await projectService.addMemberToProject(selectedProject.id, userId);
      fetchProjects(); // Refresh project list to show new member
      setShowMemberModal(false);
      alert('Member added successfully!');
    } catch (err) {
      alert(err.response?.data?.message || 'Failed to add member');
    }
  };

  if (loading) return <Loader />;

  return (
    <>
    <div className="projects-container fade-in">
      <header className="page-header">
        <div className="header-info">
          <h1>Projects</h1>
          <p className="text-secondary">Manage and track your team's initiatives.</p>
        </div>
        {user.role === 'ADMIN' && (
          <button className="create-btn" onClick={() => setShowModal(true)}>
            <Plus size={20} />
            <span>New Project</span>
          </button>
        )}
      </header>

      <div className="projects-grid">
        {projects.length > 0 ? (
          projects.map(project => (
            <div key={project.id} className="project-card glass-panel">
              <div className="project-title">
                <div>
                  <h3>{project.name}</h3>
                  <p className="project-desc">{project.description}</p>
                </div>
                {user.role === 'ADMIN' && (
                  <button 
                    className="action-btn" 
                    title="Add Member"
                    onClick={() => {
                      setSelectedProject(project);
                      setShowMemberModal(true);
                    }}
                  >
                    <UserPlus size={18} />
                  </button>
                )}
              </div>

              <div className="project-meta">
                <div className="member-avatars">
                  {project.members?.slice(0, 5).map((m, i) => (
                    <div key={i} className="member-avatar" title={m.name}>
                      {m.name?.charAt(0)}
                    </div>
                  ))}
                  {project.members?.length > 5 && (
                    <div className="member-avatar more">
                      +{project.members.length - 5}
                    </div>
                  )}
                  {(!project.members || project.members.length === 0) && (
                    <span className="text-secondary" style={{ fontSize: '0.8rem' }}>No members</span>
                  )}
                </div>

                {user.role === 'ADMIN' && (
                  <div className="project-actions">
                    <button 
                      className="action-btn delete" 
                      title="Delete"
                      onClick={() => handleDeleteProject(project.id)}
                    >
                      <Trash2 size={16} />
                    </button>
                  </div>
                )}
              </div>
            </div>
          ))
        ) : (
          <div className="empty-state glass-panel" style={{ gridColumn: '1 / -1' }}>
            <Users size={48} className="text-secondary" style={{ marginBottom: '16px' }} />
            <h3>No projects found</h3>
            <p>Get started by creating your first project.</p>
          </div>
        )}
      </div>
    </div>

    {/* Create Project Modal */}
    {showModal && (
        <div className="modal-overlay">
          <div className="modal-content glass-panel">
            <div className="modal-header">
              <h2>Create New Project</h2>
              <button onClick={() => setShowModal(false)} className="action-btn">
                <X size={20} />
              </button>
            </div>
            <form onSubmit={handleCreateProject} className="modal-form">
              <div className="form-group">
                <label>Project Name</label>
                <input 
                  type="text" 
                  value={newProject.name}
                  onChange={(e) => setNewProject({...newProject, name: e.target.value})}
                  placeholder="Enter project name"
                  required
                />
              </div>
              <div className="form-group">
                <label>Description</label>
                <textarea 
                  rows="4"
                  value={newProject.description}
                  onChange={(e) => setNewProject({...newProject, description: e.target.value})}
                  placeholder="What is this project about?"
                ></textarea>
              </div>
              <button type="submit" className="submit-btn" disabled={submitting}>
                {submitting ? 'Creating...' : 'Create Project'}
              </button>
            </form>
          </div>
        </div>
      )}

      {/* Add Member Modal */}
      {showMemberModal && (
        <div className="modal-overlay">
          <div className="modal-content glass-panel" style={{ maxWidth: '400px' }}>
            <div className="modal-header">
              <h2>Add Member</h2>
              <button onClick={() => setShowMemberModal(false)} className="action-btn">
                <X size={20} />
              </button>
            </div>
            <p className="text-secondary" style={{ marginBottom: '20px' }}>
              Assign a member to <strong>{selectedProject?.name}</strong>
            </p>
            <div className="member-list" style={{ maxHeight: '300px', overflowY: 'auto', display: 'flex', flexDirection: 'column', gap: '10px' }}>
              {allUsers.filter(u => !selectedProject?.members?.some(m => m.id === u.id)).length > 0 ? (
                allUsers
                  .filter(u => !selectedProject?.members?.some(m => m.id === u.id))
                  .map(userItem => (
                    <div key={userItem.id} className="member-item glass-panel" style={{ padding: '12px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                      <div>
                        <div style={{ fontWeight: 600 }}>{userItem.name}</div>
                        <div style={{ fontSize: '0.8rem', color: 'var(--text-secondary)' }}>{userItem.email}</div>
                      </div>
                      <button 
                        className="action-btn" 
                        onClick={() => handleAddMember(userItem.id)}
                        style={{ background: 'var(--primary)', color: 'white' }}
                      >
                        <Plus size={16} />
                      </button>
                    </div>
                  ))
              ) : (
                <div className="empty-state">No more users to add.</div>
              )}
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default Projects;
