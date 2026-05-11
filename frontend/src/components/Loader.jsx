import React from 'react';
import './Loader.css';

const Loader = () => {
  return (
    <div className="loader-container">
      <div className="premium-loader">
        <div className="loader-ring"></div>
        <div className="loader-ring"></div>
        <div className="loader-ring"></div>
        <span className="loader-text">Loading...</span>
      </div>
    </div>
  );
};

export default Loader;
