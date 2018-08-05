package com.riglerr.domain.interfaces;

import com.riglerr.domain.entities.MessageContext;

public interface UseCase {
    public void execute(MessageContext messageContext);
}
