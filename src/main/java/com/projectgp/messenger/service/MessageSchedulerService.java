package com.projectgp.messenger.service;

import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;

import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projectgp.messenger.model.MessageJob;
import com.projectgp.messenger.model.MessageTask;

import jakarta.annotation.PostConstruct;

@Service
public class MessageSchedulerService {

    private static final Logger logger = LoggerFactory.getLogger(MessageSchedulerService.class);

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private MessageTriggerListener messageTriggerListener; // 自定义监听器

    public void scheduleMessageTask(MessageTask messagetask) throws SchedulerException {
        // 定义一个任务调度实例，将该实例与HelloJob绑定，任务类需要实现Job接口
        JobDetail jobDetail = JobBuilder.newJob(MessageJob.class)
                .withIdentity("messageJob-" + messagetask.getTaskId(), "messageGroup")
                .usingJobData("taskId", messagetask.getTaskId())
                .build();

        // 定义触发器
        Trigger trigger;

        if ("SCHEDULED".equalsIgnoreCase(messagetask.getTimeType())) {
            String cronExpression = messagetask.getCronExpression();

            // 验证 Cron 表达式的有效性
            if (!CronExpression.isValidExpression(cronExpression)) {
                throw new SchedulerException("无效的 Cron 表达式: " + cronExpression);
            }

            // 设置时区（根据需要调整）
            TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");

            // 创建 Cron Trigger
            trigger = TriggerBuilder.newTrigger()
                    .withIdentity("messageTrigger-" + messagetask.getTaskId(), "messageGroup")
                    .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)
                            .inTimeZone(timeZone))
                    .build();
        } else if ("CYCLED".equalsIgnoreCase(messagetask.getTimeType())) {

            Integer repeatInterval = messagetask.getRepeatInterval();
            Integer repeatCount = messagetask.getRepeatCount();

            if (repeatInterval == null) {
                throw new SchedulerException("repeatInterval is null or empty for task ID: " + messagetask.getTaskId());
            }
            if (repeatCount == null) {
                throw new SchedulerException("repeatCount is null or empty for task ID: " + messagetask.getTaskId());
            }
            if (messagetask.getSendTime() == null) {
                System.out.println("MessageTask ID " + messagetask.getTaskId() + " 的 sendTime 为 null，跳过调度。");
                return;
            } else {// 使用简单的间隔和重复次数
                trigger = TriggerBuilder.newTrigger()
                        .withIdentity("messageTrigger-" + messagetask.getTaskId(), "messageGroup")
                        .startAt(Date.from(messagetask.getSendTime().atZone(ZoneId.systemDefault()).toInstant()))
                        .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInMinutes(messagetask.getRepeatInterval())
                                .withRepeatCount(messagetask.getRepeatCount()))
                        .build();
            }
        } else {
            // 立即执行一次
            trigger = TriggerBuilder.newTrigger()
                    .withIdentity("messageTrigger-" + messagetask.getTaskId(), "messageGroup")
                    .startNow()
                    .build();
        }

        // 检查是否重复调度
        if (scheduler.checkExists(trigger.getKey())) {
            logger.warn("Trigger already exists for task ID: {}. Skipping scheduling.", messagetask.getTaskId());
            return;
        }

        // 使用触发器调度任务的执行
        scheduler.scheduleJob(jobDetail, trigger);
        logger.info("Scheduled task with ID: {}", messagetask.getTaskId());
    }

    public void pauseMessageTask(Long taskId) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey("messageJob-" + taskId, "messageGroup");
        scheduler.pauseJob(jobKey);
    }

    public void resumeMessageTask(Long taskId) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey("messageJob-" + taskId, "messageGroup");
        scheduler.resumeJob(jobKey);
    }

    public void deleteMessageTask(Long taskId) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey("messageJob-" + taskId, "messageGroup");
        scheduler.deleteJob(jobKey);
    }

    @PostConstruct
    public void registerTriggerListener() {
        try {
            scheduler.getListenerManager().addTriggerListener(messageTriggerListener);
            logger.info("MessageTriggerListener registered.");
        } catch (SchedulerException e) {
            logger.error("Failed to register MessageTriggerListener.", e);
        }
    }
}
