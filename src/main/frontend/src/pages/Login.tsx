import { FormEvent, useState } from 'react';
import { login } from "../lib/api";
import { useNavigate, Link } from "react-router-dom";

export default function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  async function onSubmit(e: FormEvent) {
    e.preventDefault();
    setError(null);
    try {
      await login(username, password);
      navigate("/");
    } catch (e: any) {
      setError(e?.response?.data || "Login failed");
    }
  }

  return (
    <div className="page auth-form">
      <h1>Login</h1>
      <form onSubmit={onSubmit}>
        <div>
        <label for="username">Username:</label>
            <input type="text" id="username" value={username} onChange={e => setUsername(e.target.value)} placeholder="Username" required />
        </div>
        <div>
        <label for="password">Password:</label>
            <input type="password" id="password" value={password} onChange={e => setPassword(e.target.value)} placeholder="Password" required />
        </div>
        <button type="submit">Login</button>
        {error && <p role="alert" style={{color:'crimson'}}>{error}</p>}
      </form>
      <p>Don't have an account? <Link to="/register">Register</Link></p>
    </div>
  );
}
