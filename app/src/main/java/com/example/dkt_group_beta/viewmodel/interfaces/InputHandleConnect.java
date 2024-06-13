package com.example.dkt_group_beta.viewmodel.interfaces;

import com.example.dkt_group_beta.communication.enums.ConnectType;
import com.example.dkt_group_beta.model.GameInfo;

public interface InputHandleConnect {
    void handleConnect(ConnectType connectType, GameInfo gameInfo);
}
