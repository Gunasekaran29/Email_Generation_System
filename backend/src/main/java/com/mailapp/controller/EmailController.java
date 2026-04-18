package com.mailapp.controller;

import com.mailapp.dto.EmailResponse;
import com.mailapp.dto.SendEmailRequest;
import com.mailapp.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/emails")
@CrossOrigin(origins = "*") // ✅ FORCE CORS FIX
@RequiredArgsConstructor
@Slf4j
public class EmailController {

    private final EmailService svc;

    // ✅ TEST API (important)
    @GetMapping("/test")
    public String test() {
        return "OK";
    }

    @GetMapping
    public ResponseEntity<List<EmailResponse>> list(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Boolean starred,
            @RequestParam(required = false) String search) {

        if (search != null && !search.isBlank())  return ResponseEntity.ok(svc.search(search));
        if (Boolean.TRUE.equals(starred))          return ResponseEntity.ok(svc.getStarred());
        if (status != null && !status.isBlank())   return ResponseEntity.ok(svc.getByStatus(status));
        return ResponseEntity.ok(svc.getAll());
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> unreadCount() {
        return ResponseEntity.ok(Map.of("count", svc.unreadCount()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmailResponse> get(@PathVariable String id) {
        return ResponseEntity.ok(svc.getById(id));
    }

    @PostMapping("/send")
    public ResponseEntity<?> send(@Valid @RequestBody SendEmailRequest req) {
        try {
            EmailResponse sent = svc.send(req);
            return ResponseEntity.ok(sent);
        } catch (Exception ex) {
            log.error("Send failed: {}", ex.getMessage(), ex);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Send failed: " + ex.getMessage()));
        }
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<EmailResponse> markRead(@PathVariable String id) {
        return ResponseEntity.ok(svc.markRead(id));
    }

    @PatchMapping("/{id}/star")
    public ResponseEntity<EmailResponse> star(@PathVariable String id) {
        return ResponseEntity.ok(svc.toggleStar(id));
    }

    @PatchMapping("/{id}/trash")
    public ResponseEntity<EmailResponse> trash(@PathVariable String id) {
        return ResponseEntity.ok(svc.trash(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }
}