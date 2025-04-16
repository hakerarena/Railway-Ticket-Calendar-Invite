package com.hakerarena.railway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    // @PostMapping("/generate")
    // public String generateInvite(@RequestParam String pdfPath, @RequestParam
    // String email) {
    // try {
    // pdfPath = "D:/Projects/1.pdf";
    // var details = pdfService.extractDetails(pdfPath);
    // String filePath = calendarService.createCalendarInvite(details);
    // System.out.println(details);
    // emailService.sendEmail(email, "Train Journey Details", "Here is your train
    // invite.", filePath);
    // return "Invite sent successfully!";
    // } catch (Exception e) {
    // return "Error: " + e.getMessage();
    // }
    // }

    @PostMapping(value = "/generate/v2", consumes = "multipart/form-data")
    public String generateInvite(@RequestPart("file") MultipartFile file, @RequestParam("email") String email) {
        try {
            // Save the uploaded file to a temporary location
            String tempFilePath = System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename();
            file.transferTo(new java.io.File(tempFilePath));

            // Process the file and generate the invite
            var details = pdfService.extractDetails(tempFilePath);
            String filePath = calendarService.createCalendarInvite(details);
            System.out.println(details);
            emailService.sendEmail(email, "Train Journey Details", "Here is your train invite.", filePath);

            // Clean up the temporary file
            new java.io.File(tempFilePath).delete();

            return "Invite sent successfully!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}