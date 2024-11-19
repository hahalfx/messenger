package com.projectgp.messenger.service.impl;

import java.util.Map;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projectgp.messenger.model.MessageTask;
import com.projectgp.messenger.model.MessageTemplate;
import com.projectgp.messenger.service.MessageSendService;
import com.projectgp.messenger.service.MessageTemplateService;
import java.time.LocalDateTime;

@Service
public class MessageSendServiceImpl implements MessageSendService {

    @Autowired
    private MessageTemplateService messageTemplateService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 根据任务id渲染任务的模版内容
    public void renderTaskContent(MessageTask messageTask) {
        Map<String, Object> variables = messageTask.getAttribute();
        long templateId = messageTask.getTemplateId();
        MessageTemplate template = messageTemplateService.getMessageTemplateById(templateId);
        String renderedContent = template.getContent();
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            renderedContent = renderedContent.replace("{" + entry.getKey() + "}", entry.getValue().toString());
        }
        messageTask.setContent(renderedContent);
    }

    // 向RabbitMQ发送消息
    @Override
    public MessageTask sendMessage(MessageTask messageTask) {
        System.out.println("分配消息到队列中");
        if (messageTask.getTemplateId() != null) {
            // 渲染消息内容
            // attribute参数是一个map，key是占位符，value是参数
            renderTaskContent(messageTask);
        }
        // 发送消息到交换机
        String exchangeName = "MessengerExchange";
        // 将消息任务实体转序列化作为信息内容发送
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        // 根据channelList中的队列名，发送到多个队列
        for (String channel : messageTask.getchannelList()) {
            String routingKey = "send." + channel;
            System.out.println(routingKey + "!");
            // 例如 "send.feishu"
            // 将消息发送到MQ的交换器
            rabbitTemplate.convertAndSend(exchangeName, routingKey, messageTask);
        }
        //设置消息发送时间
        messageTask.setSendTime(LocalDateTime.now());
        //设置消息状态为发送中
        messageTask.setStatus("SENDING");
        //返回一整个Task对象
        return messageTask;

    }

}
