import { Inbox, Send, Star, Trash2, Edit } from "lucide-react";
import { MailboxView } from "@/hooks/useEmails";
import { cn } from "@/lib/utils";

interface Props {
  activeView: MailboxView;
  onViewChange: (v: MailboxView) => void;
  unreadCount: number;
  onCompose: () => void;
}

const menu = [
  { id: "inbox",   label: "Inbox",   icon: Inbox },
  { id: "sent",    label: "Sent",    icon: Send },
  { id: "starred", label: "Starred", icon: Star },
  { id: "trash",   label: "Trash",   icon: Trash2 },
] as const;

export function EmailSidebar({ activeView, onViewChange, unreadCount, onCompose }: Props) {
  return (
    <aside className="w-56 border-r border-border bg-card flex flex-col shrink-0">
      <div className="p-4">
        <button
          onClick={onCompose}
          className="flex items-center gap-2 bg-primary text-primary-foreground px-4 py-2 rounded-2xl w-full font-medium text-sm shadow-sm hover:opacity-90 transition"
        >
          <Edit size={15} />
          Compose
        </button>
      </div>

      <nav className="flex flex-col gap-0.5 px-2">
        {menu.map(({ id, label, icon: Icon }) => {
          const active = activeView === id;
          return (
            <button
              key={id}
              onClick={() => onViewChange(id)}
              className={cn(
                "flex items-center justify-between px-3 py-2 rounded-xl text-sm transition-colors",
                active
                  ? "bg-primary/10 text-primary font-semibold"
                  : "text-muted-foreground hover:bg-secondary"
              )}
            >
              <span className="flex items-center gap-2.5">
                <Icon size={16} />
                {label}
              </span>
              {id === "inbox" && unreadCount > 0 && (
                <span className="text-xs bg-primary text-white px-2 py-0.5 rounded-full font-medium">
                  {unreadCount}
                </span>
              )}
            </button>
          );
        })}
      </nav>
    </aside>
  );
}
