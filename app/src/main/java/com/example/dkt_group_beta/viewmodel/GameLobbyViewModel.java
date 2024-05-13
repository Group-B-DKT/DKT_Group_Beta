package com.example.dkt_group_beta.viewmodel;

import android.util.Log;

import com.example.dkt_group_beta.activities.interfaces.GameLobbyAction;
import com.example.dkt_group_beta.communication.controller.ActionController;
import com.example.dkt_group_beta.communication.controller.InfoController;
import com.example.dkt_group_beta.communication.controller.WebsocketClientController;
import com.example.dkt_group_beta.communication.enums.Action;
import com.example.dkt_group_beta.communication.enums.Info;
import com.example.dkt_group_beta.model.Field;
import com.example.dkt_group_beta.model.Game;
import com.example.dkt_group_beta.model.GameInfo;
import com.example.dkt_group_beta.model.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameLobbyViewModel {
    private List<Player> connectedPlayers;
    private InfoController infoController;
    private ActionController actionController;
    private GameLobbyAction gameLobbyAction;

    private Player player;

    public GameLobbyViewModel(GameLobbyAction gameLobbyAction) {
        this.infoController = new InfoController(this::handleInfo);
        this.actionController = new ActionController(this::handleAction);
        this.gameLobbyAction = gameLobbyAction;
        this.connectedPlayers = new ArrayList<>();
        this.player = WebsocketClientController.getPlayer();
    }

    public void getConnectedPlayerNames() {
        infoController.getConnectedPlayers();
    }


    public void leaveGame() {
        actionController.leaveGame();
    }


    public void setReady () {
        player.setReady(!player.isReady());
        actionController.isReady(player.isReady());

    }

    public void startGame(){
        Log.d("DEBUG", "GameLobbyViewModel::startGame/ " + connectedPlayers);
        if (connectedPlayers.size() < Game.MIN_PLAYER){
            gameLobbyAction.assertInputDialog("Not enough Players connected to start the game!");
            return;
        }
        if (connectedPlayers.stream().filter(p -> !p.getId().equals(player.getId()) && p.isReady()).count() < connectedPlayers.size() - 1){
            gameLobbyAction.assertInputDialog("Not enough Players ready to start the game!");
            return;
        }
        List<Field> fields;
        fields = Field.loadFields(gameLobbyAction.getContext());
        actionController.gameStarted(fields);
    }


    public void handleInfo (Info info, List < GameInfo > gameInfos){
        Log.d("DEBUG", "GameLobbyViewModel::handleInfo/ " + gameInfos.get(0).getConnectedPlayers());
        GameInfo gameInfo = gameInfos.get(0);

        if (gameInfo == null) return;

        gameInfo.getConnectedPlayers()
                .forEach(g -> {
                    if (!this.connectedPlayers.contains(g)) {
                        if (g.getId().equals(player.getId()))
                            this.connectedPlayers.add(player);
                        else this.connectedPlayers.add(g);
                        gameLobbyAction.addPlayerToView(g);
                    }
                });
        }

    public void handleAction (Action action, String param, Player fromPlayer, List<Field> fields) {
        Log.d("DEBUG", "GameLobbyViewModel::handleAction/ " + action);


        if (action == Action.LEAVE_GAME) {
            handleLeaveGame(fromPlayer);
        }
        if (action == Action.HOST_CHANGED) {
            handleHostChanged(fromPlayer);
        }
        if (action == Action.GAME_JOINED_SUCCESSFULLY) {
            this.getConnectedPlayerNames();
        }
        if (action == Action.CHANGED_READY_STATUS) {
            handleChangedReadyStatus(fromPlayer);
        }
        if (action == Action.GAME_STARTED){
            handleGameStarted(fromPlayer, fields);
        }
    }

    private void handleGameStarted(Player fromPlayer, List<Field> fields) {
        Log.d("PL1", fromPlayer.getId() + "|  " +connectedPlayers.stream().map(p -> p.getId() + ": " + p.isOnTurn()).collect(Collectors.toList()));
        Player isOnTurnPlayer = this.connectedPlayers.stream()
                                                     .filter(p -> p.getId().equals(fromPlayer.getId()))
                                                     .findFirst().orElse(null);
        if (isOnTurnPlayer == null){
            return;
        }
        isOnTurnPlayer.setOnTurn(true);
        Log.d("PL2", isOnTurnPlayer.getId() + ": " + isOnTurnPlayer.isOnTurn());
        Log.d("PL3", fromPlayer.getId() + "|  " +connectedPlayers.stream().map(p -> p.getId() + ": " + p.isOnTurn()).collect(Collectors.toList()));
        gameLobbyAction.switchToGameBoard(this.connectedPlayers, fields);
    }

    private void handleChangedReadyStatus(Player fromPlayer) {
        Player changedPlayer = connectedPlayers.stream()
                        .filter(p -> p.getId().equals(fromPlayer.getId()))
                        .findFirst().orElse(null);
        if (changedPlayer == null){
            return;
        }
        changedPlayer.setReady(fromPlayer.isReady());

        Log.d("DEBUG", "GameLobbyViewModel::handleAction/ " + fromPlayer.getUsername());
        if (player.getId().equals(fromPlayer.getId())) {
            gameLobbyAction.changeReadyBtnText(fromPlayer.isReady());

        }
        gameLobbyAction.readyStateChanged(fromPlayer.getUsername(), fromPlayer.isReady());
    }

    private void handleHostChanged(Player fromPlayer) {
        if (fromPlayer.getId().equals(player.getId()) && !player.isHost()) {
            player.setHost(fromPlayer.isHost());
            gameLobbyAction.addStartButton();
        }

        this.connectedPlayers.remove(fromPlayer);
        this.connectedPlayers.add(fromPlayer);

        this.connectedPlayers.forEach(p -> {
            gameLobbyAction.removePlayerFromView(p);
            gameLobbyAction.addPlayerToView(p);
        });
    }

    private void handleLeaveGame(Player fromPlayer) {
        gameLobbyAction.removePlayerFromView(fromPlayer);
        if (player.getUsername().equals(fromPlayer.getUsername())) {
            gameLobbyAction.switchToGameLobby(fromPlayer);
            player.setHost(false);

        } else {
            this.connectedPlayers.remove(fromPlayer);
        }
    }
}
