package com.riglerr.domain.usecases;

import com.riglerr.domain.entities.Alert;
import com.riglerr.domain.entities.MessageContext;
import com.riglerr.domain.interfaces.AlertRepository;
import com.riglerr.domain.interfaces.Messenger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CreateRaidAlertUseCaseTests {

    private AlertRepository repo = mock(AlertRepository.class);
    private Messenger messenger = mock(Messenger.class);
    private CreateRaidAlertUseCase UC;
    private Alert expectedAlert;
    private MessageContext messageContext;

    @BeforeEach
    void beforeEachCreateNewUseCaseObject() {
        UC  = new CreateRaidAlertUseCase(repo, messenger);
    }

    @Test
    void successfullyParsesMessageAndAddsToRepository() throws Exception {
        String inputString = "!raidalert -t 15:30:00 -i P7D -m Raid Starting in 30 mins.";
        buildAlertAndContext(inputString);
        UC.execute(messageContext);

        var actualAlert = captureArgumentForAddToRepository();

        assertEquals(expectedAlert, actualAlert);
    }

    @Test
    void successfullySendsSuccessMessage() throws Exception {
        String inputString = "!raidalert -t 15:30:00 -i P7D -m Raid Starting in 30 mins.";
        buildAlertAndContext(inputString);
        UC.execute(messageContext);

        var actualMessage = captureArgumentForMessengerSendMessage();

        String expectedSuccessMessage = "Alert successfully created.";
        long expectedGuildId = messageContext.getGuidId();
        long expectedChannelId = messageContext.getChannelId();

        assertEquals(expectedSuccessMessage, actualMessage.getText());
        assertEquals(expectedGuildId, actualMessage.getGuidId());
        assertEquals(expectedChannelId, actualMessage.getChannelId());
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

    private MessageContext captureArgumentForMessengerSendMessage() {
        ArgumentCaptor<MessageContext> argument = ArgumentCaptor.forClass(MessageContext.class);
        verify(messenger).sendMessage(argument.capture());
        return argument.getValue();
    }
}
