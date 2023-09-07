package com.sparkedhost.accuratereadings.exceptions;

public class InvalidPowerActionException extends Exception {
    public InvalidPowerActionException() {
        super("The supplied power action is invalid.");
    }
}
