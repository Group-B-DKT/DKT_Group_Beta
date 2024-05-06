package com.example.dkt_group_beta.communication.utilities;

import com.example.dkt_group_beta.communication.ActionJsonObject;
import com.example.dkt_group_beta.communication.ConnectJsonObject;
import com.example.dkt_group_beta.communication.InfoJsonObject;
import com.example.dkt_group_beta.communication.Wrapper;
import com.example.dkt_group_beta.communication.enums.Request;
import com.google.gson.Gson;

public class WrapperHelper {
    private static Gson gson = new Gson();

    private WrapperHelper() {}

    public static Object getInstanceFromWrapper(Wrapper wrapper){
        if (wrapper.getClassname() == null || wrapper.getClassname().isEmpty())
            return null;

        String object = gson.toJson(wrapper.getObject());

        switch (wrapper.getClassname()){
            case "ConnectJsonObject": {
                return gson.fromJson(object, ConnectJsonObject.class);
            }
            case "ActionJsonObject": {
                return gson.fromJson(object, ActionJsonObject.class);
            }
            case "InfoJsonObject": {
                return gson.fromJson(object, InfoJsonObject.class);
            }
            default:
                return null;
        }
    }

    public static Object getInstanceFromJson(String json){
        Wrapper wrapper;
        try {
            wrapper = gson.fromJson(json, Wrapper.class);
        }catch (Exception e){
            return null;
        }
        return wrapper != null ? getInstanceFromWrapper(wrapper) : null;
    }

    public static String toJsonFromObject(int gameId, Request request, Object object){
        Wrapper wrapper = new Wrapper(object.getClass().getSimpleName(), gameId, request, object);
        return gson.toJson(wrapper);
    }

    public static String toJsonFromObject(Request request, Object object){
        Wrapper wrapper = new Wrapper(object.getClass().getSimpleName(), -1, request, object);
        return gson.toJson(wrapper);
    }
}
