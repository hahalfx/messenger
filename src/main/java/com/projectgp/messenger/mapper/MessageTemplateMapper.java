package com.projectgp.messenger.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.projectgp.messenger.model.MessageTemplate;

public interface MessageTemplateMapper extends BaseMapper<MessageTemplate> {

    // 自定义查询方法，如根据名称查询模板
    //MessageTemplate selectByName(String name);

    // 其他自定义方法
}