package com.riglerr.domain.usecases;

import com.riglerr.domain.entities.Alert;
import com.riglerr.domain.entities.MessageContext;
import com.riglerr.domain.interfaces.AlertRepository;
import com.riglerr.domain.interfaces.Messager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CreateRaidAlertUseCaseTests {

    private AlertRepository repo = mock(AlertRepository.class);
    private Messager messager = mock(Messager.class);
    private CreateRaidAlertUseCase UC;
    private Alert expectedAlert;
    private MessageContext messageContext;
    private String ExpectedSuccessMessage = "Alert successfully created.";

    @BeforeEach
    void beforeEachCreateNewUseCaseObject() {
        UC  = new CreateRaidAlertUseCase(repo, messager);
    }

    @Test
    void successfullyParsesMessageAndAddsToRepository() throws Exception {
        String inputString = "!raidalert -t 15:30:00 -i P7D -m Raid Starting in 30 mins.";
        buildAlertAndContext(inputString);
        UC.createRaidAlert(messageContext);

        var actualAlert = captureArgumentForAddToRepository();

        assertEquals(expectedAlert, actualAlert);
    }

    @Test
    void successfullySendsSuccessMessage() throws Exception {
        String inputString = "!raidalert -t 15:30:00 -i P7D -m Raid Starting in 30 mins.";
        buildAlertAndContext(inputString);
        UC.createRaidAlert(messageContext);

        var actualMessage = captureArgumentForMessagerSendMessage();

        assertEquals(ExpectedSuccessMessage, actualMessage);
    }

    private void buildAlertAndContext(String message) throws Exception {
        messageContext = new MessageContext(message, 1, 1);
        expectedAlert = Alert.fromString(message);
    }

    private Alert captureArgumentForAddToRepository() {
        ArgumentCaptor<Alert> argument = ArgumentCaptor.forClass(Alert.class);
        verify(repo).add(argument.capture());
        return argument.getValue();
    }

    private String captureArgumentForMessagerSendMessage() {
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        verify(messager).sendMessage(argument.capture());
        return argument.getValue();
    }
}
