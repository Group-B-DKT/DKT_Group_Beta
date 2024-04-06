package com.example.dkt_group_beta.viewmodel.interfaces;

import com.example.dkt_group_beta.communication.enums.Info;
import com.example.dkt_group_beta.model.GameInfo;

import java.util.List;
import java.util.Map;

public interface InputHandleInfo {
    void handleInfo(Info info, List<GameInfo> gameInfos);
}
