package com.projectgp.messenger.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("trigger_events") // 指定对应的数据库表名
public class TriggerEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 事件ID，主键，自动生成
     */
    @TableId(value = "event_id", type = IdType.AUTO)
    private Long eventId;

    /**
     * 事件类型
     */
    private String eventType;

    /**
     * 事件数据，JSON格式
     */
    @TableField(value = "event_data")
    private String eventData;

    /**
     * 触发时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime triggerTime;

    /**
     * 关联的任务ID
     */
    private Long taskId;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
