import { useState, type PropsWithChildren } from 'react';
import { Link, NavLink, useNavigate } from "react-router-dom";
import { getToken, getUserInfo, logoutServer } from "../lib/api";

export default function Layout({ children }: PropsWithChildren) {
    const [open, setOpen] = useState(false);
    const navigate = useNavigate();
    const token = getToken();
    const userInfo = getUserInfo();

//     // when display user's org was first created, it could be multiple orgs & memo saves value
//     const orgDisplay = useMemo(() => {
//         const orgs = userInfo?.organizations ?? [];
//         if (!orgs.length) return null;
//         if (orgs.length === 1) return orgs[0];
//         return `${orgs[0]} +${orgs.length - 1}`;
//     }, [userInfo]);

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
                                <span style={{ alignSelf: 'center', opacity: 0.9 }}>
                                    {userInfo?.username ? (
                                        <>
                                            Logged in as: <strong>{userInfo.username}</strong> at {userInfo.organizations}
                                        </>
                                    ) : null}
                                </span>
                                <NavLink to="/tasks" end className={({ isActive }) => isActive ? 'active' : ''}>Tasks</NavLink>
                                <NavLink to="/projects" end className={({ isActive }) => isActive ? 'active' : ''}>Projects</NavLink>
                                <NavLink to="/organizations" end className={({ isActive }) => isActive ? 'active' : ''}>Organizations</NavLink>
                                <NavLink to="/login" onClick={onLogout} className={({ isActive }) => isActive ? 'active' : ''}>Logout</NavLink>
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

