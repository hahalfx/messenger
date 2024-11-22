package com.projectgp.messenger.service.impl;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projectgp.messenger.model.MessageTask;
import com.projectgp.messenger.model.MessageTemplate;
import com.projectgp.messenger.service.MessageSendService;
import com.projectgp.messenger.service.MessageTemplateService;

@Service
public class MessageSendServiceImpl implements MessageSendService {

    @Autowired
    private MessageTemplateService messageTemplateService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final Logger logger = LoggerFactory.getLogger(MessageSendService.class);

    // 校验消息模板
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{([^}]+)\\}");

    /**
     * 从模板内容中提取所有的占位符键
     * 
     * @param templateContent 模板内容
     * @return 包含所有占位符键的 Set
     */
    public static Set<String> extractPlaceholders(String templateContent) {
        Set<String> placeholders = new HashSet<>();
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(templateContent);
        while (matcher.find()) {
            placeholders.add(matcher.group(1));
        }
        return placeholders;
    }

    /**
     * 验证模板中的占位符是否与变量映射匹配
     * 
     * @param templateContent 模板内容
     * @param variables       变量映射
     * @throws IllegalArgumentException 如果存在缺失变量或多余变量
     */
    public static void validateTemplateParameters(String templateContent, Map<String, Object> variables) {
        Set<String> placeholders = extractPlaceholders(templateContent);
        Set<String> variableKeys = variables != null ? variables.keySet() : new HashSet<>();

        // 找出缺失的变量
        Set<String> missingVariables = new HashSet<>(placeholders);
        missingVariables.removeAll(variableKeys);

        // 找出多余的变量
        Set<String> extraVariables = new HashSet<>(variableKeys);
        extraVariables.removeAll(placeholders);

        StringBuilder errorMessage = new StringBuilder();

        if (!missingVariables.isEmpty()) {
            errorMessage.append("缺失的变量: ").append(missingVariables).append(". ");
        }

        if (!extraVariables.isEmpty()) {
            errorMessage.append("多余的变量: ").append(extraVariables).append(". ");
        }

        if (errorMessage.length() > 0) {
            throw new IllegalArgumentException("模板参数不匹配: " + errorMessage.toString());
        }
    }

    // 根据任务id渲染任务的模版内容
    public void renderTaskContent(MessageTask messageTask) {
        Map<String, Object> variables = messageTask.getAttribute();
        long templateId = messageTask.getTemplateId();
        MessageTemplate template = messageTemplateService.getMessageTemplateById(templateId);
        String templateContent = template.getContent();

        // 验证模板参数
        validateTemplateParameters(templateContent, variables);

        // 渲染内容
        String renderedContent = templateContent;
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            renderedContent = renderedContent.replace("{" + entry.getKey() + "}", entry.getValue().toString());
        }
        messageTask.setContent(renderedContent);
    }

    // 向RabbitMQ发送消息
    @Override
    public MessageTask sendMessage(MessageTask messageTask) {
        try {
            if (messageTask.getTemplateId() != null) {
                // 渲染消息内容
                // attribute参数是一个map，key是占位符，value是参数
                try {
                    renderTaskContent(messageTask);
                } catch (Exception e) {
                    logger.error("实例化消息模版内容失败，任务ID: {}", messageTask.getTaskId(), "模版ID:{}", messageTask.getTemplateId(),
                            e);
                    throw new RuntimeException("消息模版实例化失败", e);
                }
            }
            // 发送消息到交换机
            String exchangeName = "MessengerExchange";
            try {
                // 将消息任务实体转序列化作为信息内容发送
                rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
            } catch (Exception e) {
                logger.error("消息任务实体转换信息失败", e);
                throw new RuntimeException("消息任务实体转换信息失败", e);
            }
            try {
                // 根据channelList中的队列名，发送到多个队列
                for (String channel : messageTask.getchannelList()) {
                    String routingKey = "send." + channel;
                    System.out.println("发送消息任务到" + routingKey);
                    // 例如 "send.feishu"
                    // 将消息发送到MQ的交换器
                    rabbitTemplate.convertAndSend(exchangeName, routingKey, messageTask);
                }
            } catch (Exception e) {
                logger.error("发送消息到消息队列", e);
                throw new RuntimeException("发送消息到消息队列", e);
            }
            // 更新消息任务参数
            messageTask.setStatus("SENDED");
            messageTask.setActualSendTime(LocalDateTime.now());
            // 返回一整个Task对象
            return messageTask;
        } catch (Exception e) {
            logger.error("发送消息时发生异常，任务ID: {}", messageTask.getTaskId(), e);
            throw new RuntimeException("消息发送失败", e); // 将异常抛出以便上层捕捉
        }

    }

}
