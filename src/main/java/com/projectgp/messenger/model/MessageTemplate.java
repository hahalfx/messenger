package com.projectgp.messenger.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.Data;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@TableName("message_template")
public class MessageTemplate {

    private static final Logger logger = LoggerFactory.getLogger(MessageTemplate.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // 主键ID，自动生成
    @TableId(type = IdType.AUTO)
    private Long id;

    // 模板名称
    @TableField("name")
    private String name;

    // 消息类型（如 SMS、EMAIL、PUSH）
    @TableField("type")
    private String type;

    // 消息主题（邮件时使用）
    @TableField("subject")
    private String subject;

    // 模板内容，可能包含占位符
    @TableField("content")
    private String content;

    // 占位符列表，JSON 存储
    @TableField("placeholders")
    private String placeholdersJson;

    // 创建时间和更新时间，自动填充
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    // Getters 和 Setters 方法

    // 方便操作占位符的 getter 和 setter
    @TableField(exist = false)
    private List<String> placeholders;

    // 将 placeholders 序列化为 JSON 字符串
    public void setPlaceholders(List<String> placeholders) {
        this.placeholders = placeholders;
        this.placeholdersJson = serializePlaceholders(placeholders);
    }

    // 从 JSON 字符串反序列化为 List
    public List<String> getPlaceholders() {
        if (this.placeholders == null && this.placeholdersJson != null) {
            this.placeholders = deserializePlaceholders(this.placeholdersJson);
        }
        return this.placeholders;
    }

    // 序列化方法
    private String serializePlaceholders(List<String> placeholders) {
        try {
            return objectMapper.writeValueAsString(placeholders);
        } catch (Exception e) {
            logger.error("序列化占位符列表时发生错误", e);
            return null;
        }
    }

    // 反序列化方法
    private List<String> deserializePlaceholders(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            logger.error("反序列化占位符列表时发生错误", e);
            return new ArrayList<>();
        }
    }

    // 渲染模板方法
    public String render(Map<String, Object> variables) {
        String renderedContent = content;
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            renderedContent = renderedContent.replace("{" + entry.getKey() + "}", entry.getValue().toString());
        }
        return renderedContent;
    }
}