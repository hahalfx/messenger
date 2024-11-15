package com.projectgp.messenger.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.projectgp.messenger.model.MessageTemplate;


public interface MessageTemplateService extends IService<MessageTemplate> {
    // 业务逻辑方法，如创建、更新、删除模板
    // 创建消息模版
    public void createMessageTemplate(MessageTemplate messageTemplate);
    // 删除消息模版
    public void deleteMessageTemplate(long Id);
    // 修改消息模版
    public void updateMessageTemplate(MessageTemplate messageTemplate);
    // 查询消息模版
    public MessageTemplate getMessageTemplateById(long Id);
    
}
