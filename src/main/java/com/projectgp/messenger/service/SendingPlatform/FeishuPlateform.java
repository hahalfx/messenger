package com.projectgp.messenger.service.SendingPlatform;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.projectgp.messenger.model.MessageTask;
import com.projectgp.messenger.service.MessageTaskService;
import java.time.LocalDateTime;

@Component
public class FeishuPlateform {

    @Autowired
    private MessageTaskService messageTaskService;

    @RabbitListener(queues = "feishuQueue")
    public void sendFeishuMessage(Long Id){
        System.out.println("正在通过飞书发送消息任务：" + Id);
        MessageTask task = messageTaskService.getById(Id);
        //将飞书消息发送时间保存到数据库
        task.setActualSendTime(LocalDateTime.now());
        task.setStatus("SENT");
        task.setAlive("DOWN");
        messageTaskService.updateById(task);
    }
    
}
