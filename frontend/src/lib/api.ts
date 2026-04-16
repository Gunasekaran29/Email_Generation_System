// ─────────────────────────────────────────────────────────────────────────────
//  Spring Boot API Client
// ─────────────────────────────────────────────────────────────────────────────

const BASE = import.meta.env.VITE_API_URL ?? "http://localhost:8080/api";

async function req<T>(path: string, init: RequestInit = {}): Promise<T> {
  const res = await fetch(`${BASE}${path}`, {
    headers: { "Content-Type": "application/json", ...init.headers },
    ...init,
  });

  if (!res.ok) {
    const err = await res.json().catch(() => ({ error: res.statusText }));
    throw new Error((err as any).error ?? "API error");
  }

  if (res.status === 204) return undefined as T;

  return res.json();
}

// ── Types ─────────────────────────────────────────────────────────────────────

export interface EmailDTO {
  id: string;
  sender: string;
  senderEmail: string;
  recipients: string[];
  subject: string;
  body: string;
  preview: string;
  status: "inbox" | "sent" | "trash";
  isRead: boolean;
  isStarred: boolean;
  avatar: string;
  createdAt: string;
}

export interface SendPayload {
  to: string;
  subject: string;
  body: string;
  senderName?: string;
}

// ── API ───────────────────────────────────────────────────────────────────────

export const emailApi = {
  getAll: () => req<EmailDTO[]>("/emails"),
  getByStatus: (s: string) => req<EmailDTO[]>(`/emails?status=${s}`),
  getStarred: () => req<EmailDTO[]>("/emails?starred=true"),
  search: (q: string) =>
    req<EmailDTO[]>(`/emails?search=${encodeURIComponent(q)}`),
  getById: (id: string) => req<EmailDTO>(`/emails/${id}`),
  unreadCount: () => req<{ count: number }>("/emails/unread-count"),

  send: (payload: SendPayload) =>
    req<EmailDTO>("/emails/send", {
      method: "POST",
      body: JSON.stringify(payload),
    }),

  markRead: (id: string) =>
    req<EmailDTO>(`/emails/${id}/read`, { method: "PATCH" }),

  toggleStar: (id: string) =>
    req<EmailDTO>(`/emails/${id}/star`, { method: "PATCH" }),

  trash: (id: string) =>
    req<EmailDTO>(`/emails/${id}/trash`, { method: "PATCH" }),

  delete: (id: string) =>
    req<void>(`/emails/${id}`, { method: "DELETE" }),
};