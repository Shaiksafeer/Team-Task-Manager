import React from 'react';
import '../styles/Cards.css';

const ProjectCard = ({ project }) => {
  return (
    <div className="card project-card">
      <h3>{project.name}</h3>
      <p>{project.description}</p>
      <div className="card-footer">
        <span>Tasks: {project.taskCount || 0}</span>
        <button className="view-btn">View Details</button>
      </div>
    </div>
  );
};

export default ProjectCard;
