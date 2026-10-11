import { useEffect, useState } from "react";
import api from "../api/axiosConfig";
import "./Budget.css";

function Budget() {
  const [budget, setBudget] = useState(null);
  const [amount, setAmount] = useState("");
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState("");

  const fetchBudget = async () => {
    try {
      const response = await api.get("/budget");
      setBudget(response.data);
      setAmount(
        response.data?.monthlyBudget !== undefined
          ? String(response.data.monthlyBudget)
          : ""
      );
      setMessage("");
    } catch {
      setBudget(null);
    } finally {
      setLoading(false);
    }
  };

 useEffect(() => {
  const loadBudget = async () => {
    try {
      const response = await api.get("/budget");
      setBudget(response.data);
      setAmount(
        response.data?.amount !== undefined
          ? String(response.data.amount)
          : ""
      );
    } catch {
      setBudget(null);
    } finally {
      setLoading(false);
    }
  };

  loadBudget();
}, []);

  const handleSubmit = async (event) => {
    event.preventDefault();

    if (!amount || Number(amount) <= 0) {
      setMessage("Please enter a valid budget amount.");
      return;
    }

    try {
      await api.post("/budget", {
        monthlyBudget : Number(amount)
      });
      setMessage("Budget saved successfully.");
      await fetchBudget();
    } catch (error) {
      setMessage(
        error.response?.data?.message ||
          "Could not save budget. Please check the budget API."
      );
    }
  };

  return (
    <div className="budget-page">
      <header className="budget-header">
        <h1>Monthly Budget</h1>
        <p>Plan your spending and keep your finances on track.</p>
      </header>

      <section className="budget-card">
        <h2>Set Your Budget</h2>
        <p className="budget-description">
          Enter the total amount you want to spend this month.
        </p>

        {loading ? (
          <p>Loading budget...</p>
        ) : (
          <form onSubmit={handleSubmit}>
            <label htmlFor="budgetAmount">Monthly budget (₹)</label>

            <input
              id="budgetAmount"
              type="number"
              min="1"
              step="0.01"
              placeholder="e.g. 15000"
              value={amount}
              onChange={(event) => setAmount(event.target.value)}
              required
            />

            <button type="submit">Save Budget</button>
          </form>
        )}

        {budget && (
          <div className="current-budget">
            <span>Current Budget</span>
            <strong>
              ₹{Number(budget.monthlyBudget || 0).toLocaleString("en-IN")}
            </strong>
          </div>
        )}

        {message && <p className="budget-message">{message}</p>}
      </section>
    </div>
  );
}

export default Budget;