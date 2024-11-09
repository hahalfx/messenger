package com.projectgp.messenger.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projectgp.messenger.model.Task;


@RestController 
@RequestMapping("/messenger")
public class TaskController {


    @Autowired
    public Task task;

    @GetMapping("/newtask")
    public String createTask(@RequestParam String sender, String receiver, String method, String content, String template) {
        task.setSender(sender);
        task.setReceiver(receiver);
        task.setMethod(method);
        task.setContent(content);
        task.setTemplate(template);

        return "{success}";
    }



    
    
    
}
