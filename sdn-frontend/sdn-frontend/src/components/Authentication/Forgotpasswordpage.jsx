import { useState } from "react";
import "./LoginPage.css";

export default function ForgotPasswordPage({ onNavigateToLogin }) {
  const [email, setEmail] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const [submitted, setSubmitted] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    if (!email.trim()) {
      setError("Enter the email tied to your account.");
      return;
    }

    setLoading(true);
    try {
      // Replace with your real backend endpoint, e.g. POST /api/auth/forgot-password
      await fetch("http://localhost:9090/api/auth/forgot-password", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email }),
      });

      // Always show the same confirmation regardless of whether the email
      // exists, so the form can't be used to find out which emails are
      // registered.
      setSubmitted(true);
    } catch (err) {
      setError("Could not reach the server. Is the backend running?");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-page">
      <form className="auth-card" onSubmit={handleSubmit}>
        <div className="auth-header">
          <span className="auth-eyebrow">Account recovery</span>
          <h1 className="auth-title">Reset password</h1>
        </div>

        {submitted ? (
          <div className="auth-success">
            If an account exists for that email, a reset link has been sent.
            Check your inbox to continue.
          </div>
        ) : (
          <>
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

            {error && <div className="auth-error">{error}</div>}

            <button type="submit" className="auth-submit" disabled={loading}>
              {loading ? "Sending..." : "Send reset link"}
            </button>
          </>
        )}

        <div className="auth-links">
          <button type="button" className="auth-link" onClick={onNavigateToLogin}>
            Back to login
          </button>
        </div>
      </form>
    </div>
  );
}