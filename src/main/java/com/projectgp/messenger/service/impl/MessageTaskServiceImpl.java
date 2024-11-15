package com.projectgp.messenger.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.projectgp.messenger.mapper.MessageTaskMapper;
import com.projectgp.messenger.model.MessageTask;
import com.projectgp.messenger.service.MessageTaskService;

@Service
public class MessageTaskServiceImpl extends ServiceImpl<MessageTaskMapper, MessageTask> implements MessageTaskService {

    @Autowired
    private MessageTaskMapper messageTaskMapper;

    @Override
    public void createMessageTask(MessageTask messageTask) {
        //保存消息任务到数据库中
        messageTaskMapper.insert(messageTask);
    }

    @Override
    public void deleteMessageTask(long Id) {
        messageTaskMapper.deleteById(Id);
    }

    @Override
    public void updateMessageTask(MessageTask messageTask) {
        messageTaskMapper.updateById(messageTask);
    }

    @Override
    public MessageTask getMessageTaskById(long Id) {
        return messageTaskMapper.selectById(Id);
    }


}
