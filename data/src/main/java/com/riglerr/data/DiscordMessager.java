package com.riglerr.data;

import com.riglerr.domain.entities.MessageContext;
import com.riglerr.domain.interfaces.Messager;
import net.dv8tion.jda.core.JDA;

public class DiscordMessager implements Messager {

    private JDA bot;
    private MessageContext currentMessageContext;

    public DiscordMessager(JDA jda) {
        bot = jda;
    }

    @Override
    public void sendMessage(String message) {
        bot.getGuildById(currentMessageContext.getGuidId())
            .getTextChannelById(currentMessageContext.getChannelId())
            .sendMessage(message).queue();
    }

    public void addContext(MessageContext messageContext) {
        currentMessageContext = messageContext;
    }
}
