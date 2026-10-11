import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Login from "./pages/Login";
import "./App.css";
import { useEffect, useState } from "react";
import api from "./api/axiosConfig";
import Transactions from "./pages/Transactions";
import Budget from "./pages/Budget";
import AIInsights from "./pages/AIInsights";

function Dashboard() {
  const [dashboardData, setDashboardData] = useState(null);
const [error, setError] = useState("");

useEffect(() => {
  api.get("/dashboard")
    .then((response) => {
      setDashboardData(response.data);
    })
    .catch(() => {
      setError("Dashboard data load zala nahi.");
    });
}, []);
if (error) {
  return <p>{error}</p>;
}

if (!dashboardData) {
  return <p>Loading dashboard...</p>;
}

const handleLogout = () => {
  localStorage.removeItem("token");
  window.location.href = "/";
};
  return (
    <div className="app-container">
      <aside className="sidebar">
        <h2>💰 SmartExpense</h2>
        <nav>
          <a href="/dashboard">Dashboard</a>
          <a href="/transactions">Transactions</a>
          <a href="/budget">Budget</a>
          <a href="/insights">AI Insights</a>
          <a href="/settings">Settings</a>
        </nav>
        <button
  type="button"
  onClick={handleLogout}
  className="logout-button"
>
  Logout
</button>
      </aside>

      <main className="main-content">
        <h1>Dashboard</h1>
        <p>Welcome to your Smart Expense Manager</p>

        <div className="dashboard-cards">
          <div className="card">
            <h3>Total Balance</h3>
            <h2>{"\u20B9"}{dashboardData.balance}</h2>
          </div>

          <div className="card">
            <h3>Total Income</h3>
            <h2>{"\u20B9"}{dashboardData.totalIncome}</h2>
          </div>

          <div className="card">
            <h3>Total Expenses</h3>
            <h2>{"\u20B9"}{dashboardData.totalExpense}</h2>
          </div>
        </div>

        <div className="card">
          <h3>AI Financial Insights</h3>
          <p>Get intelligent insights about your spending habits.</p>
          <button onClick={() => window.location.href = "/insights"}>
            Generate AI Insights
          </button>
        </div>
      </main>
    </div>
  );
}

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login />} />

        <Route
          path="/dashboard"
          element={
            localStorage.getItem("token") ? (
              <Dashboard />
            ) : (
              <Navigate to="/" replace />
            )
          }
        />

        <Route
          path="/transactions"
          element={
            localStorage.getItem("token") ? (
              <Transactions />
            ) : (
              <Navigate to="/" replace />
            )
          }
        />

        <Route
  path="/budget"
  element={
    localStorage.getItem("token") ? (
      <Budget />
    ) : (
      <Navigate to="/" replace />
    )
  }
/>

<Route
  path="/insights"
  element={
    localStorage.getItem("token") ? (
      <AIInsights />
    ) : (
      <Navigate to="/" replace />
    )
  }
/>

        <Route
          path="*"
          element={<Navigate to="/" replace />}
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;