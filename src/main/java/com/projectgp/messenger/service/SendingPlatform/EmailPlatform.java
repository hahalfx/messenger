package com.projectgp.messenger.service.SendingPlatform;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.projectgp.messenger.model.MessageTask;

@Component
public class EmailPlatform {

    @SuppressWarnings("unchecked")
    @RabbitListener(queues = "emailQueue")
    public void sendemailMessage(MessageTask messageTask){
        System.out.println("正在通过邮件发送消息任务：" + messageTask.getTaskId());
        //提取发送者信息

        Map<String, Object> sender = (Map<String, Object>)messageTask.getSenderRecipient().get("sender");
        String senderName = (String) sender.get("name");
        System.out.println("消息发送者：" + senderName);
        Integer senderEmail = (Integer) sender.get("email");
        System.out.println("消息发送者："+ senderEmail);
        //提取接收者信息
        Map<String, Object> reciever = (Map<String, Object>)messageTask.getReceiverInformation().get("reciever");
        String recieverName =  (String) reciever.get("name");
        System.out.println("消息接收者："+ recieverName);
        Integer recieverEmail =  (Integer) reciever.get("email");
        System.out.println("消息接收者：" + recieverEmail);
        //提取消息内容
        String content = messageTask.getContent();
        System.out.println("消息内容："+ content);
        // 发送短信
        //将消息发送时间保存到数据库
        //task.setActualSendTime(LocalDateTime.now());
    }
}
