package com.projectgp.messenger.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projectgp.messenger.model.MessageTask;
import com.projectgp.messenger.service.MessageTaskService;


@RestController 
@RequestMapping("/messenger-task")
public class MessageTaskController {


    @Autowired
    public MessageTaskService messageTasksService;

    @PostMapping("/create")
    public String createTask(@ModelAttribute MessageTask messageTask) {

        messageTasksService.createMessageTask(messageTask);
        return "New Task has been successfully Created with id:"+ messageTask.getTaskId();
    }

    @DeleteMapping("/delete")
    public String deleteTask(long taskId) {

        messageTasksService.deleteMessageTask(taskId);
        return "Task has been successfully deleted with id:"+ taskId;
    }

    @PutMapping("/update")
    public String updateTask(@ModelAttribute MessageTask messageTask) {

         messageTasksService.updateMessageTask(messageTask);
         return "New Task has been successfully updated with id:"+ messageTask.getTaskId();
    }

    @GetMapping("/find")
    public MessageTask findTask(long taskId) {
        System.out.println("Finding Task by Id: " + taskId);
         return messageTasksService.getMessageTaskById(taskId);
    }



    
    
    
}
