import { FormEvent, useState } from 'react';
import { register } from "../lib/api";
import { useNavigate, Link } from "react-router-dom";

export default function Register() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  async function onSubmit(e: FormEvent) {
    e.preventDefault();
    setError(null);
    try {
      await register(username, password);
      navigate("/");
    } catch (e: any) {
      setError(e?.response?.data || "Register failed");
    }
  }

  return (
    <div className="page auth-form">
      <h1>Register</h1>
      <form onSubmit={onSubmit}>
        <div>
          <input value={username} onChange={e => setUsername(e.target.value)} placeholder="Username" required />
        </div>
        <div>
          <input type="password" value={password} onChange={e => setPassword(e.target.value)} placeholder="Password" required />
        </div>
        <button type="submit">Register</button>
        {error && <p role="alert" style={{color:'crimson'}}>{error}</p>}
      </form>
      <p>Already have an account? <Link to="/login">Login</Link></p>
    </div>
  );
}
