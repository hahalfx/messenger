package com.projectgp.messenger.service.SendingPlatform;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.projectgp.messenger.model.MessageTask;

@Component
public class OtherPlatform {

    @RabbitListener(queues = "otherQueue")
    public void sendotherMessage(MessageTask messageTask){
        System.out.println("正在通过其他平台发送消息任务：" + messageTask.getTaskId());
        //将消息发送时间保存到数据库
        //task.setActualSendTime(LocalDateTime.now());
    }
}
