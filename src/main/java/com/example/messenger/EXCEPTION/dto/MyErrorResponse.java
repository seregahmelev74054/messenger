package com.example.messenger.EXCEPTION.dto;

import java.util.List;
import java.util.Map;

public class MyErrorResponse {

    private int status;
    private String message;
    private long timeStamp;

    private Map<String, List<String>> errors;

    public MyErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timeStamp = System.currentTimeMillis();
    }

    public MyErrorResponse(int status, String message, Map<String, List<String>> errors) {
        this.status = status;
        this.message = message;
        this.timeStamp = System.currentTimeMillis();

        this.errors = errors;
    }

    public int getStatus() {return status;}
    public String getMessage() {return message;}
    public long getTimeStamp() {return timeStamp;}

    public Map<String, List<String>> getErrors() {return errors;}

}
