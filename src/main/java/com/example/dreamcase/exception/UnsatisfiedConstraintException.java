package com.example.dreamcase.exception;

public class UnsatisfiedConstraintException extends RuntimeException{
    public UnsatisfiedConstraintException(String message) {
        super(message);
    }

    public UnsatisfiedConstraintException(String message, Throwable cause) {
        super(message, cause);
    }
}
