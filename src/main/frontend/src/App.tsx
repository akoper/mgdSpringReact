import { Routes, Route } from "react-router-dom";
import Layout from './layout/Layout.tsx';
import Tasks from './pages/Tasks.tsx';
import Contact from './pages/Contact.tsx';

export default function App() {
    return (
        <Layout>
            <Routes>
                <Route path="/" element={<Tasks />} />
                <Route path="/contact" element={<Contact />} />
                <Route path="*" element={<div className="page"><h1>Not Found</h1></div> } />
            </Routes>
        </Layout>
    )
}