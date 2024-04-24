package com.example.dkt_group_beta.communication;

import com.example.dkt_group_beta.communication.enums.Action;
import com.example.dkt_group_beta.model.Player;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public  class ActionJsonObject {
    private Action action;
    private String param;
    private Player fromPlayer;


    public ActionJsonObject(Action action, String param, Player fromPlayer) {
        this.action = action;
        this.param = param;
        this.fromPlayer = fromPlayer;
    }

    public ActionJsonObject(Action action, String param) {
        this(action, param, null);
    }

    public ActionJsonObject(Action action) {
        this(action, null);
    }

    public Action getAction() {
        return action;
    }
    public String getParam() {
        return param;
    }

    public Player getFromPlayer(){return fromPlayer;}

}
