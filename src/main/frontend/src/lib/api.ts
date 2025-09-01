import axios from "axios";

// Use same-origin API base path. During development, Vite proxy forwards '/api' to Spring Boot (see vite.config.ts).
const API_BASE = "/api";

const basicToken = btoa("admin:secret");

export const api = axios.create({
    baseURL: API_BASE,
    headers: {
        "Authorization": `Basic ${basicToken}`,
        "Content-Type": "application/json"
    },
});

export type Task = {
    id: number;
    title: string;
    description: string;
    status: "PENDING" | "IN_PROGRESS" | "COMPLETED" | "CANCELED";
    dueDate: string;
    createdAt: string;
    updatedAt: string;
}

export type CreateTask = {
    title: string;
    description?: string;
    dueDate?: string;
}

export type updateTaskStatus = Partial<CreateTask> & {
    status? : Task["status"];
}

export async function listTasks(params?: { status?: Task["status"]; dueBefore?: string}) {
    const res = await api.get("/tasks", { params });
    return res.data;
}

export async function createTask(body: CreateTask) {
    const res = await api.post("/tasks", body);
    return res.data;
}

export async function updateTaskStatus(id: number, status: Task["status"]) {
    const res = await api.put(`/tasks/${id}`, status);
    return res.data;
}

export async function deleteTask(id: number) {
    await api.delete(`/tasks/${id}`);
}