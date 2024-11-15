package com.projectgp.messenger.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息任务实体类，对应数据库表 message_tasks
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("message_tasks")
public class MessageTask implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务ID，主键
     */
    @TableId(value = "task_id",type = IdType.AUTO)
    private Long taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 发送渠道
     */
    private String deliveryChannel;

    /**
     * 发送者和接收者信息，JSON格式
     */
    @TableField(exist = false)
    private Map<String, Object> senderRecipient;

    /**
     * 接收者查询条件
     */
    private String receiverQuery;

    /**
     * 接收者详细信息，JSON格式
     */
    @TableField(exist = false)
    private Map<String, Object> receiverInformation;

    /**
     * 模板ID，外键
     */
    private Integer templateId;

    /**
     * 时间类型（如定时、立即等）
     */
    private String timeType;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 实际发送时间
     */
    private LocalDateTime actualSendTime;

    /**
     * 状态（如待发送、已发送、失败等）
     */
    private String status;

    /**
     * 创建时间，自动填充
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间，自动填充
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /**
     * 是否有效，默认为 'YES'
     */
    private String alive = "YES";

    // JSON 字符串字段
    @TableField("sender_recipient")
    private String senderRecipientJson;

    @TableField("receiver_information")
    private String receiverInformationJson;

    // ObjectMapper 和 Logger 的实例（确保已正确注入或初始化）
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(MessageTask.class);
    
    // 将 senderRecipient 序列化为 JSON 字符串
    public void setSenderRecipient(Map<String, Object> senderRecipient) {
        this.senderRecipient = senderRecipient;
        this.senderRecipientJson = serializeSenderRecipient(senderRecipient);
    }

    // 从 JSON 字符串反序列化为 Map
    public Map<String, Object> getSenderRecipient() {
        if (this.senderRecipient == null && this.senderRecipientJson != null) {
            this.senderRecipient = deserializeSenderRecipient(this.senderRecipientJson);
        }
        return this.senderRecipient;
    }

    // 序列化方法
    private String serializeSenderRecipient(Map<String, Object> senderRecipient) {
        try {
            return objectMapper.writeValueAsString(senderRecipient);
        } catch (JsonProcessingException e) {
            logger.error("序列化 senderRecipient 时发生错误", e);
            return null;
        }
    }

    // 反序列化方法
    private Map<String, Object> deserializeSenderRecipient(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            logger.error("反序列化 senderRecipient 时发生错误", e);
            return new HashMap<>();
        }
    }

    // 将 receiverInformation 序列化为 JSON 字符串
    public void setReceiverInformation(Map<String, Object> receiverInformation) {
        this.receiverInformation = receiverInformation;
        this.receiverInformationJson = serializeReceiverInformation(receiverInformation);
    }

    // 从 JSON 字符串反序列化为 Map
    public Map<String, Object> getReceiverInformation() {
        if (this.receiverInformation == null && this.receiverInformationJson != null) {
            this.receiverInformation = deserializeReceiverInformation(this.receiverInformationJson);
        }
        return this.receiverInformation;
    }

    // 序列化方法
    private String serializeReceiverInformation(Map<String, Object> receiverInformation) {
        try {
            return objectMapper.writeValueAsString(receiverInformation);
        } catch (JsonProcessingException e) {
            logger.error("序列化 receiverInformation 时发生错误", e);
            return null;
        }
    }

    // 反序列化方法
    private Map<String, Object> deserializeReceiverInformation(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            logger.error("反序列化 receiverInformation 时发生错误", e);
            return new HashMap<>();
        }
    }

}