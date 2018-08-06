package com.riglerr.domain.interfaces;

import com.riglerr.domain.entities.MessageContext;

public interface UseCase {
    void execute(MessageContext messageContext);
}
