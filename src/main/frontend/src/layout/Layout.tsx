import { useState, type PropsWithChildren } from 'react';
import { Link, NavLink, useNavigate } from "react-router-dom";
import { getToken, logoutServer } from "../lib/api";

export default function Layout({ children }: PropsWithChildren) {
    const [open, setOpen] = useState(false);
    const navigate = useNavigate();
    const token = getToken();

    async function onLogout() {
        await logoutServer();
        navigate("/login");
    }

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


                        {token ? (
                            <>
                                <NavLink to="/tasks" end className={({ isActive }) => isActive ? 'active' : ''}>Tasks</NavLink>
                                <NavLink onClick={onLogout} className={({ isActive }) => isActive ? 'active' : ''}>Logout</NavLink>
                            </>
                        ) : (
                            <>
                                <NavLink to="/login" className={({ isActive }) => isActive ? 'active' : ''}>Login</NavLink>
                                <NavLink to="/register" className={({ isActive }) => isActive ? 'active' : ''}>Register</NavLink>
                            </>
                        )}

                    </nav>
                </div>
            </header>

            <main className="site-main">
                {children}
            </main>

            <footer className="site-footer">
                <div className="footer-content">
                    <span>© {new Date().getFullYear()} MGD React  &nbsp; | &nbsp;
                    <NavLink to="/contact" className={({ isActive }) => isActive ? 'active' : ''}>Contact Us</NavLink></span>
                </div>
            </footer>
        </div>
    );
}

