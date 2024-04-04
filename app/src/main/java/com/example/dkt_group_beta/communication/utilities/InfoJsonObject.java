package com.example.dkt_group_beta.communication.utilities;


import com.example.dkt_group_beta.communication.enums.Info;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class InfoJsonObject {
    @Getter
    private Info info;
    @Getter
    private int gameId;
}
