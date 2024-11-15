package com.projectgp.messenger.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projectgp.messenger.model.TriggerEvent;
import com.projectgp.messenger.service.TriggerEventService;

@RestController
@RequestMapping("/triggerEvent")
public class TriggerEventController {

    @Autowired
    private TriggerEventService triggerEventService;

    @PostMapping("/create")
    public String TriggerEventCreate(@ModelAttribute TriggerEvent triggerEvent) {
        triggerEventService.createTriggerEvent(triggerEvent);
        return "TriggerEventCreat id: " + triggerEvent.getEventId();
    }

    @DeleteMapping("/delete")
    public String TriggerEventDeleteById(Long eventId){
         triggerEventService.deleteTriggerEvent(eventId);
         return "TriggerEventDeleteSuccess id:" + eventId;
    }

    @PutMapping("/update")
    public String TriggerEventUpdate(@ModelAttribute TriggerEvent triggerEvent) {
        triggerEventService.updateTriggerEvent(triggerEvent);
         return "TriggerEventUpdate id:  " + triggerEvent.getEventId();
    }

    @GetMapping("/find")
    public TriggerEvent getTriggerEventById(Long eventId){
        return triggerEventService.getTriggerEventById(eventId);
    }
    
}
