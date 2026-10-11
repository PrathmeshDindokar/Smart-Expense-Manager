import { useState } from "react";
import api from "../api/axiosConfig";
import "./Login.css";
import { useNavigate } from "react-router-dom";

function Login() {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);

  const handleLogin = async (event) => {
    event.preventDefault();
    setMessage("");
    setLoading(true);

    try {
      const response = await api.post("/users/login", {
        email: email.trim(),
        password: password,
      });

      const token =
        typeof response.data === "string"
          ? response.data
          : response.data.token;

      if (!token) {
        setMessage("Token not found in the response.");
        return;
      }

      localStorage.setItem("token", token);
        navigate("/dashboard");
    } catch (error) {
      setMessage(
  `Error: ${error.message} | Status: ${error.response?.status ?? "No response"}`
);
    } finally {
      setLoading(false);
    }
  };

  return (
  <div className="login-page">
    <div className="login-brand">
      <div className="brand-logo">₹</div>

      <h1>SmartExpense</h1>

      <p>
        Take control of your money.
        <br />
        Build better financial habits.
      </p>

      <div className="brand-footer">
        Smart money. Better future.
      </div>
    </div>

    <div className="login-section">
      <form className="login-card" onSubmit={handleLogin}>
        <div className="login-heading">
          <span className="welcome-label">WELCOME BACK</span>

          <h2>Sign in to your account</h2>

          <p>Enter your details to continue.</p>
        </div>

        <div className="form-group">
          <label htmlFor="email">Email address</label>

          <input
            id="email"
            type="email"
            placeholder="you@example.com"
            value={email}
            onChange={(event) => setEmail(event.target.value)}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="password">Password</label>

          <input
            id="password"
            type="password"
            placeholder="Enter your password"
            value={password}
            onChange={(event) => setPassword(event.target.value)}
            required
          />
        </div>

        <button
          className="login-button"
          type="submit"
          disabled={loading}
        >
          {loading ? "Signing in..." : "Sign In"}
        </button>

        {message && (
          <p className="login-message" role="status">
            {message}
          </p>
        )}

        <div className="login-security">
          🔒 Your financial information stays protected.
        </div>
      </form>
    </div>
  </div>
);
}

export default Login;