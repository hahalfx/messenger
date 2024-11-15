package com.projectgp.messenger.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.projectgp.messenger.model.MessageTask;


public interface MessageTaskService extends IService<MessageTask> {
    // 业务逻辑方法，如创建、更新、删除任务
    // 创建消息任务
    public void createMessageTask(MessageTask messageTask);
    // 删除消息任务
    public void deleteMessageTask(long Id);
    // 修改消息任务
    public void updateMessageTask(MessageTask messageTask);
    // 查询消息任务
    public MessageTask getMessageTaskById(long Id);
    
}
