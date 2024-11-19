package com.projectgp.messenger.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.projectgp.messenger.mapper.MessageTaskMapper;
import com.projectgp.messenger.model.MessageTask;
import com.projectgp.messenger.service.MessageSendService;
import com.projectgp.messenger.service.MessageTaskService;

@Service
public class MessageTaskServiceImpl extends ServiceImpl<MessageTaskMapper, MessageTask> implements MessageTaskService {

    @Autowired
    private MessageTaskMapper messageTaskMapper;

    @Autowired
    public MessageSendService messageSendService;

    @Override
    public void createMessageTask(MessageTask messageTask) {
        // 保存消息任务到数据库中
        messageTaskMapper.insert(messageTask);
    }

    @Override
    public void deleteMessageTask(long Id) {
        messageTaskMapper.deleteById(Id);
    }

    @Override
    public void deleteMessageTasks(long[] Ids) {
        List<Long> taskIds = Arrays.stream(Ids)
                .boxed()
                .collect(Collectors.toList());
        messageTaskMapper.deleteByIds(taskIds);
    }

    @Override
    public void updateMessageTask(MessageTask messageTask) {
        messageTaskMapper.updateById(messageTask);
    }

    @Override
    public MessageTask getMessageTaskById(long Id) {
        return messageTaskMapper.selectById(Id);
    }

    @Override
    public void taskCheck(MessageTask messageTask) {
        //

        // 判断是否为立即发送
        if (messageTask.getTimeType().equals("IMMEDIATE")) {
            // 调用消息发送服务
            System.out.println("调用了消息发送服务");
            // 发送消息同时更新Task信息到数据库中
            MessageTask updatedTask = messageSendService.sendMessage(messageTask);
            updateMessageTask(updatedTask);
        }
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
                String senderPhoneNumber = (String) sender.get("phone");
                String senderEmail = (String) sender.get("email");

                if (isNullOrEmpty(senderName)) {
                    missingAttributes.add("SenderName");
                }

                if (messageTask.getchannelList() != null) {
                    for (String channel : messageTask.getchannelList()) {
                        if ("sms".equalsIgnoreCase(channel)) {
                            if (isNullOrEmpty(senderPhoneNumber)) {
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
                String receiverPhoneNumber = (String) receiver.get("phone");
                String receiverEmail = (String) receiver.get("email");

                if (isNullOrEmpty(receiverName)) {
                    missingAttributes.add("ReceiverName");
                }

                if (messageTask.getchannelList() != null) {
                    for (String channel : messageTask.getchannelList()) {
                        if ("sms".equalsIgnoreCase(channel)) {
                            if (isNullOrEmpty(receiverPhoneNumber)) {
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

}
