package com.projectgp.messenger.service.SendingPlatform;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.projectgp.messenger.model.MessageTask;


@Component
public class SMSPlateform {

    @SuppressWarnings("unchecked")
    @RabbitListener(queues = "smsQueue")
    public void sendsmsMessage(MessageTask messageTask){
        // System.out.println("正在通过短信发送消息任务：" + messageTask.getTaskId());

        Map<String, Object> sender = (Map<String, Object>)messageTask.getSenderRecipient().get("sender");
        String senderName = (String) sender.get("name");
        System.out.println("消息发送者：" + senderName);
        Integer senderPhoneNumber = (Integer) sender.get("phone");
        System.out.println("消息发送者："+ senderPhoneNumber);
        //提取接收者信息
        Map<String, Object> receiver = (Map<String, Object>)messageTask.getReceiverInformation().get("receiver");
        String receiverName =  (String) receiver.get("name");
        System.out.println("消息接收者："+ receiverName);
        Integer receiverPhoneNumber =  (Integer) receiver.get("phone");
        System.out.println("消息接收者：" + receiverPhoneNumber);
        //提取消息内容
        String content = messageTask.getContent();
        System.out.println("消息内容："+ content);
        // 发送短信

        //将消息发送时间保存到数据库
        //task.setActualSendTime(LocalDateTime.now());
    }
    
}
