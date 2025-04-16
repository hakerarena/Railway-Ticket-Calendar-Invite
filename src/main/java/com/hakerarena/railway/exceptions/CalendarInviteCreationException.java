package com.hakerarena.railway.exceptions;

public class CalendarInviteCreationException extends Exception {
    public CalendarInviteCreationException(String message) {
        super(message);
    }

    public CalendarInviteCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}