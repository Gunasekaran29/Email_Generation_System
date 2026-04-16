import { Star } from "lucide-react";
import { EmailDTO } from "@/lib/api";
import { cn } from "@/lib/utils";
import { formatEmailDate } from "@/lib/dateUtils";

interface Props {
  email: EmailDTO;
  isSelected: boolean;
  onSelect: (id: string) => void;
  onToggleStar: (id: string) => void;
}

export function EmailListItem({ email, isSelected, onSelect, onToggleStar }: Props) {
  return (
    <div
      onClick={() => onSelect(email.id)}
      className={cn(
        "flex items-center gap-3 px-4 py-2.5 cursor-pointer border-b border-border transition-colors",
        isSelected && "bg-primary/5 border-l-2 border-l-primary",
        !email.isRead ? "email-item-unread" : "email-item-read",
        "hover:bg-secondary/60"
      )}
    >
      {/* Star */}
      <button
        onClick={e => { e.stopPropagation(); onToggleStar(email.id); }}
        className="shrink-0"
      >
        <Star
          className={cn(
            "h-4 w-4 transition-colors",
            email.isStarred
              ? "fill-starred text-starred"
              : "text-muted-foreground/40 hover:text-muted-foreground"
          )}
        />
      </button>

      {/* Avatar */}
      <div className="shrink-0 h-8 w-8 rounded-full bg-primary/10 flex items-center justify-center text-xs font-semibold text-primary">
        {email.sender[0].toUpperCase()}
      </div>

      {/* Content */}
      <div className="flex-1 min-w-0">
        <div className="flex items-center justify-between gap-2">
          <span className={cn("text-sm truncate", !email.isRead && "font-semibold")}>
            {email.sender}
          </span>
          <span className="text-xs text-muted-foreground shrink-0">
            {formatEmailDate(email.createdAt)}
          </span>
        </div>
        <div className={cn("text-sm truncate", !email.isRead ? "text-foreground" : "text-muted-foreground")}>
          {email.subject}
        </div>
        <div className="text-xs text-muted-foreground truncate">{email.preview}</div>
      </div>
    </div>
  );
}
