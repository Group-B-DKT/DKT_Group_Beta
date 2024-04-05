package com.example.dkt_group_beta.communication.utilities;

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

    public Wrapper(String classname, int gameId, Request request, Object object) {
        this.classname = classname;
        this.gameId = gameId;
        this.request = request;
        this.object = object;
    }
}
