package com.example.dkt_group_beta.viewmodel;

import static com.example.dkt_group_beta.communication.enums.Info.GAME_LIST;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.dkt_group_beta.activities.interfaces.GameSearchAction;
import com.example.dkt_group_beta.communication.controller.ActionController;
import com.example.dkt_group_beta.communication.controller.ConnectController;
import com.example.dkt_group_beta.communication.controller.InfoController;
import com.example.dkt_group_beta.communication.enums.Info;
import com.example.dkt_group_beta.model.GameInfo;

import java.util.Arrays;
import java.util.List;

public class GameSearchViewModelTest {
    private GameSearchViewModel gameSearchViewModel;
    private InfoController infoControllerMock;
    private ConnectController connectControllerMock;
    private ActionController actionControllerMock;
    private GameSearchAction gameSearchActionMock;

    @Before
    public void setUp() {
        String uri = "http://uri.com";
        String username = "username";
        String id = "id";
        gameSearchActionMock = mock(GameSearchAction.class);
        gameSearchViewModel = new GameSearchViewModel(uri, username, id, gameSearchActionMock);
        infoControllerMock = mock(InfoController.class);
        gameSearchViewModel.infoController = infoControllerMock;
        actionControllerMock = mock(ActionController.class);
        gameSearchViewModel.actionController = actionControllerMock;

    }

    @Test
    public void testReceiveGames() {
        gameSearchViewModel.receiveGames();
        verify(infoControllerMock, times(1)).getGameListFromServer();
    }
    @Test
    public void testCreateGame() {
        String inputText = "input";
        gameSearchViewModel.createGame(inputText);
        verify(actionControllerMock, times(1)).createGame(inputText);
    }
    @Test
    public void testHandleInfo() {
        Info info = GAME_LIST;
        List<GameInfo> gameInfos = Arrays.asList(new GameInfo(1, "Game 1", 4), new GameInfo(2, "Game 2", 3));

        gameSearchViewModel.handleInfo(info, gameInfos);
        verify(gameSearchActionMock, times(1)).refreshGameListItems();
        verify(gameSearchActionMock, times(1)).addGameToScrollView(1, "Game 1", 4);
        verify(gameSearchActionMock, times(1)).addGameToScrollView(2, "Game 2", 3);
    }
    @Test
    public void testHandleInfoNull() {
        Info info = GAME_LIST;
        gameSearchViewModel.handleInfo(info, null);

        verify(gameSearchActionMock, times(0)).refreshGameListItems();
        verify(gameSearchActionMock, times(0)).addGameToScrollView(0, "", 0);
    }
}
