package com.projectgp.messenger.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.projectgp.messenger.model.MessageTask;

public interface MessageTaskMapper extends BaseMapper<MessageTask> {

    // 自定义查询方法，如根据名称查询模板
    //MessageTemplate selectByName(String name);

    // 其他自定义方法
}