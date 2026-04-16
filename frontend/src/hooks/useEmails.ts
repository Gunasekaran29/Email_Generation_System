import { useState, useEffect, useCallback } from "react";
import { emailApi, EmailDTO } from "@/lib/api";

export type MailboxView = "inbox" | "sent" | "trash" | "starred";

export const useEmails = () => {
  const [emails, setEmails]       = useState<EmailDTO[]>([]);
  const [selectedId, setSelectedId] = useState<string | null>(null);
  const [view, setView]           = useState<MailboxView>("inbox");
  const [searchQuery, setSearchQuery] = useState("");
  const [loading, setLoading]     = useState(true);

  // ── FETCH ────────────────────────────────────────────────────────────────
  const fetchEmails = useCallback(async () => {
    setLoading(true);
    try {
      let data: EmailDTO[];
      if (searchQuery.trim())       data = await emailApi.search(searchQuery);
      else if (view === "starred")  data = await emailApi.getStarred();
      else                          data = await emailApi.getByStatus(view);
      setEmails(data);
    } catch (err) {
      console.error("Fetch error:", err);
    } finally {
      setLoading(false);
    }
  }, [view, searchQuery]);

  useEffect(() => { fetchEmails(); }, [fetchEmails]);

  // ── ACTIONS ──────────────────────────────────────────────────────────────
  const markAsRead = async (id: string) => {
    setEmails(prev => prev.map(e => e.id === id ? { ...e, isRead: true } : e));
    await emailApi.markRead(id).catch(console.error);
  };

  const toggleStar = async (id: string) => {
    const email = emails.find(e => e.id === id);
    if (!email) return;
    setEmails(prev => prev.map(e => e.id === id ? { ...e, isStarred: !e.isStarred } : e));
    await emailApi.toggleStar(id).catch(console.error);
  };

  const moveToTrash = async (id: string) => {
    setEmails(prev => prev.filter(e => e.id !== id));
    setSelectedId(null);
    await emailApi.trash(id).catch(console.error);
  };

  const deleteEmail = async (id: string) => {
    setEmails(prev => prev.filter(e => e.id !== id));
    setSelectedId(null);
    await emailApi.delete(id).catch(console.error);
  };

  return {
    emails,
    selectedEmail: emails.find(e => e.id === selectedId),
    selectedId,
    view,
    searchQuery,
    loading,
    unreadCount: emails.filter(e => !e.isRead).length,
    setView,
    setSearchQuery,
    selectEmail: setSelectedId,
    fetchEmails,
    markAsRead,
    toggleStar,
    moveToTrash,
    deleteEmail,
  };
};
