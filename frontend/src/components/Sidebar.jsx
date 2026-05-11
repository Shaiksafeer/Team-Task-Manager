import React from 'react';
import { NavLink } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { 
  LayoutDashboard, 
  FolderKanban, 
  CheckSquare, 
  LogOut,
  ChevronLeft,
  ChevronRight
} from 'lucide-react';
import './Sidebar.css';

const Sidebar = ({ isOpen, toggleSidebar }) => {
  const { user, logout } = useAuth();

  const navItems = [
    { name: 'Dashboard', path: '/dashboard', icon: <LayoutDashboard size={20} /> },
    { name: 'Projects', path: '/projects', icon: <FolderKanban size={20} /> },
    { name: 'Tasks', path: '/tasks', icon: <CheckSquare size={20} /> },
  ];

  return (
    <aside className={`sidebar glass-panel ${isOpen ? 'open' : 'closed'}`}>
      <div className="sidebar-header">
        <div className="logo-container">
          <div className="logo-icon">T</div>
          {isOpen && <span className="logo-text">Taskify</span>}
        </div>
        <button className="toggle-btn" onClick={toggleSidebar}>
          {isOpen ? <ChevronLeft size={18} /> : <ChevronRight size={18} />}
        </button>
      </div>

      <nav className="sidebar-nav">
        {navItems.map((item) => (
          <NavLink 
            key={item.path} 
            to={item.path} 
            className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}
          >
            <span className="nav-icon">{item.icon}</span>
            {isOpen && <span className="nav-name">{item.name}</span>}
          </NavLink>
        ))}
      </nav>

      <div className="sidebar-footer">
        {isOpen && (
          <div className="user-info">
            <div className="user-avatar">
              {user?.name?.charAt(0) || 'U'}
            </div>
            <div className="user-details">
              <span className="user-name">{user?.name}</span>
              <span className="user-role">{user?.role}</span>
            </div>
          </div>
        )}
        <button className="logout-btn" onClick={logout} title="Logout">
          <LogOut size={20} />
          {isOpen && <span>Logout</span>}
        </button>
      </div>
    </aside>
  );
};

export default Sidebar;
