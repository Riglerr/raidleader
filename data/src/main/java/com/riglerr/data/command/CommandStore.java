package com.riglerr.data;

import com.riglerr.data.exceptions.DuplicateCommandException;
import com.riglerr.data.exceptions.InvalidCommandException;
import com.riglerr.domain.entities.MessageContext;
import com.riglerr.domain.interfaces.UseCase;

import java.util.HashMap;

public class CommandStore {
    private HashMap<String, UseCase> commands;
    
    public CommandStore() {
        commands = new HashMap<>();
    }

    public void addCommand(String prefix, String moduleIdentifier, String commandIdentifier, UseCase useCase) {
        verifyCommand(prefix, moduleIdentifier, commandIdentifier, useCase);
        var key = makeCommandKey(prefix, moduleIdentifier, commandIdentifier);
        if (commands.containsKey(key)) {
            throw new DuplicateCommandException();
        }
        commands.put(key, useCase);
    }

    public HashMap<String, UseCase> getCommands() {
        return commands;
    }

    public void executeCommand(MessageContext message) {
        var splitMessage = message.getText().split(" ");
        var prefix = splitMessage[0].substring(0, 1);
        var moduleIdentifier = splitMessage[0].substring(1);
        var commandIdentifier = splitMessage[1];
        var key = makeCommandKey(prefix, moduleIdentifier, commandIdentifier);
        if (commands.containsKey(key)) {
            commands.get(key).execute(message);
        }
    }

    private String makeCommandKey(String prefix, String identifier, String commandIdentifier) {
        return String.format("%s %s %s",prefix, identifier, commandIdentifier);
    }

    private void verifyCommand(String prefix, String moduleIdentifier,
                               String commandIdentifier, UseCase useCase) {
        if (prefix == null || prefix.isEmpty()) {
            throw new InvalidCommandException("Prefix is Null or Empty");
        }
        if (moduleIdentifier == null || moduleIdentifier.isEmpty()) {
            throw new InvalidCommandException("ModuleIdentifier is Null or Empty");
        }
        if (commandIdentifier == null || commandIdentifier.isEmpty()) {
            throw new InvalidCommandException("CommandIdentifier is Null or Empty");
        }
        if (useCase == null) {
            throw new InvalidCommandException("Usecase cannot be null");
        }

    }


}
