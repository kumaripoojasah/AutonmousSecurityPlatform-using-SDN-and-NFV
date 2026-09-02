import React, { useState } from 'react';
import DashboardView from './views/DashboardView';
import AuthFlow from './components/Authentication/AuthFlow';

export default function App() {
    // On refresh, check if a token is already stored so the user isn't
    // sent back to the login screen every time.
    const [authData, setAuthData] = useState(() => {
        const token = localStorage.getItem('sdn_token');
        const role = localStorage.getItem('sdn_role');
        const username = localStorage.getItem('sdn_username');
        return token ? { token, role, username } : null;
    });

    const handleAuthenticated = (data) => {
        // AuthFlow -> LoginPage already stores token/role in localStorage.
        // Also keep username around for display in the dashboard header.
        localStorage.setItem('sdn_username', data.username ?? '');
        setAuthData(data);
    };

    const handleLogout = () => {
        localStorage.removeItem('sdn_token');
        localStorage.removeItem('sdn_role');
        localStorage.removeItem('sdn_username');
        setAuthData(null);
    };

    return (
        <div style={{ backgroundColor: '#0a0e1a', minHeight: '100vh', margin: 0, padding: 0 }}>
            {authData ? (
                <DashboardView role={authData.role} onLogout={handleLogout} />
            ) : (
                <AuthFlow onAuthenticated={handleAuthenticated} />
            )}
        </div>
    );
}