package com.example.study_org_server.exception;

public class MeetingNotFoundException extends RuntimeException {
    private String meetingName;
    public MeetingNotFoundException(String meetingName){
        super(meetingName +" is not Found");
        this.meetingName=meetingName;

    }
}
