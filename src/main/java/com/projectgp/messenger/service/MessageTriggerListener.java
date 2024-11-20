package com.projectgp.messenger.service;

import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.TriggerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projectgp.messenger.mapper.MessageTaskMapper;

import java.time.LocalDateTime;

import org.quartz.JobExecutionContext;

@Service
public class MessageTriggerListener implements TriggerListener {

    private static final Logger logger = LoggerFactory.getLogger(MessageTriggerListener.class);

    @Autowired
    private MessageTaskMapper messageTaskMapper; // 用于更新数据库

    @Override
    public String getName() {
        return "MessageTriggerListener";
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
        // 可选：触发器启动时的逻辑
        logger.info("Trigger fired: {}", trigger.getKey().getName());
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
        // 返回 false 表示不拦截任务的执行
        return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
        // 可选：触发器错过执行的逻辑
        logger.warn("Trigger misfired: {}", trigger.getKey().getName());
    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext context,
            Trigger.CompletedExecutionInstruction triggerInstructionCode) {
        // 获取任务 ID
        Long taskId = (Long) context.getJobDetail().getJobDataMap().get("taskId");


        if (taskId != null) {
            // 更新任务状态 ALIVE 为 "NO"
            messageTaskMapper.updateAliveToNo(taskId);
            // 更新任务实际发送时间
            messageTaskMapper.updateActualSendTime(taskId, LocalDateTime.now());
            logger.info("Task with ID {} completed and ALIVE set to NO.", taskId);
        } else {
            logger.warn("Task ID is null, unable to update ALIVE status.");
        }
    }

}
