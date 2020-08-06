package com.thoughtworks.rslist.exception;

public class UserNotEnoughVoteException extends RuntimeException{
    public UserNotEnoughVoteException(String message) {
        super(message);
    }
}
