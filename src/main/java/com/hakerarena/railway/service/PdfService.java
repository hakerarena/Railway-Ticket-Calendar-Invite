package com.hakerarena.railway.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import com.hakerarena.railway.model.TrainDetails;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PdfService {

    public TrainDetails extractDetails(String pdfPath) throws IOException {
        File file = new File(pdfPath);
        try (PDDocument document = PDDocument.load(file)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String pdfContent = pdfStripper.getText(document);

            return parseTrainDetails(pdfContent);
        }
    }

    private TrainDetails parseTrainDetails(String text) {
        TrainDetails details = new TrainDetails();

        // Extract using regex
        Pattern pnrPattern = Pattern.compile("PNR\\s*(\\d+)");
        Pattern trainPattern = Pattern.compile("Train No./Name\\s*(\\d+)");
        Pattern fromToPattern = Pattern.compile("From\\s*(.*?)\\s*To\\s*(.*?)\\s*");
        Pattern dateTimePattern = Pattern.compile(
                "Departure\\*\\s*(\\d{2}:\\d{2})\\s*(\\d{2}-[A-Za-z]{3}-\\d{4})\\s*Arrival\\*\\s*(\\d{2}:\\d{2})\\s*(\\d{2}-[A-Za-z]{3}-\\d{4})");

        Matcher matcher = pnrPattern.matcher(text);
        if (matcher.find())
            details.setPnr(matcher.group(1));

        matcher = trainPattern.matcher(text);
        if (matcher.find())
            details.setTrainNumber(matcher.group(1));

        matcher = fromToPattern.matcher(text);
        if (matcher.find()) {
            details.setFromStation(matcher.group(1));
            details.setToStation(matcher.group(2));
        }

        matcher = dateTimePattern.matcher(text);
        if (matcher.find()) {
            details.setDepartureTime(matcher.group(1) + " " + matcher.group(2));
            details.setArrivalTime(matcher.group(3) + " " + matcher.group(4));
        }

        return details;
    }
}
