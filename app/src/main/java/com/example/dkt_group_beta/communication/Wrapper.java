package com.example.dkt_group_beta.communication;

import com.example.dkt_group_beta.communication.enums.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

public class Wrapper {
    private String classname;
    private int gameId;
    private Request request;
    private Object object;

    public Wrapper(String classname, int gameId, Request request, Object object) {
        this.classname = classname;
        this.gameId = gameId;
        this.request = request;
        this.object = object;
    }

    public String getClassname() {
        return classname;
    }

    public int getGameId() {
        return gameId;
    }

    public Request getRequest() {
        return request;
    }

    public Object getObject() {
        return object;
    }

    @Override
    public String toString() {
        return "Wrapper{" +
                "classname='" + classname + '\'' +
                ", gameId=" + gameId +
                ", request=" + request +
                ", object=" + object +
                '}';
    }
}
