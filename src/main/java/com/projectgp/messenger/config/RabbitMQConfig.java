package com.projectgp.messenger.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {


    // 交换器名称
    public static final String EXCHANGE_NAME = "MessengerExchange";

    // 1. 声明队列
    //飞书消息队列
    @Bean
    public Queue feishuQueue() {
        return new Queue("feishuQueue", true); // true 表示持久化
    }
    //邮箱消息队列
    @Bean
    public Queue emailQueue() {
        return new Queue("emailQueue", true);  // true 表示持久化
    }
    //短信消息队列
    @Bean
    public Queue smsQueue() {
        return new Queue("smsQueue", true);   // true 表示持久化
    }
    //平台内网消息队列
    @Bean
    public Queue netQueue()  {
        return new Queue("netQueue", true);   // true 表示持久化
    }
    //其他消息队列
    @Bean
    public Queue otherQueue()   {
        return new Queue("otherQueue", true);  // true 表示持久化
    }

    // 2. 声明交换器
    @Bean
    public DirectExchange MessengerExchange() {
        return new DirectExchange("MessengerExchange", true, false);
    }

    // 3. 绑定队列到交换器
    @Bean
    public Binding bindingFeishu(Queue feishuQueue, DirectExchange MessengerExchange) {
        return BindingBuilder.bind(feishuQueue).to(MessengerExchange).with("send.feishu");
    }

    @Bean
    public Binding bindingEmail(Queue emailQueue, DirectExchange MessengerExchange) {
        return BindingBuilder.bind(emailQueue).to(MessengerExchange).with("send.email");
    }

    @Bean
    public Binding bindingSms(Queue smsQueue, DirectExchange MessengerExchange) {
        return BindingBuilder.bind(smsQueue).to(MessengerExchange).with("send.sms");
    }

    @Bean
    public Binding bindingNet(Queue netQueue, DirectExchange MessengerExchange)  {
        return BindingBuilder.bind(netQueue).to(MessengerExchange).with("send.net");
    }

    @Bean
    public Binding bindingOther(Queue otherQueue, DirectExchange MessengerExchange){
        return BindingBuilder.bind(otherQueue).to(MessengerExchange).with("send.other");
    }



}
