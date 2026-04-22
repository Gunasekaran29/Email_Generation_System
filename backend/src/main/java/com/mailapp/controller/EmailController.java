package com.mailapp.controller;

import com.mailapp.dto.EmailResponse;
import com.mailapp.dto.SendEmailRequest;
import com.mailapp.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/emails")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // backup safety
public class EmailController {

    private final EmailService svc;

    @GetMapping("/test")
    public String test() {
        return "OK";
    }

    @GetMapping
    public ResponseEntity<List<EmailResponse>> list(
            @RequestParam(required = false) String status) {

        if (status != null) return ResponseEntity.ok(svc.getByStatus(status));
        return ResponseEntity.ok(svc.getAll());
    }
    @PatchMapping("/{id}/read")
public ResponseEntity<EmailResponse> markRead(@PathVariable String id) {
    return ResponseEntity.ok(svc.markRead(id));
}

@PatchMapping("/{id}/star")
public ResponseEntity<EmailResponse> toggleStar(@PathVariable String id) {
    return ResponseEntity.ok(svc.toggleStar(id));
}

@PatchMapping("/{id}/trash")
public ResponseEntity<EmailResponse> trash(@PathVariable String id) {
    return ResponseEntity.ok(svc.trash(id));
}
    @PostMapping("/send")
    public ResponseEntity<?> send(@Valid @RequestBody SendEmailRequest req) {
        try {
            return ResponseEntity.ok(svc.send(req));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // ⭐ IMPORTANT — FIXES PREFLIGHT
    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptions() {
        return ResponseEntity.ok().build();
    }
}