package com.example.dkt_group_beta.communication;

import com.example.dkt_group_beta.communication.enums.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class Wrapper {
    @Getter
    private String classname;
    @Getter
    private int gameId;
    @Getter
    private Request request;
    @Getter
    private Object object;

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
