import { useState } from "react";
import api from "../api/axiosConfig";
import "./AIInsights.css"; 
import ReactMarkdown from "react-markdown";

function AIInsights() {
  const [insight, setInsight] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const generateInsights = async () => {
    setLoading(true);
    setError("");
    setInsight("");

    try {
      const response = await api.get("/ai/insight");

      const data = response.data;

      setInsight(
        typeof data === "string"
          ? data
          : data.insight || data.message || JSON.stringify(data, null, 2)
      );
    } catch (err) {
      setError(
        err.response?.data?.message ||
          "Don't worry, AI insights failed to load. Backend and API key need to be checked."
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="ai-insights-page">
      <header className="ai-insights-header">
        <div className="ai-icon">✦</div>
        <div>
          <h1>AI Financial Insights</h1>
          <p>
            Understand your spending habits and make smarter financial
            decisions.
          </p>
        </div>
      </header>

      <section className="ai-insights-card">
        <div className="ai-card-heading">
          <span className="ai-badge">AI POWERED</span>
          <h2>Your Personal Financial Assistant</h2>
          <p>
            Generate personalized insights based on your financial
            transactions.
          </p>
        </div>

        <button
          className="generate-insights-button"
          onClick={generateInsights}
          disabled={loading}
        >
          {loading ? "Generating Insights..." : "✦ Generate AI Insights"}
        </button>

        {loading && (
          <div className="ai-loading">
            <div className="ai-spinner"></div>
            <p>Analyzing your financial data...</p>
          </div>
        )}

        {error && <div className="ai-error">{error}</div>}

        {insight && (
          <div className="ai-result">
            <h3>✨ Your Financial Insights</h3>
            <div className="ai-result-content">
              <ReactMarkdown>{insight}</ReactMarkdown>
            </div>
          </div>
        )}
      </section>

      <div className="ai-disclaimer">
        <span>ℹ️</span>
        <p>
          AI-generated suggestions are for informational purposes.
          Review your financial decisions carefully.
        </p>
      </div>
    </div>
  );
}

export default AIInsights;