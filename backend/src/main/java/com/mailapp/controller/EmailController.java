package com.mailapp.controller;

import com.mailapp.model.Email;
import com.mailapp.service.EmailService;
import com.mailapp.dto.SendEmailRequest;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emails")
@CrossOrigin
public class EmailController {

    private final EmailService svc;

    public EmailController(EmailService svc) {
        this.svc = svc;
    }

    @GetMapping
    public List<Email> getAll(@RequestParam(required = false) String status) {
        if (status != null) {
            return svc.getByStatus(status);
        }
        return svc.getAll();
    }

    @PostMapping("/send")
    public Email send(@RequestBody SendEmailRequest req) {
        return svc.send(req);
    }

    @PatchMapping("/{id}/read")
    public Email markRead(@PathVariable String id) {
        return svc.markRead(id);
    }

    @PatchMapping("/{id}/star")
    public Email toggleStar(@PathVariable String id) {
        return svc.toggleStar(id);
    }

    @PatchMapping("/{id}/trash")
    public Email trash(@PathVariable String id) {
        return svc.trash(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        svc.delete(id);
    }
}