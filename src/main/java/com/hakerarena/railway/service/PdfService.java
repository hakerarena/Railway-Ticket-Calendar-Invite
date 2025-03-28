package com.hakerarena.railway.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import com.hakerarena.railway.model.TrainClass;
import com.hakerarena.railway.model.TrainDetails;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PdfService {

    public TrainDetails extractDetails(String pdfPath) throws IOException {
        return parseTrainDetails(extractText(pdfPath));
    }

    public String extractText(String filePath) {
        try (PDDocument document = PDDocument.load(new File(filePath))) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        } catch (IOException e) {
            System.err.println("Error reading PDF: " + e.getMessage());
            return null;
        }
    }

    private TrainDetails parseTrainDetails(String text) {
        TrainDetails details = new TrainDetails();

        // Extract Station Details
        Pattern stationPattern = Pattern.compile(
                "Booked From\\s+To\\s*\\n\\s*([^(]+?)\\s*\\(([^)]+)\\)\\s*([^(]+?)\\s*\\(([^)]+)\\)\\s*([^(]+?)\\s*\\(([^)]+)\\)");
        Matcher stationMatcher = stationPattern.matcher(text);

        if (stationMatcher.find()) {
            details.setBookedFromStation(stationMatcher.group(1).trim() + " (" + stationMatcher.group(2).trim() + ")");
            details.setDepartureStation(stationMatcher.group(3).trim() + " (" + stationMatcher.group(4).trim() + ")");
            details.setArrivalStation(stationMatcher.group(5).trim() + " (" + stationMatcher.group(6).trim() + ")");
        } else {
            System.out.println("Station details not found.");
        }

        // Normalize line breaks and remove extra spaces
        String cleanedText = text.replaceAll("\\r", "").replaceAll("\\u00A0", " ").replaceAll("\\s+", " ").trim();

        // Extract PNR, Train No., Train Name, and Class using more flexible regex
        Pattern trainPattern = Pattern.compile(
                "PNR\\s+Train No\\./Name\\s+Class\\s*\\n?(\\d+)\\s+(\\d+)\\s*/\\s*([A-Za-z\\s]+)");
        Matcher trainMatcher = trainPattern.matcher(cleanedText);

        if (trainMatcher.find()) {
            String pnr = trainMatcher.group(1);
            String trainNumber = trainMatcher.group(2);
            String trainInfo = trainMatcher.group(3).trim();

            TrainClass trainClass = TrainClass.fromString(trainInfo);

            if (trainClass != null) {
                String className = trainClass.getName();
                String classCode = trainClass.getCode();
                String trainName = trainInfo.replace(className, "").trim();

                details.setPnr(pnr);
                details.setTrainNumber(trainNumber);
                details.setTrainName(trainName);
                details.setClassName(className);
                details.setClassCode(classCode);
            } else {
                System.out.println("Train class not identified.");
            }
        } else {
            System.out.println("PNR not found.");
        }

        // Extract Departure and Arrival
        Pattern departurePattern = Pattern.compile("(?<=Departure\\*\\s*)(\\d{2}:\\d{2}\\s+\\d{2}-[A-Za-z]+-\\d{4})");
        Pattern arrivalPattern = Pattern.compile("(?<=Arrival\\*\\s*)(\\d{2}:\\d{2}\\s+\\d{2}-[A-Za-z]+-\\d{4})");

        Matcher departureMatcher = departurePattern.matcher(text);
        Matcher arrivalMatcher = arrivalPattern.matcher(text);

        if (departureMatcher.find()) {
            details.setDepartureDate(departureMatcher.group(1));
        } else {
            System.out.println("Departure not found.");
        }

        if (arrivalMatcher.find()) {
            details.setArrivalDate(arrivalMatcher.group(1));
        } else {
            System.out.println("Arrival not found.");
        }
        return details;
    }
}
