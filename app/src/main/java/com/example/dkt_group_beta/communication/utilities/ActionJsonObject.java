package com.example.dkt_group_beta.communication.utilities;

import com.example.dkt_group_beta.communication.enums.Action;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class ActionJsonObject {
    private Action action;
    private String param;
    private String fromPlayername;

    public ActionJsonObject(Action action) {
        this.action = action;
    }

    public Action getAction() {
        return action;
    }
    public String getParam() {
        return param;
    }

    public String getFromPlayername(){return fromPlayername;}

}
