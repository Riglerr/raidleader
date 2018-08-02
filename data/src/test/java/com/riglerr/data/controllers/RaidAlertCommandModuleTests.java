package com.riglerr.data.controllers;

import com.riglerr.data.exceptions.CommandNotFoundException;
import com.riglerr.domain.entities.MessageContext;
import com.riglerr.domain.interfaces.AlertRepository;
import com.riglerr.domain.interfaces.Messager;
import com.riglerr.domain.usecases.CreateRaidAlertUseCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RaidAlertCommandModuleTests {

    @Test
    void identifiesAndRunsCreateRaidAlertUseCase() {
        // Build
        var mockUseCase = mock(CreateRaidAlertUseCase.class);
        var mockMessageContext = mock(MessageContext.class);
        var messager = mock(Messager.class);
        when(mockMessageContext.getText()).thenReturn("!raidalert -t 16:30 -m ThisisAMessage.");
        var module = new RaidAlertCommandModule(messager, mockUseCase);

        // Run
        module.execute(mockMessageContext);

        // Assert
        verify(mockUseCase).createRaidAlert(mockMessageContext);
    }

    @Test
    void shouldSendErrorMessageWhenNoMatchingCommandFound() {
        // Build
        var mockUseCase = mock(CreateRaidAlertUseCase.class);
        var mockMessageContext = mock(MessageContext.class);
        var messager = mock(Messager.class);

        when(mockMessageContext.getText()).thenReturn("!raidalert notCommand -t 16:30 -m ThisisAMessage.");
        var module = new RaidAlertCommandModule(messager, mockUseCase);

        module.execute(mockMessageContext);
        verify(messager).sendMessage("Invalid command 'notCommand'");
    }

}