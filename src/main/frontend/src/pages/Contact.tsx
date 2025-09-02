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
                <label>Name: </label>
                <input type="text" placeholder="Name" className="width375" value={name} onChange={e => setName(e.target.value)} required/>

                <label>Email: </label>
                <input type="email" placeholder="Email" className="width375" value={email} onChange={e => setEmail(e.target.value)} required/>

                <label>Message:</label>
                <textarea placeholder="Message" className="width375" value={message} onChange={e => setMessage(e.target.value)} required/>

                <button className="button" type="submit">Send</button>
                {status && <p className="form-status" role="status">{status}</p>}
            </form>
        </div>
    )

}