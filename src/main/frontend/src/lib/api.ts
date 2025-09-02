import axios from "axios";

// Use same-origin API base path. During development, Vite proxy forwards '/api' to Spring Boot (see vite.config.ts).
const API_BASE = "/api";

const TOKEN_KEY = "auth_token";

export function getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
}
export function setToken(token: string | null) {
    if (token) localStorage.setItem(TOKEN_KEY, token); else localStorage.removeItem(TOKEN_KEY);
}
export function logout() { setToken(null); }

export const api = axios.create({
    baseURL: API_BASE,
    headers: { "Content-Type": "application/json" },
});

// Attach token on each request
api.interceptors.request.use((config) => {
    const token = getToken();
    if (token) {
        config.headers = config.headers ?? {};
        (config.headers as any)["Authorization"] = `Bearer ${token}`;
    }
    return config;
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

export async function register(username: string, password: string) {
    const res = await api.post(`/auth/register`, { username, password });
    const token = res.data?.token as string | undefined;
    if (token) setToken(token);
    return res.data;
}

export async function login(username: string, password: string) {
    const res = await api.post(`/auth/login`, { username, password });
    const token = res.data?.token as string | undefined;
    if (token) setToken(token);
    return res.data;
}

export async function logoutServer() {
    try {
        await api.post(`/auth/logout`);
    } catch (e) {
        // ignore server errors for logout to ensure client clears token
    } finally {
        logout();
    }
}

// Organizations
export type Organization = { id: number; name: string; createdAt: string };

export async function createOrganization(name: string): Promise<Organization> {
    const res = await api.post(`/organizations`, { name });
    return res.data;
}

export async function joinOrganization(id: number): Promise<{ joined: boolean; organizationId: number }> {
    const res = await api.post(`/organizations/${id}/join`);
    return res.data;
}