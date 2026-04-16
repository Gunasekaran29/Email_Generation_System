import { Search, Loader2 } from "lucide-react";
import { EmailDTO } from "@/lib/api";
import { EmailListItem } from "./EmailListItem";
import { MailboxView } from "@/hooks/useEmails";

interface Props {
  emails: EmailDTO[];
  selectedId: string | null;
  view: MailboxView;
  searchQuery: string;
  loading: boolean;
  onSearch: (q: string) => void;
  onSelect: (id: string) => void;
  onToggleStar: (id: string) => void;
}

const labels: Record<MailboxView, string> = {
  inbox: "Inbox", sent: "Sent", starred: "Starred", trash: "Trash",
};

export function EmailList({ emails, selectedId, view, searchQuery, loading, onSearch, onSelect, onToggleStar }: Props) {
  return (
    <div className="flex flex-col h-full bg-card">
      {/* Search */}
      <div className="p-3 border-b border-border">
        <div className="flex items-center gap-2 bg-secondary rounded-xl px-3 py-2">
          <Search className="h-4 w-4 text-muted-foreground shrink-0" />
          <input
            type="text"
            value={searchQuery}
            onChange={e => onSearch(e.target.value)}
            placeholder="Search mail"
            className="flex-1 bg-transparent text-sm outline-none placeholder:text-muted-foreground"
          />
        </div>
      </div>

      {/* Label */}
      <div className="px-4 py-2 border-b border-border">
        <h2 className="text-sm font-medium">{labels[view]}</h2>
      </div>

      {/* List */}
      <div className="flex-1 overflow-y-auto">
        {loading ? (
          <div className="flex items-center justify-center h-40 gap-2 text-muted-foreground text-sm">
            <Loader2 className="h-4 w-4 animate-spin" /> Loading…
          </div>
        ) : emails.length === 0 ? (
          <div className="flex items-center justify-center h-40 text-sm text-muted-foreground">
            No emails found
          </div>
        ) : (
          emails.map(email => (
            <EmailListItem
              key={email.id}
              email={email}
              isSelected={selectedId === email.id}
              onSelect={onSelect}
              onToggleStar={onToggleStar}
            />
          ))
        )}
      </div>
    </div>
  );
}
