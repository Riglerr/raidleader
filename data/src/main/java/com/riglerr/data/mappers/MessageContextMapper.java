package com.riglerr.data.mappers;

import com.riglerr.domain.entities.MessageContext;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * MessageContextMapper
 * @description - Maps DV8 events into domain MessageContexts
 */
public class MessageContextMapper {
    public static MessageContext map(MessageReceivedEvent event) {
        if (event == null) {
            throw new RuntimeException("Cannot map null event.");
        }
        var messageContext = new MessageContext(event.getMessage().getContentDisplay(),
                event.getGuild().getIdLong(), event.getChannel().getIdLong());
        return messageContext;
    }
}
