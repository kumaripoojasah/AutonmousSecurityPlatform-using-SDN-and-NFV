import { useEffect, useState } from "react";
import "./LoginPage.css";

// These lines print one by one, like the boot sequence in your screenshot.
// Swap the text for whatever fits your project's branding.
const BOOT_LINES = [
  "CYBER_SDN//OS v2.4.1",
  "LOADING SECURITY MODULES [OK]",
  "ESTABLISHING OPENFLOW CHANNEL...",
  "MININET TOPOLOGY DETECTED [6 NODES]",
  "VNF ORCHESTRATION ENGINE READY",
  "ACCESS TERMINAL READY.",
];

const LINE_DELAY_MS = 350; // time between each boot line appearing

export default function LoginPage({ onLoginSuccess, onNavigateToSignup, onNavigateToForgotPassword }) {
  const [visibleLines, setVisibleLines] = useState(0);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  // Reveal boot lines one at a time on mount
  useEffect(() => {
    if (visibleLines >= BOOT_LINES.length) return;
    const timer = setTimeout(() => {
      setVisibleLines((n) => n + 1);
    }, LINE_DELAY_MS);
    return () => clearTimeout(timer);
  }, [visibleLines]);

  const handleAuthenticate = async (e) => {
    e.preventDefault();
    setError("");

    if (!username.trim() || !password.trim()) {
      setError("Enter both username and password.");
      return;
    }

    setLoading(true);
    try {
      // Replace with your real backend endpoint, e.g. POST /api/auth/login
      const res = await fetch("http://localhost:9090/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password }),
      });

      if (!res.ok) {
        setError("Invalid username or password.");
        setLoading(false);
        return;
      }

      const data = await res.json();
      // Expecting something like { userId, username, role, token }
      localStorage.setItem("sdn_token", data.token);
      localStorage.setItem("sdn_role", data.role);

      onLoginSuccess?.(data);
    } catch (err) {
      setError("Could not reach the server. Is the backend running?");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-page">
      <div className="boot-log" aria-live="polite">
        {BOOT_LINES.slice(0, visibleLines).map((line, i) => (
          <div className="boot-line" key={i}>
            <span className="boot-arrow">&gt;</span> {line}
          </div>
        ))}
        {visibleLines >= BOOT_LINES.length && (
          <div className="boot-line boot-cursor">
            <span className="boot-arrow">&gt;</span>
          </div>
        )}
      </div>

      <form className="auth-card" onSubmit={handleAuthenticate}>
        <div className="auth-header">
          <span className="auth-eyebrow">Authentication required</span>
          <h1 className="auth-title">System access</h1>
        </div>

        <label className="auth-field">
          <span className="auth-label">Username</span>
          <input
            type="text"
            placeholder="admin"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            autoComplete="username"
          />
        </label>

        <label className="auth-field">
          <span className="auth-label">Password</span>
          <input
            type="password"
            placeholder="••••••••"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            autoComplete="current-password"
          />
        </label>

        {error && <div className="auth-error">{error}</div>}

        <button type="submit" className="auth-submit" disabled={loading}>
          {loading ? "Authenticating..." : "Authenticate"}
        </button>

        <div className="auth-links">
          <button
            type="button"
            className="auth-link"
            onClick={onNavigateToForgotPassword}
          >
            Forgot password?
          </button>
          <button
            type="button"
            className="auth-link"
            onClick={onNavigateToSignup}
          >
            New user? Sign up
          </button>
        </div>
      </form>
    </div>
  );
}