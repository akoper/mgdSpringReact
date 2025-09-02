import axios from "axios";

// Use same-origin API base path. During development, Vite proxy forwards '/api' to Spring Boot (see vite.config.ts).
const API_BASE = "/api";

const TOKEN_KEY = "auth_token";
const USER_INFO_KEY = "auth_user_info";

export function getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
}
export function setToken(token: string | null) {
    if (token) localStorage.setItem(TOKEN_KEY, token); else localStorage.removeItem(TOKEN_KEY);
}

export type AuthUserInfo = {
    username: string;
    roles?: string;
    organizations?: string[];
}

export function setUserInfo(info: AuthUserInfo | null) {
    if (info) localStorage.setItem(USER_INFO_KEY, JSON.stringify(info));
    else localStorage.removeItem(USER_INFO_KEY);
}
export function getUserInfo(): AuthUserInfo | null {
    const raw = localStorage.getItem(USER_INFO_KEY);
    if (!raw) return null;
    try { return JSON.parse(raw) as AuthUserInfo; } catch { return null; }
}

export function logout() { setToken(null); setUserInfo(null); }

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

export type AuthResponse = { token: string; username: string; roles: string; organizations?: string[] };

export async function register(username: string, password: string) {
    const res = await api.post<AuthResponse>(`/auth/register`, { username, password });
    const data = res.data;
    if (data?.token) setToken(data.token);
    if (data?.username) setUserInfo({ username: data.username, roles: data.roles, organizations: data.organizations ?? [] });
    return data;
}

export async function login(username: string, password: string) {
    const res = await api.post<AuthResponse>(`/auth/login`, { username, password });
    const data = res.data;
    if (data?.token) setToken(data.token);
    if (data?.username) setUserInfo({ username: data.username, roles: data.roles, organizations: data.organizations ?? [] });
    return data;
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
    const res = await api.post<Organization>(`/organizations`, { name });
    const org = res.data;
    // Optimistically update local user info to include the new organization
    const info = getUserInfo();
    if (info) {
        const orgs = Array.from(new Set([...(info.organizations ?? []), org.name]));
        setUserInfo({ ...info, organizations: orgs });
    }
    return org;
}

export async function joinOrganization(id: number): Promise<{ joined: boolean; organizationId: number }> {
    const res = await api.post(`/organizations/${id}/join`);
    return res.data;
}