package com.riglerr.data;

import com.riglerr.domain.entities.MessageContext;
import com.riglerr.domain.interfaces.Messenger;
import net.dv8tion.jda.core.JDA;

public class DiscordMessenger implements Messenger {

    private JDA bot;

    public DiscordMessenger(JDA jda) {
        bot = jda;
    }

    @Override
    public void sendMessage(MessageContext messageContext) {
        bot.getGuildById(messageContext.getGuidId())
            .getTextChannelById(messageContext.getChannelId())
            .sendMessage(messageContext.getText()).queue();
    }
}
