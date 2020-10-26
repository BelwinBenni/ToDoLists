package com.bb.tododemo;

import java.io.Serializable;

public class Tasks implements Serializable {
    private String topic;
    private String notes;
    private String date;
    private String time;
    private boolean isFinished;



    public Tasks() {
    }


    public Tasks(String topic, String notes, String date, String time, boolean isFinished) {
        this.topic = topic;
        this.notes = notes;
        this.date = date;
        this.time = time;
        this.isFinished = false;
    }

    public String getTopic() {
        return topic;
    }

    public String getNotes() {
        return notes;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

}
