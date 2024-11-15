package com.projectgp.messenger.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projectgp.messenger.model.MessageTemplate;
import com.projectgp.messenger.service.MessageTemplateService;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/message-template")
public class MessageTemplateController {


    @Autowired
    private MessageTemplateService messageTemplateservice;

    @PostMapping("/create")
    public String createmessagetemplate(@ModelAttribute MessageTemplate messageTemplate){

        messageTemplateservice.createMessageTemplate(messageTemplate);
        return "successfully created a message template  id: " + messageTemplate.getId() + "\n";
    }

    @DeleteMapping("/delete")
    public String deletemessagetemplate(long id){

        messageTemplateservice.deleteMessageTemplate(id);
        return "successfully deleted message template id: " + id;
    }

    //要修改成可以选着参数修改的，可以基于任何信息
    //要给出id
    @PutMapping("/update")
    public String updatemessagetemplate(@ModelAttribute MessageTemplate messageTemplate){

        messageTemplateservice.updateMessageTemplate(messageTemplate);
        return "successfully updated a message template id:" + messageTemplate.getId()+ "\n";
    }

    @GetMapping("/find")
    public MessageTemplate getmessagetemplate(long id){

        return messageTemplateservice.getMessageTemplateById(id);
    }

    @GetMapping("/findall")
    public ArrayList<MessageTemplate> getallmessagetemplates(){

        return messageTemplateservice.getAllMessageTemplate();
    }
    

    
}
