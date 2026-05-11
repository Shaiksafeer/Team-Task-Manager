import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import MainLayout from '../layouts/MainLayout';
import ProtectedRoute from '../components/ProtectedRoute';

// Page Imports
import Login from '../pages/Login';
import Signup from '../pages/Signup';
import Dashboard from '../pages/Dashboard';
import Projects from '../pages/Projects';
import Tasks from '../pages/Tasks';

const AppRoutes = () => {
  return (
    <Routes>
      {/* Public Routes: Accessible to everyone */}
      <Route path="/login" element={<Login />} />
      <Route path="/signup" element={<Signup />} />

      {/* Protected Routes: Token required */}
      <Route element={<ProtectedRoute />}>
        <Route
          path="/dashboard"
          element={
            <MainLayout>
              <Dashboard />
            </MainLayout>
          }
        />
        <Route
          path="/projects"
          element={
            <MainLayout>
              <Projects />
            </MainLayout>
          }
        />
        <Route
          path="/tasks"
          element={
            <MainLayout>
              <Tasks />
            </MainLayout>
          }
        />
      </Route>

      {/* Default Redirection */}
      <Route path="/" element={<Navigate to="/dashboard" />} />
      <Route path="*" element={<Navigate to="/dashboard" />} />
    </Routes>
  );
};

export default AppRoutes;
