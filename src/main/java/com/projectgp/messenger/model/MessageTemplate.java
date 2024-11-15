package com.projectgp.messenger.model;

import com.baomidou.mybatisplus.annotation.*;
import com.projectgp.messenger.config.JSONConverter;
import lombok.Data;


import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("message_template")
public class MessageTemplate {

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
        this.placeholdersJson = JSONConverter.serializeListtoJsonString(placeholders);
    }

    // 从 JSON 字符串反序列化为 List
    public List<String> getPlaceholders() {
        if (this.placeholders == null && this.placeholdersJson != null) {
            this.placeholders = JSONConverter.deserializeJsonStringtoList(this.placeholdersJson);
        }
        return this.placeholders;
    }

}