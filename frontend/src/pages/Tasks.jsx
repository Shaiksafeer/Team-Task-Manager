import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import taskService from '../services/taskService';
import projectService from '../services/projectService';
import Loader from '../components/Loader';
import { 
  Plus, 
  Search, 
  Filter, 
  Calendar,
  CheckCircle2,
  Clock,
  AlertCircle,
  TrendingUp,
  X
} from 'lucide-react';
import './Tasks.css';

const Tasks = () => {
  const { user } = useAuth();
  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [updatingTask, setUpdatingTask] = useState(null);
  const [loadingMembers, setLoadingMembers] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [projects, setProjects] = useState([]);
  const [projectMembers, setProjectMembers] = useState([]);
  const [newTask, setNewTask] = useState({
    title: '',
    description: '',
    priority: 'MEDIUM',
    status: 'TODO',
    dueDate: '',
    projectId: '',
    assignedToId: ''
  });

  useEffect(() => {
    fetchTasks();
    if (user.role === 'ADMIN') {
      fetchProjects();
    }
  }, []);

  const fetchTasks = async (showLoader = true) => {
    if (showLoader) setLoading(true);
    try {
      const data = await taskService.getAllTasks();
      setTasks(data);
    } catch (err) {
      console.error('Failed to fetch tasks');
    } finally {
      setLoading(false);
    }
  };

  const fetchProjects = async () => {
    try {
      const data = await projectService.getAllProjects();
      setProjects(data);
    } catch (err) {
      console.error('Failed to fetch projects');
    }
  };

  const handleProjectChange = async (projectId) => {
    setNewTask({ ...newTask, projectId, assignedToId: '' });
    if (projectId) {
      setLoadingMembers(true);
      try {
        const members = await projectService.getProjectMembers(projectId);
        setProjectMembers(members);
      } catch (err) {
        console.error('Failed to fetch members');
      } finally {
        setLoadingMembers(false);
      }
    } else {
      setProjectMembers([]);
    }
  };

  const handleCreateTask = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    
    // Ensure dueDate is in ISO format with seconds for LocalDateTime
    let formattedDate = newTask.dueDate;
    if (formattedDate && formattedDate.length === 16) {
        formattedDate += ':00';
    }

    const payload = {
        ...newTask,
        dueDate: formattedDate,
        projectId: parseInt(newTask.projectId),
        assignedToId: newTask.assignedToId ? parseInt(newTask.assignedToId) : null
    };

    try {
      await taskService.createTask(payload);
      setNewTask({
        title: '',
        description: '',
        priority: 'MEDIUM',
        status: 'TODO',
        dueDate: '',
        projectId: '',
        assignedToId: ''
      });
      setShowModal(false);
      await fetchTasks(false); // Refresh tasks without showing full screen loader
    } catch (err) {
      console.error('Failed to create task:', err.response?.data || err.message);
      alert(err.response?.data?.message || 'Failed to create task. Check if due date is in the future.');
    } finally {
      setSubmitting(false);
    }
  };

  const handleStatusUpdate = async (id, newStatus) => {
    setUpdatingTask(id);
    try {
      await taskService.updateTaskStatus(id, newStatus);
      await fetchTasks(false);
    } catch (err) {
      console.error('Failed to update status');
      alert('Failed to update task status. Please try again.');
    } finally {
      setUpdatingTask(null);
    }
  };

  if (loading) return <Loader />;

  return (
    <>
    <div className="tasks-container fade-in">
      <header className="page-header">
        <div className="header-info">
          <h1>Tasks</h1>
          <p className="text-secondary">Track and manage your team's workload.</p>
        </div>
        {user.role === 'ADMIN' && (
          <button className="create-btn" onClick={() => setShowModal(true)}>
            <Plus size={20} />
            <span>Add Task</span>
          </button>
        )}
      </header>

      <div className="tasks-table-container glass-panel">
        <table className="tasks-table">
          <thead>
            <tr>
              <th>Task Name</th>
              <th>Status</th>
              <th>Priority</th>
              <th>Assignee</th>
              <th>Due Date</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {tasks.length > 0 ? (
              tasks.map(task => (
                <tr key={task.id} className={updatingTask === task.id ? 'row-updating' : ''}>
                  <td>
                    <div className="task-title-cell">
                      <span className="task-project-name">{task.projectName}</span>
                      <span style={{ fontWeight: 600 }}>{task.title}</span>
                    </div>
                  </td>
                  <td>
                    <span className={`status-badge ${task.status.toLowerCase()}`}>
                      {updatingTask === task.id ? 'Updating...' : task.status.replace('_', ' ')}
                    </span>
                  </td>
                  <td>
                    <div className="priority-indicator">
                      <span className={`priority-dot ${task.priority.toLowerCase()}`}></span>
                      <span>{task.priority}</span>
                    </div>
                  </td>
                  <td>
                    <div className="assignee-cell">
                      <div className="assignee-avatar">
                        {task.assignedTo?.name?.charAt(0) || '?'}
                      </div>
                      <span style={{ fontSize: '0.9rem' }}>{task.assignedTo?.name || 'Unassigned'}</span>
                    </div>
                  </td>
                  <td>
                    <div className="priority-indicator text-secondary">
                      <Calendar size={14} />
                      <span style={{ fontSize: '0.85rem' }}>
                        {task.dueDate ? new Date(task.dueDate).toLocaleString() : 'No date'}
                      </span>
                    </div>
                  </td>
                  <td>
                    <select 
                      className="status-select"
                      value={task.status}
                      onChange={(e) => handleStatusUpdate(task.id, e.target.value)}
                      disabled={updatingTask === task.id}
                    >
                      <option value="TODO">To Do</option>
                      <option value="IN_PROGRESS">In Progress</option>
                      <option value="DONE">Done</option>
                    </select>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="6" className="empty-state">
                  <AlertCircle size={48} className="text-secondary" style={{ marginBottom: '16px' }} />
                  <h3>No tasks found</h3>
                  <p>Either you have no tasks or everything is completed!</p>
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>

    {showModal && (
        <div className="modal-overlay">
          <div className="modal-content glass-panel">
            <div className="modal-header">
              <h2>Create New Task</h2>
              <button onClick={() => setShowModal(false)} className="action-btn" disabled={submitting}>
                <X size={20} />
              </button>
            </div>
            <form onSubmit={handleCreateTask} className="modal-form">
              <div className="form-group">
                <label>Task Title</label>
                <input 
                  type="text" 
                  value={newTask.title}
                  onChange={(e) => setNewTask({...newTask, title: e.target.value})}
                  placeholder="What needs to be done?"
                  required
                  disabled={submitting}
                />
              </div>
              
              <div className="form-row">
                <div className="form-group">
                  <label>Project</label>
                  <select 
                    value={newTask.projectId}
                    onChange={(e) => handleProjectChange(e.target.value)}
                    required
                    disabled={submitting}
                  >
                    <option value="">Select Project</option>
                    {projects.map(p => <option key={p.id} value={p.id}>{p.name}</option>)}
                  </select>
                </div>
                <div className="form-group">
                  <label>Assign To {loadingMembers && <span className="loading-text">(Loading members...)</span>}</label>
                  <select 
                    value={newTask.assignedToId}
                    onChange={(e) => setNewTask({...newTask, assignedToId: e.target.value})}
                    disabled={!newTask.projectId || submitting || loadingMembers}
                  >
                    <option value="">Select Member</option>
                    {projectMembers.map(m => (
                      <option key={m.id} value={m.id}>{m.name}</option>
                    ))}
                  </select>
                </div>
              </div>

              <div className="form-row">
                <div className="form-group">
                  <label>Priority</label>
                  <select 
                    value={newTask.priority}
                    onChange={(e) => setNewTask({...newTask, priority: e.target.value})}
                    disabled={submitting}
                  >
                    <option value="LOW">Low</option>
                    <option value="MEDIUM">Medium</option>
                    <option value="HIGH">High</option>
                  </select>
                </div>
                <div className="form-group">
                  <label>Due Date & Time</label>
                  <input 
                    type="datetime-local" 
                    value={newTask.dueDate}
                    onChange={(e) => setNewTask({...newTask, dueDate: e.target.value})}
                    required
                    disabled={submitting}
                  />
                </div>
              </div>

              <div className="form-group">
                <label>Description</label>
                <textarea 
                  rows="3"
                  value={newTask.description}
                  onChange={(e) => setNewTask({...newTask, description: e.target.value})}
                  placeholder="Additional details..."
                  disabled={submitting}
                ></textarea>
              </div>

              <button type="submit" className="submit-btn" disabled={submitting}>
                {submitting ? 'Creating...' : 'Create Task'}
              </button>
            </form>
          </div>
        </div>
      )}
    </>
  );
};

export default Tasks;
