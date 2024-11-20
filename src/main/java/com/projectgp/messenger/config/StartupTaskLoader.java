package com.projectgp.messenger.config;

import java.util.List;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.projectgp.messenger.model.MessageTask;
import com.projectgp.messenger.service.MessageSchedulerService;
import com.projectgp.messenger.service.MessageTaskService;

@Component
public class StartupTaskLoader {

    @Autowired
    private MessageTaskService messageTaskService;

    @Autowired
    private MessageSchedulerService messageSchedulerService;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        // 加载需要调度的任务
        List<MessageTask> tasks = messageTaskService.getAllActiveTasks();
        for (MessageTask task : tasks) {
            try {
                messageSchedulerService.scheduleMessageTask(task);
            } catch (SchedulerException e) {
                // 处理调度异常
                e.printStackTrace();
            }
        }
    }

}
