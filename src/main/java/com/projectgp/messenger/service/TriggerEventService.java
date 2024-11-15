package com.projectgp.messenger.service;

import java.util.ArrayList;

import com.baomidou.mybatisplus.extension.service.IService;
import com.projectgp.messenger.model.TriggerEvent;

public interface TriggerEventService extends IService<TriggerEvent>{
    // 创建消息模版
    public void createTriggerEvent(TriggerEvent triggerEvent);
    // 删除消息模版
    public void deleteTriggerEvent(long Id);
    // 修改消息模版
    public void updateTriggerEvent(TriggerEvent triggerEvent);
    // 查询消息模版
    public TriggerEvent getTriggerEventById(long Id);

    public ArrayList<TriggerEvent> getAllTriggerEvent();
    
}
