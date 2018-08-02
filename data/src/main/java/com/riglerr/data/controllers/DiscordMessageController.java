package com.riglerr.data.controllers;

import com.riglerr.data.interfaces.CommandModule;
import com.riglerr.domain.entities.MessageContext;

import java.util.*;

public class DiscordMessageController {

    private HashMap<String, CommandModule> Modules;

    public DiscordMessageController(CommandModule... commandModules) {
        Modules = new HashMap<>();
        for (CommandModule commandModule : commandModules) {
            verifyModule(commandModule);
            Modules.put(getModuleKey(commandModule), commandModule);
        }
    }

    public List<CommandModule> getModules() {
        return new ArrayList<>(Modules.values());
    }

    public void processMessage(MessageContext context) {
        var key = context.getText().split(" ")[0];
        if (Modules.containsKey(key)) {
            Modules.get(key).execute(context);
        }
    }

    private String getModuleKey(CommandModule module) {
        return String.format("%s%s", module.getModulePrefix(), module.getModuleIdentifier());
    }

    private void verifyModule(CommandModule module) {
        if (module.getModulePrefix() == null || module.getModulePrefix().isEmpty()) {
            throw new RuntimeException("Module prefix is null or empty.");
        }
        if (module.getModuleIdentifier() == null || module.getModuleIdentifier().isEmpty()) {
            throw new RuntimeException("Module identifier is null or empty.");
        }
        if (Modules.containsKey(getModuleKey(module))) {
            throw new RuntimeException("Duplicate CommandModule added.");
        }
    }

}
