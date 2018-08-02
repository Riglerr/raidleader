package com.riglerr.data.interfaces;

import com.riglerr.data.exceptions.CommandNotFoundException;
import com.riglerr.domain.entities.MessageContext;

public interface CommandModule {
    void execute(MessageContext context);
    String getModuleIdentifier();
    String getModulePrefix();
}
