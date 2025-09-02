import { Routes, Route } from "react-router-dom";
import Layout from './layout/Layout.tsx';
import Tasks from './pages/Tasks.tsx';
import Contact from './pages/Contact.tsx';
import Login from './pages/Login.tsx';
import Register from './pages/Register.tsx';
import Home from './pages/Home.tsx';
import Organizations from './pages/Organizations.tsx';

export default function App() {
    return (
        <Layout>
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/tasks" element={<Tasks />} />
                <Route path="/organizations" element={<Organizations />} />
                <Route path="/contact" element={<Contact />} />
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
                <Route path="*" element={<div className="page"><h1>Not Found</h1></div> } />
            </Routes>
        </Layout>
    )
}