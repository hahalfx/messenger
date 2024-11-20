package com.projectgp.messenger.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.projectgp.messenger.mapper.MessageTaskMapper;
import com.projectgp.messenger.model.MessageTask;
import com.projectgp.messenger.service.MessageSchedulerService;
import com.projectgp.messenger.service.MessageSendService;
import com.projectgp.messenger.service.MessageTaskService;

@Service
public class MessageTaskServiceImpl extends ServiceImpl<MessageTaskMapper, MessageTask> implements MessageTaskService {

    @Autowired
    private MessageTaskMapper messageTaskMapper;

    @Autowired
    public MessageSendService messageSendService;

    @Autowired
    private MessageSchedulerService quartzSchedulerService;

    @Override
    public void createMessageTask(MessageTask messageTask) {
        // 保存消息任务到数据库中
        messageTaskMapper.insert(messageTask);
    }

    @Override
    public void deleteMessageTask(long Id) {
        messageTaskMapper.deleteById(Id);
        //删除任务调度器中的任务
        try {
            quartzSchedulerService.deleteMessageTask(Id);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteMessageTasks(long[] Ids) {
        for(long id : Ids){
            try {
                quartzSchedulerService.deleteMessageTask(id);
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }
        List<Long> taskIds = Arrays.stream(Ids)
                .boxed()
                .collect(Collectors.toList());
        messageTaskMapper.deleteByIds(taskIds);
        
    }

    @Override
    public void updateMessageTask(MessageTask messageTask) {
        messageTaskMapper.updateById(messageTask);
        try {
            quartzSchedulerService.deleteMessageTask(messageTask.getTaskId());
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        try {
            quartzSchedulerService.scheduleMessageTask(getMessageTaskById(messageTask.getTaskId()));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public MessageTask getMessageTaskById(long Id) {
        return messageTaskMapper.selectById(Id);
    }

    @Override
    public void AddTasktoscheduler(MessageTask messageTask) {
        // 调度任务
        try {
            quartzSchedulerService.scheduleMessageTask(messageTask);
        } catch (SchedulerException e) {
            // 处理调度异常
            e.printStackTrace();
        }
    }

    public void updateAliveToNo(long taskId) {
        // 更新数据库 ALIVE 属性为 null
        messageTaskMapper.updateAliveToNo(taskId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public String requestCheck(MessageTask messageTask) {
        List<String> missingAttributes = new ArrayList<>();

        // 检查 messageTask 是否为 null
        if (messageTask == null) {
            return "MessageTask is null";
        }

        // 检查 SenderRecipient 和 ChannelList
        if (messageTask.getSenderRecipient() == null) {
            missingAttributes.add("SenderRecipient");
        } else {
            Map<String, Object> senderRecipient = messageTask.getSenderRecipient();
            Map<String, Object> sender = (Map<String, Object>) senderRecipient.get("sender");

            if (sender == null) {
                missingAttributes.add("Sender");
            } else {
                String senderName = (String) sender.get("name");
                Integer senderPhoneNumber = (Integer) sender.get("phone");
                String senderEmail = (String) sender.get("email");

                if (isNullOrEmpty(senderName)) {
                    missingAttributes.add("SenderName");
                }

                if (messageTask.getchannelList() != null) {
                    for (String channel : messageTask.getchannelList()) {
                        if ("sms".equalsIgnoreCase(channel)) {
                            if (senderPhoneNumber == null || senderPhoneNumber == 0) {
                                missingAttributes.add("SenderPhoneNumber");
                            }
                        } else if ("email".equalsIgnoreCase(channel)) {
                            if (isNullOrEmpty(senderEmail)) {
                                missingAttributes.add("SenderEmail");
                            }
                        }
                    }
                } else {
                    missingAttributes.add("ChannelList");
                }
            }
        }

        // 检查 ReceiverInformation 和 ChannelList
        if (messageTask.getReceiverInformation() == null) {
            missingAttributes.add("ReceiverInformation");
        } else {
            Map<String, Object> receiverInformation = messageTask.getReceiverInformation();
            Map<String, Object> receiver = (Map<String, Object>) receiverInformation.get("receiver");

            if (receiver == null) {
                missingAttributes.add("Receiver");
            } else {
                String receiverName = (String) receiver.get("name");
                Integer receiverPhoneNumber = (Integer) receiver.get("phone");
                String receiverEmail = (String) receiver.get("email");

                System.out.println(receiverName + "\n" + receiverEmail + "\n" + receiverPhoneNumber);

                if (isNullOrEmpty(receiverName)) {
                    missingAttributes.add("ReceiverName");
                }

                if (messageTask.getchannelList() != null) {
                    for (String channel : messageTask.getchannelList()) {
                        if ("sms".equalsIgnoreCase(channel)) {
                            if (receiverPhoneNumber == null || receiverPhoneNumber == 0) {
                                missingAttributes.add("ReceiverPhoneNumber");
                            }
                        } else if ("email".equalsIgnoreCase(channel)) {
                            if (isNullOrEmpty(receiverEmail)) {
                                missingAttributes.add("ReceiverEmail");
                            }
                        }
                    }
                } else {
                    missingAttributes.add("ChannelList");
                }
            }
        }

        // 检查 TemplateId 和 Content
        if (isNullOrEmpty(messageTask.getContent())) {
            if (messageTask.getTemplateId() == null) {
                missingAttributes.add("Content");
            } else if (isNullOrEmpty(messageTask.getAttributeJson())) {
                missingAttributes.add("PlaceholderAttribute");
            }
        }

        // 检查 TimeType
        if (messageTask.getTimeType() == null) {
            missingAttributes.add("TimeType");
        }

        if(messageTask.getTimeType().equalsIgnoreCase("CYCLED")){
            if(messageTask.getRepeatCount() == null){
                missingAttributes.add( "RepeatCount");
            }
            if(messageTask.getRepeatInterval()==null){
                missingAttributes.add("RepeatInterval");
            }
        }

        if("SCHEDULED".equalsIgnoreCase(messageTask.getTimeType())){
            if(messageTask.getSendTime() == null){
                missingAttributes.add( "SendTime");
            }
        }

        // 返回结果
        if (missingAttributes.isEmpty()) {
            return "NoMissingAttribute";
        } else {
            return "MissingAttribute: " + String.join(", ", missingAttributes);
        }
        
    }

    // 辅助方法：检查字符串是否为 null 或空
    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    //获取所有激活的循环或者定时任务
    @Override
    public List<MessageTask> getAllActiveTasks() {
        LambdaQueryWrapper<MessageTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MessageTask::getAlive, "YES")
                    .eq(MessageTask::getTimeType, "SCHEDULED")
                    .or()
                    .eq(MessageTask::getAlive, "YES")
                    .eq(MessageTask::getTimeType, "CYCLED"); // 查询条件：ALIVE = 'YES'

        // 查询结果
        return messageTaskMapper.selectList(queryWrapper);
    }

    //暂停消息任务
    @Override
    public void pauseMessageTask(long[] Ids) {
        for(long id : Ids){
            MessageTask messageTask = getMessageTaskById(id);
            messageTask.setAlive("NO");
            updateMessageTask(messageTask);
            try {
                quartzSchedulerService.pauseMessageTask(id);
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }
    }

    //恢复消息任务
    @Override
    public void resumeMessageTask(long[] Ids) {
        for(long id : Ids){
            MessageTask messageTask = getMessageTaskById(id);
            messageTask.setAlive("YES");
            updateMessageTask(messageTask);
            try {
                quartzSchedulerService.resumeMessageTask(id);
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }
    }

}
