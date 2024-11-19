package com.projectgp.messenger.model;

import com.baomidou.mybatisplus.annotation.*;
import com.projectgp.messenger.config.JSONConverter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    //通过一个消息任务相同信息多渠道发送
    @TableField(exist = false)
    private List<String> channelList;

    //Map格式的发送者信息
    @TableField(exist = false)
    private Map<String, Object> senderRecipient;

    /**
     * 接收者查询条件
     */
    private String receiverQuery;

    //Map格式的接受者信息
    @TableField(exist = false)
    private Map<String, Object> receiverInformation;

    /**
     * 模板ID，外键
     */
    private Integer templateId;

    /**
     * 时间类型（如SCHEDULED、IMMEDIATE、RECURRING等）
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
     * 状态（如WAITING、SENDING、SENT、FAILED等）
     */
    private String status = "WAITING";

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

    //消息内容
    private String content;

    //数据库中Json字符串形式的模版消息的参数
    @TableField("attribute")
    private String attributeJson;

    //Map格式的消息模版参数
    @TableField(exist = false)
    private Map<String, Object> attribute;

    //数据库Json字符串形式的发送者和接收者信息
    @TableField("sender_recipient")
    private String senderRecipientJson;

    @TableField("receiver_information")
    private String receiverInformationJson;

    // 将 senderRecipient 序列化为 JSON 字符串
    public void setSenderRecipient(Map<String, Object> senderRecipient) {
        this.senderRecipient = senderRecipient;
        this.senderRecipientJson = JSONConverter.serializeMaptoJsonString(senderRecipient);
    }

    // 从 senderRecipientJson字符串反序列化为 Map
    public Map<String, Object> getSenderRecipient() {
        if (this.senderRecipient == null && this.senderRecipientJson != null) {
            this.senderRecipient = JSONConverter.deserializeJsonStringtoMap(this.senderRecipientJson);
        }
        return this.senderRecipient;
    }
    // 将 receiverInformation 序列化为 JSON 字符串
    public void setReceiverInformation(Map<String, Object> receiverInformation) {
        this.receiverInformation = receiverInformation;
        this.receiverInformationJson = JSONConverter.serializeMaptoJsonString(receiverInformation);
    }

    // receiverInformationJson字符串反序列化为 Map
    public Map<String, Object> getReceiverInformation() {
        if (this.receiverInformation == null && this.receiverInformationJson != null) {
            this.receiverInformation = JSONConverter.deserializeJsonStringtoMap(this.receiverInformationJson);
        }
        return this.receiverInformation;
    }

    //attributeJson字符串反序列化为 Map
    public Map<String, Object> getAttribute() {
		if (this.attribute == null && this.attributeJson != null) {
			this.attribute = JSONConverter.deserializeJsonStringtoMap(this.attributeJson);
        }
    	return this.attribute;
	}

    //将 attribute Map序列化为 JSON字符串
    public void setAttribute(Map<String, Object> attribute) {
        this.attribute = attribute;
        this.attributeJson  = JSONConverter.serializeMaptoJsonString(attribute);
    }

    //将deliverchannel字符串反序列化为List<String>
    public List<String> getchannelList() {
        if (this.channelList  == null && this.deliveryChannel != null) {
            channelList = JSONConverter.deserializeJsonStringtoList(this.deliveryChannel);
         }
    	return this.channelList;
    }

    //将channellist List<String>序列化为字符串
    public void setchannelList(List<String> channelList)  {
        this.channelList = channelList;
        this.deliveryChannel = JSONConverter.serializeListtoJsonString(channelList);
    }


}