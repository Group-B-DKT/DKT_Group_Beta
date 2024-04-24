package com.example.dkt_group_beta.communication.controller;

import android.util.Log;

import com.example.dkt_group_beta.communication.ActionJsonObject;
import com.example.dkt_group_beta.communication.enums.Action;
import com.example.dkt_group_beta.communication.enums.Request;
import com.example.dkt_group_beta.communication.utilities.WrapperHelper;
import com.example.dkt_group_beta.viewmodel.interfaces.InputHandleAction;

public class ActionController {
    private InputHandleAction handleAction;

    public ActionController(InputHandleAction handleAction) {
        this.handleAction = handleAction;
        WebsocketClientController.addMessageHandler(this::onMessageReceived);
    }
    public void createGame(String gameName){
        ActionJsonObject actionJsonObject = new ActionJsonObject(Action.CREATE_GAME, gameName);
        String msg = WrapperHelper.toJsonFromObject(Request.ACTION, actionJsonObject);
        WebsocketClientController.sendToServer(msg);
    }

    public void joinGame(int gameId){
        ActionJsonObject actionJsonObject = new ActionJsonObject(Action.JOIN_GAME);
        String msg = WrapperHelper.toJsonFromObject(gameId, Request.ACTION, actionJsonObject);

        WebsocketClientController.sendToServer(msg);
    }
    public void leaveGame() {
        int gameId = WebsocketClientController.getConnectedGameId();
        ActionJsonObject actionJsonObject = new ActionJsonObject(Action.LEAVE_GAME);
        String msg = WrapperHelper.toJsonFromObject(gameId, Request.ACTION, actionJsonObject);
        WebsocketClientController.sendToServer(msg);
    }

    public void isReady(boolean isReady){
        ActionJsonObject actionJsonObject;
        if (isReady)
            actionJsonObject = new ActionJsonObject(Action.READY, null, WebsocketClientController.getPlayer());
        else actionJsonObject = new ActionJsonObject(Action.NOT_READY, null, WebsocketClientController.getPlayer());

        int connectedGameId = WebsocketClientController.getConnectedGameId();
        if (connectedGameId == -1)
            return;

        String msg = WrapperHelper.toJsonFromObject(connectedGameId, Request.ACTION, actionJsonObject);
        Log.d("DEBUG", msg);
        WebsocketClientController.sendToServer(msg);
    }

    private void onMessageReceived(Object actionObject) {
        if (!(actionObject instanceof ActionJsonObject))
            return;

        Log.d("DEBUG", "ActionController::onMessageReceived/ " + ((ActionJsonObject) actionObject).getAction());

        ActionJsonObject actionJsonObject = (ActionJsonObject) actionObject;
        handleAction.handleAction(actionJsonObject.getAction(), actionJsonObject.getParam(), actionJsonObject.getFromPlayer());
    }

}
