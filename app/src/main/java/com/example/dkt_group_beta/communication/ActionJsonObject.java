package com.example.dkt_group_beta.communication;

import com.example.dkt_group_beta.communication.enums.Action;
import com.example.dkt_group_beta.model.Field;
import com.example.dkt_group_beta.model.Player;

import java.util.List;

public  class ActionJsonObject {
    private Action action;
    private String param;
    private Player fromPlayer;

    private List<Field> fields;

    public ActionJsonObject(Action action, String param, Player fromPlayer, List<Field> fields) {
        this.action = action;
        this.param = param;
        this.fromPlayer = fromPlayer;
        this.fields = fields;
    }

    public ActionJsonObject(Action action, String param, List<Field> fields) {
        this(action, param, null, fields);
    }

    public ActionJsonObject(Action action, String param, Player fromPlayer) {
        this(action, param, fromPlayer, null);
    }

    public ActionJsonObject(Action action, String param) {
        this(action, param, null, null);
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

    public List<Field> getFields() {
        return fields;
    }
}
