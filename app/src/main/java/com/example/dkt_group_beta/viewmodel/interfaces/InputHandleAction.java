package com.example.dkt_group_beta.viewmodel.interfaces;

import com.example.dkt_group_beta.communication.enums.Action;
import com.example.dkt_group_beta.communication.enums.Info;
import com.example.dkt_group_beta.model.GameInfo;

import java.util.List;

public interface InputHandleAction {
    void handleAction(Action action, String param, String fromPlayername);
}
