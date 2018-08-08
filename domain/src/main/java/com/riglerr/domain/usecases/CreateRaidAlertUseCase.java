package com.riglerr.domain.usecases;

import com.riglerr.domain.entities.MessageContext;
import com.riglerr.domain.interfaces.AlertRepository;
import com.riglerr.domain.interfaces.Messenger;
import com.riglerr.domain.interfaces.UseCase;
import com.riglerr.domain.util.AlertMessageParser;

public class CreateRaidAlertUseCase implements UseCase {
    private AlertRepository alertRepository;
    private Messenger messenger;

    public CreateRaidAlertUseCase(AlertRepository alertRepository, Messenger messenger) {
        this.alertRepository = alertRepository;
        this.messenger = messenger;
    }

    @Override
    public void execute(MessageContext messageContext) {
        try {
            parseAndCreateAlert(messageContext);
        } catch (Exception e) {
            e.printStackTrace();
            messenger.sendMessage(new MessageContext(e.getMessage(),
                    messageContext.getGuidId(), messageContext.getChannelId()));
        }
    }

    private void parseAndCreateAlert(MessageContext messageContext) throws Exception {
        var alert = AlertMessageParser.parse(messageContext);
        alertRepository.add(alert);
        messenger.sendMessage(new MessageContext("Alert successfully created.", messageContext.getGuidId(), messageContext.getChannelId()));
    }


}
