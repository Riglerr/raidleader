package com.riglerr.presentation.discord;

import com.riglerr.data.command.CommandStore;
import com.riglerr.data.DiscordMessenger;
import com.riglerr.data.mappers.MessageContextMapper;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

    private CommandStore commandStore;

    public MessageListener(CommandStore commandStore) {
        this.commandStore = commandStore;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        var messageContext = MessageContextMapper.map(event);
        commandStore.executeCommand(messageContext);
    }
}
