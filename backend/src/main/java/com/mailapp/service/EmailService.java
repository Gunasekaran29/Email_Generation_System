public EmailResponse send(SendEmailRequest req) {

    EmailResponse res = new EmailResponse();

    res.setId(UUID.randomUUID().toString());
    res.setSender("Me");

    // 🔥 IMPORTANT FIX
    res.setSenderEmail("me@mail.com");
    res.setRecipients(Arrays.asList(req.getTo().split(",")));

    res.setSubject(req.getSubject());
    res.setBody(req.getBody());
    res.setPreview(req.getBody() != null ? req.getBody().substring(0, Math.min(50, req.getBody().length())) : "");
    res.setStatus("sent");
    res.setRead(true);
    res.setStarred(false);
    res.setAvatar("");
    res.setCreatedAt(java.time.LocalDateTime.now().toString());

    emails.add(res);

    return res;
}
public EmailResponse markRead(String id) {
    for (EmailResponse e : emails) {
        if (e.getId().equals(id)) {
            e.setRead(true);
            return e;
        }
    }
    return null;
}

public EmailResponse toggleStar(String id) {
    for (EmailResponse e : emails) {
        if (e.getId().equals(id)) {
            e.setStarred(!e.isStarred());
            return e;
        }
    }
    return null;
}

public EmailResponse trash(String id) {
    for (EmailResponse e : emails) {
        if (e.getId().equals(id)) {
            e.setStatus("trash");
            return e;
        }
    }
    return null;
}