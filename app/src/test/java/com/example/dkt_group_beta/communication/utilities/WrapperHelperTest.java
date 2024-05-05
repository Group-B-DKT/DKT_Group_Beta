package com.example.dkt_group_beta.communication.utilities;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.dkt_group_beta.communication.ActionJsonObject;
import com.example.dkt_group_beta.communication.ConnectJsonObject;
import com.example.dkt_group_beta.communication.InfoJsonObject;
import com.example.dkt_group_beta.communication.Wrapper;
import com.example.dkt_group_beta.communication.enums.Action;
import com.example.dkt_group_beta.communication.enums.ConnectType;
import com.example.dkt_group_beta.communication.enums.Info;
import com.example.dkt_group_beta.communication.enums.Request;
import com.google.gson.Gson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WrapperHelperTest {
    private Gson gson;

    @BeforeEach
    void setUp(){
        gson = new Gson();
    }

    @Test
    void getInstanceFromWrapperReturnNull(){
        assertNull(WrapperHelper.getInstanceFromWrapper(new Wrapper(null, -1, Request.INFO, null)));
    }

    @Test
    void getInstanceFromWrapperConnectJsonObject(){
        ConnectJsonObject connectJsonObject = new ConnectJsonObject(ConnectType.NEW_CONNECT);
        Wrapper wrapper = new Wrapper(connectJsonObject.getClass().getSimpleName(), -1, Request.CONNECT, connectJsonObject);
        assertTrue(WrapperHelper.getInstanceFromWrapper(wrapper) instanceof ConnectJsonObject);
    }

    @Test
    void getInstanceFromWrapperReturnActionJsonObject(){
        ActionJsonObject actionJsonObject = new ActionJsonObject(Action.ROLL_DICE);
        Wrapper wrapper = new Wrapper(actionJsonObject.getClass().getSimpleName(), -1, Request.CONNECT, actionJsonObject);
        assertTrue(WrapperHelper.getInstanceFromWrapper(wrapper) instanceof ActionJsonObject);
    }

    @Test
    void getInstanceFromWrapperReturnInfoJsonObject(){
        InfoJsonObject infoJsonObject = new InfoJsonObject(Info.GAME_LIST);
        Wrapper wrapper = new Wrapper(infoJsonObject.getClass().getSimpleName(), -1, Request.CONNECT, infoJsonObject);
        assertTrue(WrapperHelper.getInstanceFromWrapper(wrapper) instanceof InfoJsonObject);
    }

    @Test
    void getInstanceFromJsonReturnTrue(){
        InfoJsonObject infoJsonObject = new InfoJsonObject(Info.GAME_LIST);
        Wrapper wrapper = new Wrapper(infoJsonObject.getClass().getSimpleName(), -1, Request.CONNECT, infoJsonObject);
        String json = gson.toJson(wrapper);
        assertTrue(WrapperHelper.getInstanceFromJson(json) instanceof InfoJsonObject);
    }

    @Test
    void getInstanceFromJsonReturnNull(){
        String json = "";
        assertNull(WrapperHelper.getInstanceFromJson(json));
    }

    @Test
    void toJsonFromObjectCorrect(){
        ConnectJsonObject connectJsonObject = new ConnectJsonObject(ConnectType.NEW_CONNECT);
        Wrapper wrapper = new Wrapper(connectJsonObject.getClass().getSimpleName(), -1, Request.CONNECT, connectJsonObject);
        String expected = gson.toJson(wrapper);

        assertEquals(expected, WrapperHelper.toJsonFromObject(-1, Request.CONNECT, connectJsonObject));
        assertEquals(expected, WrapperHelper.toJsonFromObject(Request.CONNECT, connectJsonObject));
    }
}
