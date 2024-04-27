package com.example.dkt_group_beta.model;

import lombok.Getter;

public class Field {

    @Getter
    private int position;
    @Getter
    private String name;


    public Field(int position, String name) {
        this.position = position;
        this.name = name;
    }

}
