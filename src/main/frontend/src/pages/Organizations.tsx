import { FormEvent, useState } from 'react';
import { createOrganization, joinOrganization } from '../lib/api';

export default function Organizations() {
    const [createName, setCreateName] = useState("");
    const [joinId, setJoinId] = useState("");
    const [message, setMessage] = useState<string | null>(null);

    async function onCreate(e: FormEvent) {
        e.preventDefault();
        setMessage(null);
        try {
            const org = await createOrganization(createName);
            setMessage(`Created organization "${org.name}" with ID ${org.id}. You are now a member.`);
            setCreateName("");
        } catch (err: any) {
            const status = err?.response?.status;
            if (status === 409) setMessage("Organization with this name already exists.");
            else if (status === 401) setMessage("Please login to create an organization.");
            else setMessage("Failed to create organization.");
        }
    }

    async function onJoin(e: FormEvent) {
        e.preventDefault();
        setMessage(null);
        const id = Number(joinId);
        if (!id || id <= 0) { setMessage("Enter a valid organization ID."); return; }
        try {
            const res = await joinOrganization(id);
            if (res.joined) setMessage(`Joined organization with ID ${res.organizationId}.`);
        } catch (err: any) {
            const status = err?.response?.status;
            if (status === 404) setMessage("Organization not found.");
            else if (status === 401) setMessage("Please login to join an organization.");
            else setMessage("Failed to join organization.");
        }
    }

    return (
        <div className="container">
            <h1>Organizations</h1>
            {message && <div className="alert">{message}</div>}

            <section>
                <h2>Create Organization</h2>
                <form onSubmit={onCreate} className="form">
                    <label>
                        Name
                        <input type="text" value={createName} onChange={e => setCreateName(e.target.value)} placeholder="Organization name" required />
                    </label>
                    <button type="submit">Create</button>
                </form>
            </section>

            <section style={{ marginTop: 24 }}>
                <h2>Join Organization</h2>
                <form onSubmit={onJoin} className="form">
                    <label>
                        Organization ID
                        <input type="text" value={joinId} onChange={e => setJoinId(e.target.value)} placeholder="e.g., 1" />
                    </label>
                    <button type="submit">Join</button>
                </form>
            </section>
        </div>
    );
}
