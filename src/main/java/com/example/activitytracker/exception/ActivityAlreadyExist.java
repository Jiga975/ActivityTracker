package com.example.activitytracker.exception;

public class ActivityAlreadyExist extends RuntimeException {
    public ActivityAlreadyExist(String message){
        super(message);
    }
}
