package com.riglerr.domain.entities;
import com.riglerr.domain.util.AlertMessageParser;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class Alert {
    private LocalTime time;
    private long intervalInSeconds;
    private String messageText;
    private long guildId;
    private long channelId;
    private LocalDateTime dateCreated;

    public Alert() {

    }

    public Alert(LocalTime time, long intervalInSeconds, String messageText, long guildId, long channelId) {
        this.time = time;
        this.intervalInSeconds = intervalInSeconds;
        this.messageText = messageText;
        this.guildId = guildId;
        this.channelId = channelId;
        this.dateCreated = LocalDateTime.now();
    }

    public LocalTime getTime() {
        return time;
    }

    public long getIntervalInSeconds() {
        return intervalInSeconds;
    }

    public String getMessageText() {
        return messageText;
    }

    public long getGuildId() {
        return guildId;
    }

    public long getChannelId() {
        return channelId;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Alert)) {
            return false;
        }
        var alertObj =  (Alert) obj;
        if (this.time.equals(alertObj.time) && this.intervalInSeconds == alertObj.intervalInSeconds
                && this.messageText.equals(alertObj.messageText) && this.guildId == alertObj.guildId
                && this.channelId == alertObj.channelId) {
            return true;
        }
        return false;
    }

    public static Alert fromString(String message) throws Exception {
        return AlertMessageParser.parse(new MessageContext(message, 1, 1));
    }

}
