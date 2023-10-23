package com.example.activitytracker.exception;


public class ActivityNotFoundException extends RuntimeException {
    public ActivityNotFoundException(String message){
        super(message);
    }
}