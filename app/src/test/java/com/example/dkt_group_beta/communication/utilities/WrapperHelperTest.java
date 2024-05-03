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

import junit.framework.TestCase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WrapperHelperTest extends TestCase {
    private static Gson gson;

    @BeforeEach
    public void setUp(){
        System.out.println("ASD");
        gson = new Gson();
    }
    @Test
    public void testGetInstanceFromWrapperReturnNull(){
        assertNull(WrapperHelper.getInstanceFromWrapper(new Wrapper(null, -1, Request.INFO, null)));
    }
    @Test
    public void testGetInstanceFromWrapperConnectJsonObject(){
        ConnectJsonObject connectJsonObject = new ConnectJsonObject(ConnectType.NEW_CONNECT);
        Wrapper wrapper = new Wrapper(connectJsonObject.getClass().getSimpleName(), -1, Request.CONNECT, connectJsonObject);
        assertTrue(WrapperHelper.getInstanceFromWrapper(wrapper) instanceof ConnectJsonObject);
    }
    @Test
    public void testGetInstanceFromWrapperReturnActionJsonObject(){
        ActionJsonObject actionJsonObject = new ActionJsonObject(Action.ROLL_DICE);
        Wrapper wrapper = new Wrapper(actionJsonObject.getClass().getSimpleName(), -1, Request.CONNECT, actionJsonObject);
        assertTrue(WrapperHelper.getInstanceFromWrapper(wrapper) instanceof ActionJsonObject);
    }
    @Test
    public void testGetInstanceFromWrapperReturnInfoJsonObject(){
        InfoJsonObject infoJsonObject = new InfoJsonObject(Info.GAME_LIST);
        Wrapper wrapper = new Wrapper(infoJsonObject.getClass().getSimpleName(), -1, Request.CONNECT, infoJsonObject);
        assertTrue(WrapperHelper.getInstanceFromWrapper(wrapper) instanceof InfoJsonObject);
    }
    @Test
    public void testGetInstanceFromJsonReturnTrue(){
        InfoJsonObject infoJsonObject = new InfoJsonObject(Info.GAME_LIST);
        Wrapper wrapper = new Wrapper(infoJsonObject.getClass().getSimpleName(), -1, Request.CONNECT, infoJsonObject);
        String json = gson.toJson(wrapper);
        assertTrue(WrapperHelper.getInstanceFromJson(json) instanceof InfoJsonObject);
    }
    @Test
    public void testGetInstanceFromJsonReturnNull(){
        String json = "";
        assertNull(WrapperHelper.getInstanceFromJson(json));
    }
    @Test
    public void testToJsonFromObjectCorrect(){
        ConnectJsonObject connectJsonObject = new ConnectJsonObject(ConnectType.NEW_CONNECT);
        Wrapper wrapper = new Wrapper(connectJsonObject.getClass().getSimpleName(), -1, Request.CONNECT, connectJsonObject);
        String expected = gson.toJson(wrapper);

        assertEquals(expected, WrapperHelper.toJsonFromObject(-1, Request.CONNECT, connectJsonObject));
        assertEquals(expected, WrapperHelper.toJsonFromObject(Request.CONNECT, connectJsonObject));
    }
}
