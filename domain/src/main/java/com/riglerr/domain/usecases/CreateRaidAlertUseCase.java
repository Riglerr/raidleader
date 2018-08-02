package com.riglerr.domain.usecases;

import com.riglerr.domain.entities.MessageContext;
import com.riglerr.domain.interfaces.AlertRepository;
import com.riglerr.domain.interfaces.Messager;
import com.riglerr.domain.util.AlertMessageParser;

public class CreateRaidAlertUseCase {
    private AlertRepository alertRepository;
    private Messager messager;

    public CreateRaidAlertUseCase(AlertRepository alertRepository, Messager messager) {
        this.alertRepository = alertRepository;
        this.messager = messager;
    }

    public void createRaidAlert(MessageContext messageContext) {
        try {
            parseAndCreateAlert(messageContext);
        } catch (Exception e) {
            e.printStackTrace();
            messager.sendMessage(e.getMessage());
        }
    }

    private void parseAndCreateAlert(MessageContext messageContext) throws Exception {
        var alert = AlertMessageParser.parse(messageContext);
        alertRepository.add(alert);
        messager.sendMessage("Alert successfully created.");
    }


}
