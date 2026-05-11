import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import dashboardService from '../services/dashboardService';
import Loader from '../components/Loader';
import { 
  FolderKanban, 
  Clock, 
  CheckCircle2, 
  AlertCircle,
  TrendingUp
} from 'lucide-react';
import './Dashboard.css';

const Dashboard = () => {
  const { user } = useAuth();
  const [data, setData] = useState(null);
  const [overdueTasks, setOverdueTasks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        let dashboardData;
        if (user.role === 'ADMIN') {
          dashboardData = await dashboardService.getAdminDashboard();
        } else {
          dashboardData = await dashboardService.getMemberDashboard();
        }
        setData(dashboardData);

        const overdue = await dashboardService.getOverdueTasks();
        setOverdueTasks(overdue);
      } catch (err) {
        console.error('Dashboard fetch error:', err);
        setError('Failed to load dashboard data.');
      } finally {
        setLoading(false);
      }
    };

    if (user) {
      fetchDashboardData();
    }
  }, [user]);

  if (loading) return <Loader />;

  const stats = [
    { 
      label: 'Total Projects', 
      value: data?.totalProjects || data?.assignedProjects || 0, 
      icon: <FolderKanban size={24} />, 
      type: 'projects' 
    },
    { 
      label: 'Pending Tasks', 
      value: data?.pendingTasks || 0, 
      icon: <Clock size={24} />, 
      type: 'pending' 
    },
    { 
      label: 'Completed Tasks', 
      value: data?.completedTasks || 0, 
      icon: <CheckCircle2 size={24} />, 
      type: 'completed' 
    },
    { 
      label: 'Overdue Tasks', 
      value: overdueTasks.length, 
      icon: <AlertCircle size={24} />, 
      type: 'overdue' 
    }
  ];

  return (
    <div className="dashboard-container fade-in">
      <header className="welcome-section">
        <h1>Hello, {user.name} 👋</h1>
        <p>Here's what's happening with your projects today.</p>
      </header>

      {error && <div className="api-error">{error}</div>}

      <div className="stats-grid">
        {stats.map((stat, index) => (
          <div key={index} className="stat-card glass-panel">
            <div className={`stat-icon ${stat.type}`}>
              {stat.icon}
            </div>
            <div className="stat-info">
              <span className="stat-value">{stat.value}</span>
              <span className="stat-label">{stat.label}</span>
            </div>
          </div>
        ))}
      </div>

      <div className="dashboard-content">
        <div className="section-card glass-panel">
          <div className="section-header">
            <h2>Recent Activity</h2>
            <TrendingUp size={20} className="text-secondary" />
          </div>
          <div className="empty-state">
            <p>Activity feed coming soon...</p>
          </div>
        </div>

        <div className="section-card glass-panel">
          <div className="section-header">
            <h2>Overdue Tasks</h2>
            <AlertCircle size={20} className="text-accent" />
          </div>
          <div className="overdue-list">
            {overdueTasks.length > 0 ? (
              overdueTasks.map(task => (
                <div key={task.id} className="overdue-item">
                  <div className="overdue-info">
                    <h4>{task.title}</h4>
                    <span className="overdue-date">
                      Due: {new Date(task.dueDate).toLocaleString()}
                    </span>
                  </div>
                </div>
              ))
            ) : (
              <div className="empty-state">
                <p>No overdue tasks! Good job. 👏</p>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
