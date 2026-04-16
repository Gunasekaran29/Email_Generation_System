import { useState } from "react";
import { Mail } from "lucide-react";
import { useEmails } from "@/hooks/useEmails";
import { EmailSidebar } from "@/components/email/EmailSidebar";
import { EmailList } from "@/components/email/EmailList";
import { EmailDetail } from "@/components/email/EmailDetail";
import { ComposeModal } from "@/components/email/ComposeModal";
import { EmailDTO } from "@/lib/api";

const Index = () => {
  const {
    emails, selectedEmail, selectedId,
    view, searchQuery, unreadCount, loading,
    setView, setSearchQuery, selectEmail,
    fetchEmails, toggleStar, markAsRead,
    moveToTrash, deleteEmail,
  } = useEmails();

  const [composeOpen, setComposeOpen] = useState(false);
  const [replyTo, setReplyTo]         = useState("");
  const [replySubject, setReplySubject] = useState("");

  const handleDelete = async (id: string) => {
    const email = emails.find(e => e.id === id);
    if (!email) return;
    // Already in trash → permanent delete; otherwise → move to trash
    if (email.status === "trash") await deleteEmail(id);
    else await moveToTrash(id);
  };

  const handleReply = (email: EmailDTO) => {
    setReplyTo(email.senderEmail);
    setReplySubject("Re: " + email.subject);
    setComposeOpen(true);
  };

  return (
    <div className="flex flex-col h-screen w-full bg-background">
      {/* Header */}
      <header className="flex items-center gap-3 px-4 py-2 border-b border-border bg-card">
        <Mail className="h-6 w-6 text-primary" />
        <span className="text-xl font-medium tracking-tight">Mail</span>
      </header>

      <div className="flex flex-1 overflow-hidden">
        {/* Sidebar */}
        <EmailSidebar
          activeView={view}
          onViewChange={(v) => { setView(v); selectEmail(null); }}
          unreadCount={unreadCount}
          onCompose={() => {
            setReplyTo(""); setReplySubject(""); setComposeOpen(true);
          }}
        />

        {/* Main */}
        <main className="flex flex-1 min-w-0 overflow-hidden">
          {/* List panel */}
          <div className="w-full md:w-80 lg:w-96 border-r border-border flex flex-col">
            <EmailList
              emails={emails}
              selectedId={selectedId}
              view={view}
              searchQuery={searchQuery}
              loading={loading}
              onSearch={setSearchQuery}
              onSelect={(id) => {
                selectEmail(id);
                const e = emails.find(e => e.id === id);
                if (e && !e.isRead) markAsRead(id);
              }}
              onToggleStar={toggleStar}
            />
          </div>

          {/* Detail panel */}
          <div className="flex-1 overflow-hidden">
            {selectedEmail ? (
              <EmailDetail
                email={selectedEmail}
                onBack={() => selectEmail(null)}
                onToggleStar={() => toggleStar(selectedEmail.id)}
                onDelete={handleDelete}
                onReply={handleReply}
              />
            ) : (
              <div className="h-full flex flex-col items-center justify-center text-muted-foreground gap-3">
                <Mail className="h-16 w-16 opacity-20" />
                <p className="text-sm">Select an email to read</p>
              </div>
            )}
          </div>
        </main>
      </div>

      <ComposeModal
        isOpen={composeOpen}
        onClose={() => setComposeOpen(false)}
        onSuccess={fetchEmails}
        initialTo={replyTo}
        initialSubject={replySubject}
      />
    </div>
  );
};

export default Index;
