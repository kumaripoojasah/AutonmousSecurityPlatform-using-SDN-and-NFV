import { useState } from "react";
import LoginPage from "./LoginPage";
import SignupPage from "./SignupPage";
import ForgotPasswordPage from "./ForgotPasswordPage";

// Drop this component in wherever you currently render <LoginPage />.
// It swaps between the three auth screens with plain state — no router
// needed. If you add react-router later, replace the `view` state with
// three <Route> entries instead.
export default function AuthFlow({ onAuthenticated }) {
  const [view, setView] = useState("login"); // "login" | "signup" | "forgot"

  if (view === "signup") {
    return (
      <SignupPage
        onNavigateToLogin={() => setView("login")}
        onSignupSuccess={() => setView("login")}
      />
    );
  }

  if (view === "forgot") {
    return <ForgotPasswordPage onNavigateToLogin={() => setView("login")} />;
  }

  return (
    <LoginPage
      onLoginSuccess={onAuthenticated}
      onNavigateToSignup={() => setView("signup")}
      onNavigateToForgotPassword={() => setView("forgot")}
    />
  );
}