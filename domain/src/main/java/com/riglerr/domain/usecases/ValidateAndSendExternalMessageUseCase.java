package com.riglerr.domain.usecases;

import com.riglerr.domain.entities.MessageContext;
import com.riglerr.domain.interfaces.MessageValidator;
import com.riglerr.domain.interfaces.Messenger;
import com.riglerr.domain.interfaces.UseCase;

public class ValidateAndSendExternalMessageUseCase implements UseCase {

    private MessageValidator validator;
    private Messenger messenger;

    public ValidateAndSendExternalMessageUseCase(MessageValidator validator, Messenger messenger) {
        this.validator = validator;
        this.messenger = messenger;
    }

    @Override
    public void execute(MessageContext messageContext) {

    }
}
