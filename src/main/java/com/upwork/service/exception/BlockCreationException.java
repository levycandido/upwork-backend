package com.upwork.service.exception;

public class BlockCreationException extends RuntimeException {
    public BlockCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}