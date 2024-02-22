package com.sachin.spring.exception;

public class BookServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BookServiceException(String message) {
        super(message);
    }
}
