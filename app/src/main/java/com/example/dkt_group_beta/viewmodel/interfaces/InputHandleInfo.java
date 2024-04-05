package com.example.dkt_group_beta.viewmodel.interfaces;

import com.example.dkt_group_beta.communication.enums.Info;

import java.util.Map;

public interface InputHandleInfo {
    void handleInfo(Info info, Map<Integer, Integer> gameInfo);
}
