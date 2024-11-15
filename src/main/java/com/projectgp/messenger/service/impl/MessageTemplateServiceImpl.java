package com.projectgp.messenger.service.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.projectgp.messenger.mapper.MessageTemplateMapper;
import com.projectgp.messenger.model.MessageTemplate;
import com.projectgp.messenger.service.MessageTemplateService;

@Service
public class MessageTemplateServiceImpl extends ServiceImpl<MessageTemplateMapper, MessageTemplate> implements MessageTemplateService {

    @Autowired
    private MessageTemplateMapper messageTemplateMapper;

    // 创建消息模版
    @Override
    public void createMessageTemplate(MessageTemplate messageTemplate) {
          // 保存消息模版
        messageTemplateMapper.insert(messageTemplate);
    }

    // 删除消息模版
    @Override
    public void deleteMessageTemplate(long Id) {
        messageTemplateMapper.deleteById(Id);
    }

     // 修改消息模版
    @Override
    public void updateMessageTemplate(MessageTemplate messageTemplate) {
        messageTemplateMapper.updateById(messageTemplate);
    }

    // 查询消息模版
    @Override
    public MessageTemplate getMessageTemplateById(long Id) {
        return messageTemplateMapper.selectById(Id);
    }
}
