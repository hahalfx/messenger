package com.projectgp.messenger.service.SendingPlatform;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.projectgp.messenger.model.MessageTask;

@Component
public class NetPlatform {

    @RabbitListener(queues = "netQueue")
    public void sendnetMessage(MessageTask messageTask){
        System.out.println("正在通过内部平台发送消息任务：" + messageTask.getTaskId());
        System.out.println("消息发送者："+ messageTask.getSenderRecipientJson());
        System.out.println("消息接收者："+ messageTask.getReceiverInformation());
        System.out.println("消息内容："+ messageTask.getContent());
        //将消息发送时间保存到数据库
        //task.setActualSendTime(LocalDateTime.now());
    }
}
