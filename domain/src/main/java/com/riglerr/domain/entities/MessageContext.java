package com.riglerr.domain.entities;

public class MessageContext {
    private String text;
    private long guidId;
    private long channelId;

    public MessageContext(String text, long guidId, long channelId) {
        this.text = text;
        this.guidId = guidId;
        this.channelId = channelId;
    }

    public MessageContext(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public long getGuidId() {
        return guidId;
    }

    public long getChannelId() {
        return channelId;
    }
}
