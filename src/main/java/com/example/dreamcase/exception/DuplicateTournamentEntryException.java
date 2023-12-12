package com.example.dreamcase.exception;

public class DuplicateTournamentEntryException extends RuntimeException{
    public DuplicateTournamentEntryException(String message) {
        super(message);
    }

    public DuplicateTournamentEntryException(String message, Throwable cause) {
        super(message, cause);
    }
}
