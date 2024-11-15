package com.projectgp.messenger.service.impl;

import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projectgp.messenger.model.MessageTask;
import com.projectgp.messenger.model.MessageTemplate;
import com.projectgp.messenger.service.MessageSendService;
import com.projectgp.messenger.service.MessageTaskService;
import com.projectgp.messenger.service.MessageTemplateService;
import java.time.LocalDateTime;

@Service
public class MessageSendServiceImpl implements MessageSendService {

    @Autowired
    private MessageTaskService messageTaskService;

    @Autowired
    private MessageTemplateService messageTemplateService;

    @Autowired
    private RabbitTemplate  rabbitTemplate;

    // 根据任务id渲染任务的模版内容
    public void renderTaskContent(Long taskId, Map<String, Object> variables) {
        MessageTask task = messageTaskService.getMessageTaskById(taskId);
        long templateId = task.getTemplateId();
        MessageTemplate template = messageTemplateService.getMessageTemplateById(templateId);
        String renderedContent = template.getContent();
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            renderedContent = renderedContent.replace("{" + entry.getKey() + "}", entry.getValue().toString());
        }
        task.setContent(renderedContent);
        //将渲染好的内容保存到数据库中
        messageTaskService.updateMessageTask(task);
    }

    // 向RabbitMQ发送消息
    @Override
    public void sendMessage(MessageTask messageTask) {

        if(messageTask.getTemplateId() != null){
            // 渲染消息内容
            //这里现在需要一个模版占位符的参数，需要一个map，key是占位符，value是参数
            System.out.println("渲染消息内容");
            renderTaskContent(messageTask.getTaskId(), messageTask.getAttribute());
        }
        // 发送消息到交换机
        String exchangeName = "MessengerExchange";
        // 发送的消息是要发送的任务Id
        Long message = messageTask.getTaskId();
        // 发送到哪个队列
        String routingKey = "send." + messageTask.getDeliveryChannel();
        System.out.println(routingKey);
        // 例如 "send.feishu"
        // 将消息发送到MQ的交换器
        messageTask.setSendTime(LocalDateTime.now());
        messageTask.setStatus("SENDING");
        messageTaskService.updateById(messageTask);
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);

    }

}
