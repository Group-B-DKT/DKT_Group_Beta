package com.example.dkt_group_beta.communication;

import com.example.dkt_group_beta.communication.enums.Action;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public  class ActionJsonObject {
    private Action action;
    private String param;
    private String fromPlayername;


    public ActionJsonObject(Action action, String param, String fromPlayername) {
        this.action = action;
        this.param = param;
        this.fromPlayername = fromPlayername;
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

    public String getFromPlayername(){return fromPlayername;}

}
