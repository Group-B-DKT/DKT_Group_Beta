package com.example.dkt_group_beta.viewmodel;

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

    }

    @Test
    public void testReceiveGames() {
        gameSearchViewModel.receiveGames();
        verify(infoControllerMock, times(1)).getGameListFromServer();
    }


}
