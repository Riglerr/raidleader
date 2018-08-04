package com.riglerr.presentation.discord;

import com.riglerr.data.controllers.DiscordMessageController;
import com.riglerr.data.mappers.MessageContextMapper;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

    private DiscordMessageController messageController;

    public MessageListener(DiscordMessageController messageController) {
        this.messageController = messageController;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        var messageContext = MessageContextMapper.map(event);
        messageController.processMessage(messageContext);
    }
}
