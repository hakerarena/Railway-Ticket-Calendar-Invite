package com.hakerarena.railway.service;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.hakerarena.railway.model.TrainDetails;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;

@Service
public class CalendarService {

    public String createCalendarInvite(TrainDetails details) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd-MMM-yyyy");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("IST"));
        Date departureDate = sdf.parse(details.getDepartureDate());
        Date arrivalDate = sdf.parse(details.getArrivalDate());

        // Create calendar
        Calendar calendar = new Calendar();
        calendar.getProperties().add(new ProdId("-//Railway Invite//iCal4j 1.0//EN"));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);

        // Create event
        VEvent event = new VEvent(
                new DateTime(departureDate),
                new DateTime(arrivalDate),
                "Train to " + details.getArrivalStation());

        event.getProperties().add(new Location(details.getDepartureStation()));
        event.getProperties().add(new Description(
                "Train Number: " + details.getTrainNumber() + " / " + details.getTrainName()
                + "\nPNR: " + details.getPnr()));

        calendar.getComponents().add(event);

        // Save invite
        String filePath = "train-invite.ics";
        try (FileOutputStream fout = new FileOutputStream(filePath)) {
            CalendarOutputter outputter = new CalendarOutputter();
            outputter.output(calendar, fout);
        }
        return filePath;
    }
}
