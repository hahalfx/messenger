package com.projectgp.messenger.service;

import com.projectgp.messenger.model.MessageTask;

public interface MessageSendService {
    public MessageTask sendMessage(MessageTask messageTask);
    
}
