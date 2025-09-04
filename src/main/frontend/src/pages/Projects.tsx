import { FormEvent, useEffect, useState } from 'react';
import { getToken } from '../lib/api';

type Project = {
  id: number;
  name: string;
  description?: string;
};

export default function Projects() {
  const [items, setItems] = useState<Project[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [creating, setCreating] = useState(false);
  const [createError, setCreateError] = useState<string | null>(null);

  async function load() {
    try {
      setLoading(true);
      const token = getToken();
      const res = await fetch('/api/projects', {
        headers: token ? { Authorization: `Bearer ${token}` } : {}
      });
      if (!res.ok) throw new Error(`Failed to load: ${res.status}`);
      const data = await res.json();
      setItems(data);
      setError(null);
    } catch (e: any) {
      setError(e.message);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    load();
  }, []);

  async function onCreate(e: FormEvent) {
    e.preventDefault();
    setCreateError(null);
    if (!name.trim()) {
      setCreateError('Name is required');
      return;
    }
    try {
      setCreating(true);
      const token = getToken();
      const res = await fetch('/api/projects', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          ...(token ? { Authorization: `Bearer ${token}` } : {})
        },
        body: JSON.stringify({ name: name.trim(), description: description.trim() || undefined })
      });
      if (!res.ok) {
        const text = await res.text();
        throw new Error(text || `Failed to create: ${res.status}`);
      }
      const created = await res.json();
      setItems(prev => [created, ...prev]);
      setName('');
      setDescription('');
    } catch (e: any) {
      setCreateError(e.message);
    } finally {
      setCreating(false);
    }
  }

  return (
    <div className="page">

      <h1>Projects</h1>

      <form onSubmit={onCreate} style={{ margin: '1rem 0', display: 'grid', gap: '0.5rem', maxWidth: 480 }}>
        <div>
          <label>
            <span style={{ display: 'block', fontWeight: 600 }}>Name</span>
            <input type="text" value={name} onChange={e => setName(e.target.value)} placeholder="Project name" required />
          </label>
        </div>
        <div>
          <label>
            <span style={{ display: 'block', fontWeight: 600 }}>Description</span>
            <textarea value={description} onChange={e => setDescription(e.target.value)} placeholder="Optional description" rows={3} />
          </label>
        </div>
        {createError && <p className="error">{createError}</p>}
        <div>
          <button type="submit" disabled={creating}>{creating ? 'Creating...' : 'Create Project'}</button>
        </div>
      </form>

      {loading && <p>Loading...</p>}
      {error && <p className="error">{error}</p>}
      {!loading && !error && (
        <ul>
          {items.map(p => (
            <li key={p.id}>
              <strong>{p.name}</strong>
              {p.description ? ` — ${p.description}` : ''}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
