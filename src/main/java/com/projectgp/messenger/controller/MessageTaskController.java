package com.projectgp.messenger.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projectgp.messenger.model.MessageTask;
import com.projectgp.messenger.service.MessageTaskService;

@RestController
@RequestMapping("/messenger-task")
public class MessageTaskController {

    @Autowired
    public MessageTaskService messageTasksService;

    // 初始化一个消息任务
    @PostMapping("/initial")
    public MessageTask initialMessageTask(@ModelAttribute MessageTask messageTask) {

        messageTasksService.requestCheck(messageTask);

        return messageTask;
    }

    // 消息任务的创建
    @PostMapping("/create")
    public String createTask(@ModelAttribute MessageTask messageTask) {
        String result = messageTasksService.requestCheck(messageTask);
        //检查请求
        if(result == "NoMissingAttribute")
        {
            MessageTask newmessageTask;
            messageTasksService.createMessageTask(messageTask);
            //判断是否立即发送
            newmessageTask = messageTasksService.getMessageTaskById(messageTask.getTaskId());
            messageTasksService.AddTasktoscheduler(newmessageTask);
            return "New Task has been successfully Created with id:"+ messageTask.getTaskId();
        }
        else
        {
            return result;
        }
    }

    @DeleteMapping("/delete")
    public String deleteTask(long taskId) {

        messageTasksService.deleteMessageTask(taskId);
        return "Task has been successfully deleted with id:" + taskId;
    }

    @DeleteMapping("/batchdelete")
    public String deleteTasks(@RequestParam("taskIds") long[] taskIds) {

        messageTasksService.deleteMessageTasks(taskIds);
        return "Task has been successfully deleted with id:" + taskIds;
    }

    @PutMapping("/update")
    public String updateTask(@ModelAttribute MessageTask messageTask) {

        messageTasksService.updateMessageTask(messageTask);
        return "New Task has been successfully updated with id:" + messageTask.getTaskId();
    }

    @GetMapping("/find")
    public MessageTask findTask(long taskId) {
        System.out.println("Finding Task by Id: " + taskId);
        return messageTasksService.getMessageTaskById(taskId);
    }

}
