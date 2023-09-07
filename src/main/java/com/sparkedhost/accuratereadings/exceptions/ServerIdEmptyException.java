package com.sparkedhost.accuratereadings.exceptions;

public class ServerIdEmptyException extends Exception {
    public ServerIdEmptyException() {
        super("The server ID appears to be empty. Either we were unable to determine the ID automatically, or " +
                "there's a bug in the code.");
    }
}
