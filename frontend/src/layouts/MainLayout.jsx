import React, { useState } from 'react';
import { Outlet } from 'react-router-dom';
import Sidebar from '../components/Sidebar';
import './MainLayout.css';

const MainLayout = () => {
  const [sidebarOpen, setSidebarOpen] = useState(true);

  const toggleSidebar = () => {
    setSidebarOpen(!sidebarOpen);
  };

  return (
    <div className="main-layout">
      <Sidebar isOpen={sidebarOpen} toggleSidebar={toggleSidebar} />
      <main className={`content-area ${sidebarOpen ? 'expanded' : 'collapsed'}`}>
        <div className="content-container fade-in">
          <Outlet />
        </div>
      </main>
    </div>
  );
};

export default MainLayout;
