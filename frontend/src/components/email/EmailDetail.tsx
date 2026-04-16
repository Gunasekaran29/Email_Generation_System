import { ArrowLeft, Star, Trash2, Reply } from "lucide-react";
import { EmailDTO } from "@/lib/api";
import { formatFullDate } from "@/lib/dateUtils";
import { cn } from "@/lib/utils";

interface Props {
  email: EmailDTO;
  onBack: () => void;
  onToggleStar: (id: string) => void;
  onDelete: (id: string) => void;
  onReply: (email: EmailDTO) => void;
}

export function EmailDetail({ email, onBack, onToggleStar, onDelete, onReply }: Props) {
  return (
    <div className="flex flex-col h-full bg-card overflow-hidden">
      {/* Toolbar */}
      <div className="flex items-center gap-2 px-4 py-3 border-b border-border">
        <button
          onClick={onBack}
          className="p-1.5 rounded-full hover:bg-secondary transition-colors md:hidden"
        >
          <ArrowLeft className="h-5 w-5 text-muted-foreground" />
        </button>
        <div className="flex-1" />
        <button
          onClick={() => onToggleStar(email.id)}
          className="p-1.5 rounded-full hover:bg-secondary transition-colors"
          title="Star"
        >
          <Star className={cn("h-5 w-5", email.isStarred ? "fill-starred text-starred" : "text-muted-foreground")} />
        </button>
        <button
          onClick={() => onDelete(email.id)}
          className="p-1.5 rounded-full hover:bg-secondary transition-colors"
          title="Delete"
        >
          <Trash2 className="h-5 w-5 text-muted-foreground" />
        </button>
        <button
          onClick={() => onReply(email)}
          className="p-1.5 rounded-full hover:bg-secondary transition-colors"
          title="Reply"
        >
          <Reply className="h-5 w-5 text-muted-foreground" />
        </button>
      </div>

      {/* Content */}
      <div className="flex-1 overflow-y-auto p-6">
        <h1 className="text-xl font-normal mb-4">{email.subject}</h1>

        {/* Sender info */}
        <div className="flex items-start gap-3 mb-6">
          <div className="h-10 w-10 rounded-full bg-primary/10 flex items-center justify-center text-sm font-semibold text-primary shrink-0">
            {email.sender[0].toUpperCase()}
          </div>
          <div className="flex-1 min-w-0">
            <div className="flex items-center gap-2 flex-wrap">
              <span className="font-medium text-sm">{email.sender}</span>
              <span className="text-xs text-muted-foreground">&lt;{email.senderEmail}&gt;</span>
            </div>
            <div className="text-xs text-muted-foreground">
              to {email.recipients.join(", ")}
            </div>
            <div className="text-xs text-muted-foreground mt-0.5">
              {formatFullDate(email.createdAt)}
            </div>
          </div>
        </div>

        {/* Body */}
        <div className="text-sm leading-relaxed whitespace-pre-line text-foreground">
          {email.body}
        </div>
      </div>
    </div>
  );
}
