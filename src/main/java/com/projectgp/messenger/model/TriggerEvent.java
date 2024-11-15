package com.projectgp.messenger.model;

import org.springframework.stereotype.Repository;

//任务类实体
@Repository
public class TriggerEvent {
    private String sender;
    private String receiver;
    private String method;
    private String content;
    private String template;

    public  void setSender(String sender) {
        this.sender = sender;
    }
    public String getSender() {
        return sender ;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    public String getReceiver() {
        return receiver ;
    }

    public void setMethod(String method) {
         this.method = method;
    }
    public String getMethod() {
        return method ;
     }

    public void setContent(String content){
         this.content  = content;
    }
    public String getContent(){
        return content ;
      }
    
    public void setTemplate(String template){
        this.template = template;
        
    }
    public String getTemplate() {
        return template ;
      }

}
