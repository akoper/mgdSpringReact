import { useState } from 'react';

export default function Home() {

    return (
        <div className="page page-home">
            <h1>Welcome to Web-based Project Management Application</h1>
            <div className="hero-image">
                <img src="officeworkers.jpg" />
            </div>
            <p>Web-based project management application is a powerful information tool to help you get more done.</p>

            <p>Create projects and tasks, assign tasks to users and they can mark them as completed when done.</p>
        </div>
    )

}