package com.example.exception;

public class DuplicateBookException extends RuntimeException {
    public DuplicateBookException(String isbn) {
        super("Book with ISBN " + isbn + " already exists");
    }
}
