package com.hakerarena.railway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hakerarena.railway.service.CalendarService;
import com.hakerarena.railway.service.EmailService;
import com.hakerarena.railway.service.PdfService;

@RestController
@RequestMapping("/invite")
public class InviteController {

    @Autowired
    private PdfService pdfService;
    @Autowired
    private CalendarService calendarService;
    @Autowired
    private EmailService emailService;

    @PostMapping("/generate")
    public String generateInvite(@RequestParam String pdfPath, @RequestParam String email) {
        try {
            var details = pdfService.extractDetails(pdfPath);
            String filePath = calendarService.createCalendarInvite(details);
            emailService.sendEmail(email, "Train Journey Details", "Here is your train invite.", filePath);
            return "Invite sent successfully!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}