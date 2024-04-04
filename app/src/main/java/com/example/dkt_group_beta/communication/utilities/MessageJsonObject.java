package com.example.dkt_group_beta.communication.utilities;

public class MessageJsonObject {
    private String text;
    private String from;

    public MessageJsonObject(String text, String from) {
        this.text = text;
        this.from = from;
    }

    public String getText() {
        return text;
    }

    public String getFrom() {
        return from;
    }
}
