package com.projectgp.messenger.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.projectgp.messenger.model.MessageTask;


public interface MessageTaskService extends IService<MessageTask> {
    // 业务逻辑方法，如创建、更新、删除任务
    // 创建消息任务
    public void createMessageTask(MessageTask messageTask);
    // 删除消息任务
    public void deleteMessageTask(long Id);
    public void deleteMessageTasks(long[] Ids);
    // 修改消息任务
    public void updateMessageTask(MessageTask messageTask);
    // 查询消息任务
    public MessageTask getMessageTaskById(long Id);

    public void pauseMessageTask(long[] Ids);

    public void resumeMessageTask(long[] Ids);

    public void updateAliveToNo(long Id);

    //获取所有激活状态的任务
    public List<MessageTask> getAllActiveTasks();
    // 消息任务状态检查
    public void AddTasktoscheduler(MessageTask messageTask);
    // 消息任务请求参数检查
    public String requestCheck(MessageTask messageTask);
}
