package com.beauty_saloon_backend.controller;

import com.beauty_saloon_backend.dto.ContactFormData;
import com.beauty_saloon_backend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    @Autowired
    private EmailService emailService;



    @PostMapping("/submit")
    public ResponseEntity<?> submitContactForm(@RequestBody ContactFormData contactFormData) {
        try {
            // Send contact form details to admin
            emailService.sendContactFormToAdmin(contactFormData.getName(), contactFormData.getEmail(), contactFormData.getPhone(), contactFormData.getMessage());

            // Send confirmation email to the client
            emailService.sendConfirmationEmail(contactFormData.getEmail(), contactFormData.getName());

            return ResponseEntity.ok("Üzenet sikeresen elküldve!");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Hiba az üzenet elküldésekor: " + e.getMessage());
        }
    }
}
