package com.hakerarena.railway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

public class InviteService {
    @Autowired
    private PdfService pdfService;
    @Autowired
    private CalendarService calendarService;
    @Autowired
    private EmailService emailService;

    public String generateInvite(MultipartFile file, String email) {
        try {
            // Save the uploaded file to a temporary location
            String tempFilePath = System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename();
            file.transferTo(new java.io.File(tempFilePath));

            // Process the file and generate the invite
            var details = pdfService.extractDetails(tempFilePath);
            String filePath = calendarService.createCalendarInvite(details);
            emailService.sendEmail(email, "Train Journey Details", "Here is your train invite.", filePath);

            // Clean up the temporary file
            new java.io.File(tempFilePath).delete();

            return "Invite sent successfully!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
