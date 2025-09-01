import { useState } from 'react';

export default function Contact() {
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [message, setMessage] = useState("");
    const [status, setStatus] = useState<string | null>(null);

    function onSubmit(e: React.FormEvent) {
        e.preventDefault();
        setStatus('thanks - be back to you soon!');
        setName('');
        setEmail('');
        setMessage('');
    }

    return (
        <div className="page page-contact">
            <h1>Contact Us</h1>

            <form className="contact-form" onSubmit={onSubmit}>
                <label>
                    Name: &nbsp;
                    <input type="text" placeholder="Name" value={name} onChange={e => setName(e.target.value)} required/>
                </label>
                <label>
                    Email: &nbsp;
                <input type="email" placeholder="Email" value={email} onChange={e => setEmail(e.target.value)} required/>
                </label>
                <label>
                    Message: &nbsp;
                    <textarea placeholder="Message" value={message} onChange={e => setMessage(e.target.value)} required/>
                </label>
                <button className="button" type="submit">Send</button>
                {status && <p className="form-status" role="status">{status}</p>}
            </form>
        </div>
    )

}