package com.riglerr.presentation.discord;

import com.riglerr.data.CommandStore;
import com.riglerr.data.DiscordMessager;
import com.riglerr.data.mappers.MessageContextMapper;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

    private CommandStore commandStore;
    private DiscordMessager messager;

    public MessageListener(CommandStore commandStore, DiscordMessager messager) {
        this.commandStore = commandStore;
        this.messager = messager;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        var messageContext = MessageContextMapper.map(event);
        messager.addContext(messageContext);
        commandStore.executeCommand(messageContext);
    }
}
