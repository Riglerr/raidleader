package com.riglerr.domain.interfaces;

import com.riglerr.domain.entities.MessageContext;

public interface Messenger {
    void sendMessage(MessageContext messageContext);
}
