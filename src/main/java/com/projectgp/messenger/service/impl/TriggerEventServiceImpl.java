package com.projectgp.messenger.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.projectgp.messenger.mapper.TriggerEventMapper;
import com.projectgp.messenger.model.TriggerEvent;
import com.projectgp.messenger.service.TriggerEventService;

@Service
public class TriggerEventServiceImpl extends ServiceImpl<TriggerEventMapper, TriggerEvent>implements TriggerEventService {

    @Autowired
    private TriggerEventMapper triggerEventMapper;

    @Override
    public void createTriggerEvent(TriggerEvent triggerEvent) {
        triggerEventMapper.insert(triggerEvent);
    }

    @Override
    public void deleteTriggerEvent(long Id) {
        triggerEventMapper.deleteById(Id);
    }

    @Override
    public void updateTriggerEvent(TriggerEvent triggerEvent) {
        triggerEventMapper.updateById(triggerEvent);
    }

    @Override
    public TriggerEvent getTriggerEventById(long Id) {
        return triggerEventMapper.selectById(Id);
    }

    @Override
    public ArrayList<TriggerEvent> getAllTriggerEvent() {
        return (ArrayList<TriggerEvent>)triggerEventMapper.selectList(null);
    }
    
}
