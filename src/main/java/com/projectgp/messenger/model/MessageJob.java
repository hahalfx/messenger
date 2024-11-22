package com.projectgp.messenger.model;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.projectgp.messenger.service.MessageSendService;
import com.projectgp.messenger.service.MessageTaskService;

@Component
public class MessageJob implements Job {

    @Autowired
    private MessageTaskService messageTaskService;

    @Autowired
    private MessageSendService messageSendService;

    // 声明 Logger
    private static final Logger logger = LoggerFactory.getLogger(MessageJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // 获取再任务调度器中的taskId
        Long taskId = context.getJobDetail().getJobDataMap().getLong("taskId");

        // 根据 taskId 获取 MessageTask
        MessageTask messagetask = messageTaskService.getMessageTaskById(taskId);
        if (messagetask != null) {
            try {
                // 执行发送消息的逻辑
                messageSendService.sendMessage(messagetask);
                messageTaskService.updateMessageTask(messagetask);
                logger.info("成功发送任务 ID 为 {} 的消息。", taskId);
            } catch (Exception e) {
                logger.error("发送任务 ID 为 {} 的消息时发生异常。", taskId, e);
                throw new JobExecutionException("Failed to send message", e);
            }
            // 更新任务状态或处理逻辑
        } else {
            // 处理任务不存在的情况
            System.out.println("taskId 不存在");
        }
    }

}
