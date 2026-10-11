import { useEffect, useState } from "react";
import api from "../api/axiosConfig";
import "./Transactions.css";



function Transactions() {
  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [search, setSearch] = useState("");
  const [showForm, setShowForm] = useState(false);
const [saving, setSaving] = useState(false);

const [formData, setFormData] = useState({
  categoryId: "",
  amount: "",
  type: "EXPENSE",
  description: "",
  transactionDate: new Date().toISOString().slice(0, 10),
});

const [categories, setCategories] = useState([]);

  const fetchTransactions = async () => {
    try {
      setLoading(true);
      const response = await api.get("/transactions");
      setTransactions(
        Array.isArray(response.data)
          ? response.data
          : response.data.content || []
      );
      setError("");
    } catch {
      setError(" Don't Load Transactions . Please try again.");
    } finally {
      setLoading(false);
    }
  };

const handleFormChange = (event) => {
  const { name, value } = event.target;

  setFormData((previous) => ({
    ...previous,
    [name]: value,
  }));
};

const handleAddTransaction = async (event) => {
  event.preventDefault();

  if (!formData.categoryId || Number(formData.amount) <= 0) {
    setError("Please select a category and enter a valid amount.");
    return;
  }

  try {
    setSaving(true);
    setError("");

    await api.post("/transactions", {
      ...formData,
      categoryId: Number(formData.categoryId),
      amount: Number(formData.amount),
    });

    setShowForm(false);
    setFormData({
      categoryId: "",
      amount: "",
      type: "EXPENSE",
      description: "",
      transactionDate: new Date().toISOString().slice(0, 10),
    });

    await fetchTransactions();
  } catch (error) {
    setError(
      error.response?.data?.message ||
        "Don't Load Transactions . Please try again."
    );
  } finally {
    setSaving(false);
  }
};
  useEffect(() => {
  const loadData = async () => {
    try {
      const [transactionResponse, categoryResponse] = await Promise.all([
        api.get("/transactions"),
        api.get("/categories"),
      ]);

      setTransactions(
        Array.isArray(transactionResponse.data)
          ? transactionResponse.data
          : transactionResponse.data.content || []
      );

      setCategories(
        Array.isArray(categoryResponse.data)
          ? categoryResponse.data
          : []
      );

      setError("");
    } catch {
      setError("Data load zala nahi. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  loadData();
}, []);

  const filteredTransactions = transactions.filter((transaction) => {
    const text = `${transaction.description || ""} ${
      transaction.category || ""
    } ${transaction.type || ""}`.toLowerCase();

    return text.includes(search.toLowerCase());
  });

  const formatAmount = (amount) =>
    `₹${Number(amount || 0).toLocaleString("en-IN")}`;

  const handleEdit = async (transaction) => {
  const newAmount = window.prompt(
    "Enter updated amount:",
    String(transaction.amount ?? "")
  );

  if (newAmount === null) return;

  if (!newAmount.trim() || Number(newAmount) <= 0) {
    window.alert("Please enter a valid amount.");
    return;
  }

  const newDescription = window.prompt(
    "Enter updated description:",
    transaction.description || ""
  );

  if (newDescription === null) return;

  try {
    await api.put(`/transactions/${transaction.id}`, {
      categoryId:
        typeof transaction.category === "object"
          ? transaction.category?.id
          : transaction.categoryId,
      amount: Number(newAmount),
      type: transaction.type,
      description: newDescription,
      transactionDate:
        transaction.transactionDate || transaction.date,
    });

    window.alert("Transaction updated successfully.");
    await fetchTransactions();
  } catch (error) {
    window.alert(
      error.response?.data?.message ||
        "Could not update transaction. Please try again."
    );
  }
};

const handleDelete = async (id) => {
  if (!id) {
    window.alert("Transaction ID is missing.");
    return;
  }

  const confirmed = window.confirm(
    "Are you sure you want to delete this transaction?"
  );

  if (!confirmed) return;

  try {
    await api.delete(`/transactions/${id}`);
    window.alert("Transaction deleted successfully.");
    await fetchTransactions();
  } catch (error) {
    window.alert(
      error.response?.data?.message ||
        "Could not delete transaction. Please try again."
    );
  }
};

  return (
    <div className="transactions-page">
      {showForm && (
  <section className="add-transaction-form-card">
    <div className="add-form-heading">
      <div>
        <h2>Add New Transaction</h2>
        <p>Enter your income or expense details below.</p>
      </div>

      <button
        type="button"
        className="close-form-button"
        onClick={() => setShowForm(false)}
        aria-label="Close form"
      >
        ×
      </button>
    </div>

    <form onSubmit={handleAddTransaction} className="add-transaction-form">
      <div className="form-field">
        <label htmlFor="transactionType">Transaction Type</label>
        <select
          id="transactionType"
          name="type"
          value={formData.type}
          onChange={handleFormChange}
          required
        >
          <option value="EXPENSE">Expense</option>
          <option value="INCOME">Income</option>
        </select>
      </div>

      <div className="form-field">
        <label htmlFor="transactionAmount">Amount (₹)</label>
        <input
          id="transactionAmount"
          type="number"
          name="amount"
          min="0.01"
          step="0.01"
          placeholder="Enter amount"
          value={formData.amount}
          onChange={handleFormChange}
          required
        />
      </div>

      <div className="form-field">
        <label htmlFor="transactionCategory">Category</label>
        <select
          id="transactionCategory"
          name="categoryId"
          value={formData.categoryId}
          onChange={handleFormChange}
          required
        >
          <option value="">Select category</option>
          {categories.map((category) => (
            <option key={category.id} value={category.id}>
              {category.name}
            </option>
          ))}
        </select>
      </div>

      <div className="form-field">
        <label htmlFor="transactionDate">Date</label>
        <input
          id="transactionDate"
          type="date"
          name="transactionDate"
          value={formData.transactionDate}
          onChange={handleFormChange}
          required
        />
      </div>

      <div className="form-field form-field-full">
        <label htmlFor="transactionDescription">Description</label>
        <input
          id="transactionDescription"
          type="text"
          name="description"
          placeholder="e.g. Monthly groceries"
          value={formData.description}
          onChange={handleFormChange}
        />
      </div>

      <div className="transaction-form-actions">
        <button
          type="button"
          className="cancel-transaction-button"
          onClick={() => setShowForm(false)}
        >
          Cancel
        </button>

        <button
          type="submit"
          className="save-transaction-button"
          disabled={saving}
        >
          {saving ? "Saving..." : "Save Transaction"}
        </button>
      </div>
    </form>
  </section>
)}
      <header className="transactions-header">
        <div>
          <h1>Transactions</h1>
          <p>Manage and track your income and expenses.</p>
        </div>

            <button
  className="add-transaction-button"
  onClick={() => setShowForm(!showForm)}
>
  + Add Transaction
</button>  
            
        <button className="refresh-button" onClick={fetchTransactions}>
          ↻ Refresh
        </button>
      </header>

      <section className="transactions-summary">
        <div className="transaction-summary-card">
          <span>Total Transactions</span>
          <h2>{transactions.length}</h2>
        </div>

        <div className="transaction-summary-card">
          <span>Total Income</span>
          <h2 className="income-text">
            {formatAmount(
              transactions
                .filter((t) => String(t.type).toUpperCase() === "INCOME")
                .reduce((sum, t) => sum + Number(t.amount || 0), 0)
            )}
          </h2>
        </div>

        <div className="transaction-summary-card">
          <span>Total Expenses</span>
          <h2 className="expense-text">
            {formatAmount(
              transactions
                .filter((t) => String(t.type).toUpperCase() === "EXPENSE")
                .reduce((sum, t) => sum + Number(t.amount || 0), 0)
            )}
          </h2>
        </div>
      </section>

      <section className="transactions-table-card">
        <div className="transactions-toolbar">
          <h2>All Transactions</h2>

          <input
            type="search"
            placeholder="Search transactions..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            aria-label="Search transactions"
          />
        </div>

        {loading ? (
          <p className="transactions-message">Loading transactions...</p>
        ) : error ? (
          <p className="transactions-error">{error}</p>
        ) : filteredTransactions.length === 0 ? (
          <p className="transactions-message">No transactions found.</p>
        ) : (
          <div className="transactions-table-wrapper">
            <table className="transactions-table">
              <thead>
                <tr>
                  <th>Description</th>
                  <th>Category</th>
                  <th>Date</th>
                  <th>Type</th>
                  <th>Amount</th>
                  <th>Actions</th>
                </tr>
              </thead>

              <tbody>
                {filteredTransactions.map((transaction, index) => (
                  <tr key={transaction.id ?? index}>
                    <td>{transaction.description || "—"}</td>
                    <td>
                      {typeof transaction.category === "object"
                        ? transaction.category?.name || "—"
                        : transaction.category || "—"}
                    </td>
                    <td>
                      {transaction.date
                        ? String(transaction.date).slice(0, 10)
                        : transaction.transactionDate
                          ? String(transaction.transactionDate).slice(0, 10)
                          : "—"}
                    </td>
                    <td>
                      <span
                        className={`transaction-type ${
                          String(transaction.type).toUpperCase() === "INCOME"
                            ? "type-income"
                            : "type-expense"
                        }`}
                      >
                        {transaction.type || "—"}
                      </span>
                    </td>
                    <td className="transaction-amount">
                      {formatAmount(transaction.amount)}
                    </td>
                    <td className="transaction-actions">
  <button
    type="button"
    onClick={() => handleEdit(transaction)}
    className="edit-transaction-button"
  >
    Edit
  </button>

  <button
    type="button"
    onClick={() => handleDelete(transaction.id)}
    className="delete-transaction-button"
  >
    Delete
  </button>
</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </section>
    </div>
  );
}

export default Transactions;