package me.bettd.pterolink.exceptions;

public class InvalidTaskTypeException extends Exception {
    public InvalidTaskTypeException() {
        super("The supplied task type is invalid.");
    }
}
