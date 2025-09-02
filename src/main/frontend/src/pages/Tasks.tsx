import { useEffect, useState } from "react";
import {
    listTasks,
    createTask,
    updateTaskStatus,
    deleteTask,
    type Task
} from "../lib/api";

function Tasks() {
    const [tasks, setTasks] = useState<Task[]>([]);
    const [title, setTitle] = useState("");
    const [description, setDescription] = useState("");

    async function refresh() {
        const tasks = await listTasks();
        setTasks(tasks);
    }

    useEffect(() => {
        refresh();
    }, []);

    async function onCreate(e: React.FormEvent) {
        e.preventDefault();
        await createTask({ title: title.trim(), description: description || undefined });
        setTitle("");
        setDescription("");
        await refresh();
    }

    async function onToggleStatus(t: Task) {
        await updateTaskStatus(t.id, t.status === "COMPLETED" ? "PENDING" : "COMPLETED");
        await refresh();
    }

    async function onDelete(id: number) {
        await deleteTask(id);
        await refresh();
    }

    return (
        <div className="page task-form">
            <h1>Tasks</h1>

            <div>

            <form onSubmit={onCreate} style={{ marginBottom: 16 }}>
                <label>
                <label htmlFor="title">Title:</label>
                    <input
                        type="text"
                        id="title"
                        placeholder="Title"
                        value={title}
                        onChange={e => setTitle(e.target.value)}
                    />
                </label>
                <label htmlFor="description">Description:</label>
                    <input
                        type="text"
                        id="description"
                        placeholder="Description (optional)"
                        value={description}
                        onChange={e => setDescription(e.target.value)}
                    />
                    <button type="submit">Create</button>
            </form>

            </div>

            <ul>
                {tasks.map(t => (
                    <li key={t.id} style={{ display: "flex", gap: 8, alignItems: "center", marginBottom: 8 }}>
                        <input type="checkbox" checked={t.status === "COMPLETED"} onChange={() => onToggleStatus(t)} />
                        <div>
                            <div><strong>{t.title}</strong></div>
                            {t.description && <div style={{ color: "#666" }}>{t.description}</div>}
                        </div>
                        <button className="button task-delete" onClick={() => onDelete(t.id)}>Delete</button>
                    </li>
                ))}
            </ul>

        </div>
    );
}

export default Tasks;