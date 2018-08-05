package com.riglerr.data.controllers;

import com.riglerr.data.exceptions.CommandNotFoundException;
import com.riglerr.data.interfaces.CommandModule;
import com.riglerr.domain.entities.Alert;
import com.riglerr.domain.entities.MessageContext;
import com.riglerr.domain.interfaces.AlertRepository;
import com.riglerr.domain.interfaces.Messager;
import com.riglerr.domain.usecases.CreateRaidAlertUseCase;

public class RaidAlertCommandModule implements CommandModule {
    private String Prefix = "!";
    private String Identifier = "raidalert";
    private Messager messager;
    private CreateRaidAlertUseCase createRaidAlertUseCase;

    public RaidAlertCommandModule(Messager messager, CreateRaidAlertUseCase createRaidAlertUseCase) {
        this.messager = messager;
        this.createRaidAlertUseCase = createRaidAlertUseCase;
    }

    @Override
    public void execute(MessageContext context) {
        try {
            identifyAndExecuteCommand(context);
        } catch (CommandNotFoundException ex) {
            messager.sendMessage(String.format("Invalid command '%s'", ex.getMessage()));
        }
    }

    private void identifyAndExecuteCommand(MessageContext context) throws CommandNotFoundException {
        var split = context.getText().split(" ");

        var command = split[1];
        switch (command) {
            case "-t": {
                createRaidAlertUseCase.execute(context);
                break;
            }
            default: {
                throw new CommandNotFoundException(command);
            }
        }
    }

    @Override
    public String getModuleIdentifier() {
        return Identifier;
    }

    @Override
    public String getModulePrefix() {
        return Prefix;
    }
}
