package com.example.dkt_group_beta.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Player {

    @Getter
    private String username;
    @Getter
    private String id;
    @Getter
    private boolean isConnected;
    @Getter
    private int gameId;






}
