package com.hakerarena.railway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hakerarena.railway.model.SuccessResponse;

@Service
public class InviteService {
    private final PdfService pdfService;
    private final CalendarService calendarService;
    private final EmailService emailService;

    @Autowired
    public InviteService(PdfService pdfService, CalendarService calendarService, EmailService emailService) {
        this.pdfService = pdfService;
        this.calendarService = calendarService;
        this.emailService = emailService;
    }

    public SuccessResponse generateInvite(MultipartFile file, String email) {
        SuccessResponse response = new SuccessResponse();
        try {
            // Save the uploaded file to a temporary location
            String tempFilePath = System.getProperty("java.io.tmpdir") + java.io.File.separator + file.getOriginalFilename();
            file.transferTo(new java.io.File(tempFilePath));

            // Process the file and generate the invite
            var details = pdfService.extractDetails(tempFilePath);
            String filePath = calendarService.createCalendarInvite(details);
            emailService.sendEmail(email, "Train Journey Details", "Here is your train invite.", filePath);

            // Clean up the temporary file
            try {
                java.nio.file.Files.delete(java.nio.file.Paths.get(tempFilePath));
                response.setSuccess(true);
            } catch (Exception deleteException) {
                response.setSuccess(false);
                response.setMessage("Error deleting temporary file: " + deleteException.getMessage());
            }
            response.setMessage("Invite sent successfully!");
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Error: " + e.getMessage());
        }
        return response;
    }
}
