// src/views/DashboardView.jsx
import React from 'react';
import TrafficStream from '../components/traffic/TrafficStream';

export default function DashboardView() {
    return (
        <div style={{ padding: '30px', background: '#070a13', minHeight: '100vh' }}>
            <h1 style={{ color: '#e2e8f0', fontSize: '24px', marginBottom: '20px' }}>Security Operations Center (SOC)</h1>
            <TrafficStream />
        </div>
    );
}