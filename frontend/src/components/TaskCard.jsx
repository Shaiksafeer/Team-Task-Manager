import React from 'react';
import '../styles/Cards.css';

const TaskCard = ({ task }) => {
  const getPriorityClass = (priority) => {
    switch (priority?.toLowerCase()) {
      case 'high': return 'priority-high';
      case 'medium': return 'priority-medium';
      case 'low': return 'priority-low';
      default: return '';
    }
  };

  return (
    <div className="card task-card">
      <div className={`priority-tag ${getPriorityClass(task.priority)}`}>
        {task.priority}
      </div>
      <h4>{task.title}</h4>
      <p>{task.description}</p>
      <div className="card-footer">
        <span>Due: {task.dueDate || 'N/A'}</span>
        <span className="status">{task.status}</span>
      </div>
    </div>
  );
};

export default TaskCard;
