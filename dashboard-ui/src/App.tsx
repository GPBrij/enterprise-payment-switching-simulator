import { useEffect, useState } from 'react'
import axios from 'axios'
import { Bar, Doughnut } from 'react-chartjs-2'
import {
  Chart as ChartJS,
  ArcElement,
  BarElement,
  CategoryScale,
  LinearScale,
  Legend,
  Tooltip,
} from 'chart.js'
import './App.css'

ChartJS.register(ArcElement, BarElement, CategoryScale, LinearScale, Legend, Tooltip)

const API = 'http://localhost:8081/api/v1/operations'

type Summary = {
  totalTransactions: number
  approvedTransactions: number
  declinedTransactions: number
  approvedAmount: number
  fraudAlerts: number
  settlementBatches: number
  settledAmount: number
  approvalRate: number
}

type IssuerMetric = { issuerBank: string; transactionCount: number; grossAmount: number }
type FraudMetric = { ruleCode: string; reason: string; alertCount: number; highestRiskScore: number }
type SettlementMetric = { batchReference: string; status: string; currency: string; transactionCount: number; grossAmount: number }

function App() {
  const [summary, setSummary] = useState<Summary | null>(null)
  const [issuers, setIssuers] = useState<IssuerMetric[]>([])
  const [fraud, setFraud] = useState<FraudMetric[]>([])
  const [settlements, setSettlements] = useState<SettlementMetric[]>([])
  const [error, setError] = useState('')

  const load = async () => {
    try {
      setError('')
      const [s, i, f, st] = await Promise.all([
        axios.get(`${API}/summary`),
        axios.get(`${API}/issuers`),
        axios.get(`${API}/fraud`),
        axios.get(`${API}/settlements`),
      ])
      setSummary(s.data)
      setIssuers(i.data)
      setFraud(f.data)
      setSettlements(st.data)
    } catch {
      setError('Dashboard data could not be loaded. Confirm the switch API is running on port 8081.')
    }
  }

  useEffect(() => { void load() }, [])

  const money = (value: number) => new Intl.NumberFormat('en-ZA', {
    style: 'currency', currency: 'ZAR'
  }).format(value ?? 0)

  if (!summary) return <main className="shell"><h1>EPSS Operations</h1><p>{error || 'Loading dashboard...'}</p></main>

  const statusData = {
    labels: ['Approved', 'Declined'],
    datasets: [{ data: [summary.approvedTransactions, summary.declinedTransactions], backgroundColor: ['#22c55e', '#ef4444'] }]
  }
  const issuerData = {
    labels: issuers.map(x => x.issuerBank),
    datasets: [{ label: 'Transactions', data: issuers.map(x => x.transactionCount), backgroundColor: '#38bdf8' }]
  }

  return (
    <main className="shell">
      <header className="hero">
        <div><span className="eyebrow">ENTERPRISE PAYMENT SWITCHING SIMULATOR</span><h1>Operations Dashboard</h1><p>Management reporting across switching, fraud and settlement.</p></div>
        <button onClick={() => void load()}>Refresh</button>
      </header>
      {error && <div className="alert alert-danger">{error}</div>}

      <section className="cards">
        <article><span>Total transactions</span><strong>{summary.totalTransactions}</strong></article>
        <article><span>Approval rate</span><strong>{summary.approvalRate}%</strong></article>
        <article><span>Approved value</span><strong>{money(summary.approvedAmount)}</strong></article>
        <article><span>Fraud alerts</span><strong>{summary.fraudAlerts}</strong></article>
        <article><span>Settlement batches</span><strong>{summary.settlementBatches}</strong></article>
        <article><span>Settled value</span><strong>{money(summary.settledAmount)}</strong></article>
      </section>

      <section className="charts">
        <article className="panel"><h2>Authorization outcomes</h2><Doughnut data={statusData} /></article>
        <article className="panel"><h2>Transactions by issuer</h2><Bar data={issuerData} options={{ responsive: true }} /></article>
      </section>

      <section className="tables">
        <article className="panel"><h2>Fraud controls</h2><div className="table-responsive"><table className="table table-dark table-striped"><thead><tr><th>Rule</th><th>Reason</th><th>Alerts</th><th>Risk</th></tr></thead><tbody>{fraud.map(x => <tr key={x.ruleCode}><td>{x.ruleCode}</td><td>{x.reason}</td><td>{x.alertCount}</td><td>{x.highestRiskScore}</td></tr>)}</tbody></table></div></article>
        <article className="panel"><h2>Recent settlement batches</h2><div className="table-responsive"><table className="table table-dark table-striped"><thead><tr><th>Reference</th><th>Status</th><th>Transactions</th><th>Amount</th></tr></thead><tbody>{settlements.map(x => <tr key={x.batchReference}><td>{x.batchReference}</td><td>{x.status}</td><td>{x.transactionCount}</td><td>{money(x.grossAmount)}</td></tr>)}</tbody></table></div></article>
      </section>
    </main>
  )
}

export default App
