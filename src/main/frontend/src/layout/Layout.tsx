import { useState, type PropsWithChildren } from 'react';
import { Link, NavLink} from "react-router-dom";

export default function Layout({ children }: PropsWithChildren) {
    const [open, setOpen] = useState(false);

    return (
        <div className="site">
            <header className="site-header">
                <div className="nav-container">
                    <div className="brand">
                        <Link to="/" className="brand-link">MGD Spring Boot React</Link>
                    </div>

                    <button
                        className="hamburger"
                        aria-label="Toggle navigation"
                        aria-expanded={open}
                        onClick={() => setOpen(o => !o)}
                    >
                        <span className="bar" />
                        <span className="bar" />
                        <span className="bar" />
                    </button>

                    <nav className={"nav-links " + (open ? "open" : "")}
                         onClick={() => setOpen(false)}>
                        <NavLink to="/" end className={({ isActive }) => isActive ? 'active' : ''}>Tasks</NavLink>
                        <NavLink to="/contact" className={({ isActive }) => isActive ? 'active' : ''}>Contact</NavLink>
                    </nav>
                </div>
            </header>

            <main className="site-main">
                {children}
            </main>

            <footer className="site-footer">
                <div className="footer-content">
                    <span>© {new Date().getFullYear()} MGD React</span>
                </div>
            </footer>
        </div>
    );
}

