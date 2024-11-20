package com.projectgp.messenger.service.SendingPlatform;

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
        String senderEmail = (String) sender.get("email");
        System.out.println("消息发送者："+ senderEmail);
        //提取接收者信息
        Map<String, Object> receiver = (Map<String, Object>)messageTask.getReceiverInformation().get("receiver");
        String receiverName =  (String) receiver.get("name");
        System.out.println("消息接收者："+ receiverName);
        String receiverEmail =  (String) receiver.get("email");
        System.out.println("消息接收者：" + receiverEmail);
        //提取消息内容
        String content = messageTask.getContent();
        System.out.println("消息内容："+ content);
    }
}
