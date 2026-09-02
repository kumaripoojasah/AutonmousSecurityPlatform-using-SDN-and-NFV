import { useState } from "react";
import "./LoginPage.css";

// Self-signup only offers non-privileged roles.
// ADMIN accounts should be granted manually, never chosen by the user
// signing up, otherwise anyone can register themselves as an admin.
const SIGNUP_ROLES = [
  { value: "VIEWER", label: "Viewer — read-only dashboards" },
  { value: "ANALYST", label: "Analyst — investigate & respond" },
];

export default function SignupPage({ onSignupSuccess, onNavigateToLogin }) {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [role, setRole] = useState(SIGNUP_ROLES[0].value);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSignup = async (e) => {
    e.preventDefault();
    setError("");

    if (!username.trim() || !email.trim() || !password || !confirmPassword) {
      setError("All fields are required.");
      return;
    }
    if (password.length < 8) {
      setError("Password must be at least 8 characters.");
      return;
    }
    if (password !== confirmPassword) {
      setError("Passwords do not match.");
      return;
    }

    setLoading(true);
    try {
      // Replace with your real backend endpoint, e.g. POST /api/auth/signup
      const res = await fetch("http://localhost:9090/api/auth/signup", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, email, password, role }),
      });

      if (res.status === 409) {
        setError("That username is already taken.");
        setLoading(false);
        return;
      }
      if (!res.ok) {
        setError("Could not create the account. Try again.");
        setLoading(false);
        return;
      }

      const data = await res.json();
      onSignupSuccess?.(data);
    } catch (err) {
      setError("Could not reach the server. Is the backend running?");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-page">
      <form className="auth-card" onSubmit={handleSignup}>
        <div className="auth-header">
          <span className="auth-eyebrow">New account</span>
          <h1 className="auth-title">Register access</h1>
        </div>

        <label className="auth-field">
          <span className="auth-label">Username</span>
          <input
            type="text"
            placeholder="jane.doe"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            autoComplete="username"
          />
        </label>

        <label className="auth-field">
          <span className="auth-label">Email</span>
          <input
            type="email"
            placeholder="jane.doe@example.com"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            autoComplete="email"
          />
        </label>

        <label className="auth-field">
          <span className="auth-label">Password</span>
          <input
            type="password"
            placeholder="••••••••"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            autoComplete="new-password"
          />
        </label>

        <label className="auth-field">
          <span className="auth-label">Confirm password</span>
          <input
            type="password"
            placeholder="••••••••"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
            autoComplete="new-password"
          />
        </label>

        <label className="auth-field">
          <span className="auth-label">Role</span>
          <select value={role} onChange={(e) => setRole(e.target.value)}>
            {SIGNUP_ROLES.map((r) => (
              <option key={r.value} value={r.value}>
                {r.label}
              </option>
            ))}
          </select>
        </label>
        <div className="auth-hint">
          Admin access is granted separately by an existing administrator.
        </div>

        {error && <div className="auth-error">{error}</div>}

        <button type="submit" className="auth-submit" disabled={loading}>
          {loading ? "Creating account..." : "Create account"}
        </button>

        <div className="auth-links">
          <button type="button" className="auth-link" onClick={onNavigateToLogin}>
            Already have an account? Log in
          </button>
        </div>
      </form>
    </div>
  );
}