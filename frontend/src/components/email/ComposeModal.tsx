import { useState, useEffect } from "react";
import { X, Minus, Maximize2, Send, Loader2 } from "lucide-react";
import { emailApi } from "@/lib/api";
import { cn } from "@/lib/utils";

interface Props {
  isOpen: boolean;
  onClose: () => void;
  onSuccess?: () => void;
  initialTo?: string;
  initialSubject?: string;
}

export function ComposeModal({ isOpen, onClose, onSuccess, initialTo = "", initialSubject = "" }: Props) {
  const [to,      setTo]      = useState("");
  const [subject, setSubject] = useState("");
  const [body,    setBody]    = useState("");
  const [minimized, setMinimized] = useState(false);
  const [sending,   setSending]   = useState(false);
  const [error,     setError]     = useState("");

  useEffect(() => {
    if (isOpen) {
      setTo(initialTo);
      setSubject(initialSubject);
      setError("");
    }
  }, [initialTo, initialSubject, isOpen]);

  if (!isOpen) return null;

  const handleSend = async () => {
    if (!to.trim() || !subject.trim()) {
      setError("To and Subject are required.");
      return;
    }

    setSending(true);
    setError("");

    try {
      /**
       * POST /api/emails/send
       * Spring Boot receives this, calls JavaMailSender.send()
       * which connects to Gmail SMTP (or any SMTP) and delivers the real email.
       * Then saves to DB with status="sent".
       */
      await emailApi.send({ to, subject, body, senderName: "Me" });

      setTo(""); setSubject(""); setBody("");
      onSuccess?.();
      onClose();
    } catch (err: any) {
      setError(err.message ?? "Failed to send. Check backend logs.");
    } finally {
      setSending(false);
    }
  };

  return (
    <div
      className={cn(
        "fixed bottom-0 right-6 bg-card border border-border rounded-t-xl shadow-2xl z-50 flex flex-col",
        minimized ? "w-72 h-10" : "w-[520px] h-[430px]"
      )}
    >
      {/* Header */}
      <div className="flex items-center justify-between bg-foreground text-background px-3 py-2 rounded-t-xl">
        <span className="text-sm font-medium">New Message</span>
        <div className="flex items-center gap-2">
          <button onClick={() => setMinimized(m => !m)} className="hover:opacity-70 transition-opacity">
            <Minus size={14} />
          </button>
          <button onClick={() => setMinimized(false)} className="hover:opacity-70 transition-opacity">
            <Maximize2 size={14} />
          </button>
          <button onClick={onClose} className="hover:opacity-70 transition-opacity">
            <X size={14} />
          </button>
        </div>
      </div>

      {!minimized && (
        <>
          <input
            value={to}
            onChange={e => setTo(e.target.value)}
            placeholder="To"
            className="w-full px-3 py-2 text-sm border-b border-border bg-transparent outline-none placeholder:text-muted-foreground"
          />
          <input
            value={subject}
            onChange={e => setSubject(e.target.value)}
            placeholder="Subject"
            className="w-full px-3 py-2 text-sm border-b border-border bg-transparent outline-none placeholder:text-muted-foreground"
          />
          <textarea
            value={body}
            onChange={e => setBody(e.target.value)}
            placeholder="Write your message…"
            className="flex-1 w-full px-3 py-2 text-sm bg-transparent outline-none resize-none placeholder:text-muted-foreground"
          />

          {error && (
            <p className="text-xs text-destructive px-3 pb-1">{error}</p>
          )}

          <div className="flex items-center justify-between px-3 py-2 border-t border-border">
            <button
              onClick={handleSend}
              disabled={sending}
              className="flex items-center gap-2 bg-primary text-primary-foreground text-sm px-4 py-2 rounded-full disabled:opacity-60 hover:opacity-90 transition font-medium"
            >
              {sending
                ? <><Loader2 size={14} className="animate-spin" /> Sending…</>
                : <><Send size={14} /> Send</>
              }
            </button>
            <span className="text-xs text-muted-foreground">via Spring Boot SMTP</span>
          </div>
        </>
      )}
    </div>
  );
}
