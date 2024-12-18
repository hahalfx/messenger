## 一、整体架构设计

	1.	分层结构：
	•	Controller 层：处理 HTTP 请求，调用 Service 层的方法。
	•	Service 层：封装业务逻辑，处理消息模板和触发事件的创建、管理和消息发送。
	•	Repository 层：与数据库交互，进行 CRUD 操作。
	•	Model 层：定义实体类和数据传输对象（DTO）。
	•	Config 层：配置阿里云函数计算等相关配置。
	2.	模块划分：
	•	消息模板模块（MessageTemplate）
	•	触发事件模块（TriggerEvent）
	•	消息发送模块（MessageSending）

二、代码结构详解

1. 包结构

```
.
├── Dockerfile
├── HELP.md
├── MD
│   └── 在一个完整的消息通告（Messaging Notification）Spring Boot微服务架构中，集成阿里云函数计算（Function Compute, FC）同时使用数据库和RabbitMQ，可以显著提升系统的可扩展性、弹性和响应速.md
├── README.md
├── mvnw
├── mvnw.cmd
├── pom.xml
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── projectgp
│   │   │           └── messenger
│   │   │               ├── MessengerApplication.java
│   │   │               ├── config
│   │   │               │   ├── JSONConverter.java
│   │   │               │   ├── MyMetaObjectHandler.java
│   │   │               │   ├── RabbitMQConfig.java
│   │   │               │   └── StartupTaskLoader.java
│   │   │               ├── controller
│   │   │               │   ├── MessageTaskController.java
│   │   │               │   ├── MessageTemplateController.java
│   │   │               │   └── TriggerEventController.java
│   │   │               ├── mapper
│   │   │               │   ├── MessageTaskMapper.java
│   │   │               │   ├── MessageTemplateMapper.java
│   │   │               │   └── TriggerEventMapper.java
│   │   │               ├── model
│   │   │               │   ├── MessageJob.java
│   │   │               │   ├── MessageTask.java
│   │   │               │   ├── MessageTemplate.java
│   │   │               │   └── TriggerEvent.java
│   │   │               └── service
│   │   │                   ├── MessageSchedulerService.java
│   │   │                   ├── MessageSendService.java
│   │   │                   ├── MessageTaskService.java
│   │   │                   ├── MessageTemplateService.java
│   │   │                   ├── MessageTriggerListener.java
│   │   │                   ├── TriggerEventService.java
│   │   │                   └── impl
│   │   │                       ├── MessageSendServiceImpl.java
│   │   │                       ├── MessageTaskServiceImpl.java
│   │   │                       ├── MessageTemplateServiceImpl.java
│   │   │                       └── TriggerEventServiceImpl.java
│   │   └── resources
│   │       ├── SQL.txt
│   │       ├── application.properties
│   │       ├── application.yaml
│   │       ├── static
│   │       └── templates
│   └── test
│       └── java
│           └── com
│               └── projectgp
│                   └── messenger
│                       └── MessengerApplicationTests.java
└── target
    ├── classes
    │   ├── SQL.txt
    │   ├── application.properties
    │   ├── application.yaml
    │   └── com
    │       └── projectgp
    │           └── messenger
    │               ├── MessengerApplication.class
    │               ├── config
    │               │   ├── JSONConverter$1.class
    │               │   ├── JSONConverter$2.class
    │               │   ├── JSONConverter.class
    │               │   ├── MyMetaObjectHandler.class
    │               │   ├── RabbitMQConfig.class
    │               │   └── StartupTaskLoader.class
    │               ├── controller
    │               │   ├── MessageTaskController.class
    │               │   ├── MessageTemplateController.class
    │               │   └── TriggerEventController.class
    │               ├── mapper
    │               │   ├── MessageTaskMapper.class
    │               │   ├── MessageTemplateMapper.class
    │               │   └── TriggerEventMapper.class
    │               ├── model
    │               │   ├── MessageJob.class
    │               │   ├── MessageTask.class
    │               │   ├── MessageTemplate.class
    │               │   └── TriggerEvent.class
    │               └── service
    │                   ├── MessageSchedulerService.class
    │                   ├── MessageSendService.class
    │                   ├── MessageTaskService.class
    │                   ├── MessageTemplateService.class
    │                   ├── MessageTriggerListener.class
    │                   ├── TriggerEventService.class
    │                   └── impl
    │                       ├── MessageSendServiceImpl.class
    │                       ├── MessageTaskServiceImpl.class
    │                       ├── MessageTemplateServiceImpl.class
    │                       └── TriggerEventServiceImpl.class
    ├── generated-sources
    │   └── annotations
    ├── generated-test-sources
    │   └── test-annotations
    ├── maven-archiver
    │   └── pom.properties
    ├── maven-status
    │   └── maven-compiler-plugin
    │       ├── compile
    │       │   └── default-compile
    │       │       ├── createdFiles.lst
    │       │       └── inputFiles.lst
    │       └── testCompile
    │           └── default-testCompile
    │               ├── createdFiles.lst
    │               └── inputFiles.lst
    ├── messenger-0.0.1-SNAPSHOT.jar
    ├── messenger-0.0.1-SNAPSHOT.jar.original
    └── test-classes
        └── com
            └── projectgp
                └── messenger
                    └── MessengerApplicationTests.class

48 directories, 74 files
```



2. Model 层

	•	MessageTemplate：消息模板实体，包含模板内容、占位符等信息。
	•	TriggerEvent：触发事件实体，定义何时触发哪些消息。
	•	UserPreference：用户偏好设置，存储用户的消息接收偏好。

3. Repository 层

	•	MessageTemplateRepository：操作消息模板的 CRUD。
	•	TriggerEventRepository：操作触发事件的 CRUD。
	•	UserPreferencesRepository：存取用户偏好数据。

4. Service 层

	•	MessageTemplateService：
	•	创建、更新、删除消息模板。
	•	校验模板的正确性。
	•	TriggerEventService：
	•	创建、更新、删除触发事件。
	•	定义事件与模板的关联关系。
	•	MessageSendService：
	•	根据触发事件和用户偏好，生成最终消息。
	•	调用消息发送渠道（如短信、邮件、推送）。

5. Controller 层

	•	MessageTemplateController：提供 RESTful 接口供前端或其他服务调用，进行消息模板的管理。
	•	TriggerEventController：提供触发事件的管理接口。
	•	MessageSendController：处理消息发送请求。

6. Utils 工具类

	•	MessageFormatter：用于根据模板和数据生成最终消息内容，处理占位符替换等。

7. Config 配置

	•	FunctionComputeConfig：配置阿里云函数计算的相关参数，如入口函数、环境变量等。

三、功能流程

1. 消息模板的创建与管理

	•	创建模板：用户通过 MessageTemplateController 提交模板内容。
	•	模板校验：MessageTemplateService 校验模板格式，保存到数据库。
	•	模板查询与更新：支持模板的查询、修改和删除。

2. 触发事件的创建与管理

	•	创建事件：用户定义新的触发事件，通过 TriggerEventController 提交。
	•	事件关联：TriggerEventService 处理事件与消息模板的关联。
	•	事件管理：支持事件的查询、修改和删除。

3. 基于消息模板和用户自定义的消息发送

	•	触发消息发送：当特定事件发生时，MessageSendService 被调用。
	•	消息生成：MessageFormatter 根据模板和用户数据生成消息内容。
	•	用户偏好：UserPreferencesRepository 获取用户的接收偏好（如渠道、时间）。
	•	发送消息：调用相应的渠道 API（如短信网关、邮件服务器）发送消息。

四、迁移至 Serverless 架构

1. 阿里云函数计算（FC）部署

	•	入口函数：编写一个 Handler，作为函数计算的入口。
	•	无状态设计：确保服务的无状态性，以适应函数计算的特性。
	•	依赖管理：使用 Maven 或 Gradle，将依赖打包在一起。

2. 配置与优化

	•	环境变量：将数据库连接、API 密钥等配置为环境变量。
	•	启动性能优化：减少函数冷启动时间，如使用轻量级组件。
	•	日志与监控：利用阿里云的日志服务和监控工具，跟踪函数的执行。

3. 部署流程

	•	打包应用：将应用打包成可部署的压缩包或镜像。
	•	上传到 FC：通过阿里云控制台、CLI 或 CI/CD 工具部署到函数计算。
	•	测试与验证：部署后进行功能测试，确保服务正常运行。

五、注意事项

	•	安全性：确保敏感信息的安全传输和存储，使用 HTTPS 和加密存储。
	•	可扩展性：设计良好的接口和模块，以便于功能扩展和维护。
	•	性能优化：关注函数的执行时间和资源消耗，优化代码和依赖。

六、总结

通过清晰的分层和模块化设计，可以实现消息通知微服务的核心功能。将服务迁移至阿里云函数计算后，需要关注无服务器架构的特性，进行相应的代码和配置调整。上述代码结构提供了一个基础的框架，具体实现中可以根据实际需求进行细化和扩展。



## 数据库表

### 消息模版表

| 属性名      | 类型     | 是否为主键 | 是否必须 | 是否唯一 | 详情                       |
| ----------- | -------- | ---------- | -------- | -------- | -------------------------- |
| id          | Bigint   | 是         | 是       | 是       |                            |
| name        | varchar  |            |          |          |                            |
| type        | varchar  | 否         | 是       | 否       | 描述消息类型               |
| subject     | varchar  | 否         | 是       | 否       | 描述消息的主题或标题       |
| content     | text     | 否         | 是       | 否       | 存储消息的具体内容         |
| placeholder | Json     |            |          |          | 消息内容中的占位符         |
| create_at   | datetime | 否         | 是       | 否       | 创建时间                   |
| create_user | varchar  | 否         | 是       | 否       | 创建用户ID                 |
| update_at   | datetime | 否         | 是       | 否       | 修改时间                   |
| update_user | varchar  | 否         | 是       | 否       | 修改用户                   |
| status      | varchar  | 否         | 是       | 否       | 有草稿、活动、停用三种状态 |

### 消息任务表

| 属性名                | 类型        | 是否为主键 | 是否必须 | 是否唯一 | 详情                                              |
| --------------------- | ----------- | ---------- | -------- | -------- | ------------------------------------------------- |
| imessaged             | varchar     | 是         | 是       | 是       | 任务ID                                            |
| task_name             | varchar     | 否         | 否       | 否       | 任务名                                            |
| delivery_channel      | varchar     | 否         | 否       | 否       | 消息应该通过的发送渠道                            |
| sender_recipient      | json        | 否         | 否       | 否       | 用户的手机  邮箱  站内id                          |
| receier_query         | varchar     | 否         | 是       | 否       | 查询接受者的条件  接口名                          |
| recseiver_information | json        | 否         | 否       | 否       | 接收者信息，有可能是手机、邮箱等                  |
| template_id           | INT         | 否         | 否       | 否       | 关联的消息模板ID，外键。                          |
| time_type             | String      | 否         | 是       | 否       | IMMEDIATE, SCHEDULED                              |
| send_time             | DATETIME    | 否         | 是       | 否       | 计划发送时间。                                    |
| actual_send_time      | DATETIME    | 否         | 否       | 否       | 实际发送时间。                                    |
| status                | VARCHAR(50) | 否         | 否       | 否       | 消息的发送状态，如“pending”, “sent”, “failed”等。 |
| created_at            | DATETIME    | 否         | 是       | 否       | 记录任务创建的时间。                              |
| updated_at            | DATETIME    | 否         | 否       | 否       | 记录任务最后一次更新的时间。                      |
| ALIVE                 |             | 否         | 是       | 否       | 消息任务的存活状态                                |

 ### 触发事件表

| 字段名       | 数据类型    | 是否为主键 | 是否必须 | 是否唯一 | 描述                                            |
| ------------ | ----------- | ---------- | -------- | -------- | ----------------------------------------------- |
| event_id     | INT         | 是         | 是       | 是       | 事件的唯一标识符。                              |
| event_type   | VARCHAR(50) | 否         | 是       | 否       | 事件的类型，如“user_signup”、“order_placed”等。 |
| event_data   | JSON        | 否         | 是       | 否       | 事件的相关数据，存储为JSON格式。                |
| trigger_time | DATETIME    | 否         | 是       | 否       | 事件触发的时间。                                |
| task_id      | INT         | 否         | 否       | 否       | 关联的消息任务ID，外键。                        |
| created_at   | DATETIME    | 否         | 是       | 否       | 记录任务创建的时间。                            |
| updated_at   | DATETIME    | 否         | 否       | 否       | 记录任务最后一次更新的时间。                    |

## RabbitMQ

| 队列名      | 绑定键      |
| ----------- | ----------- |
| feishuQueue | send.feishu |
| emailQueue  | send.email  |
| smsQueue    | send.sms    |
| netQueue    | send.net    |
| otherQueue  | send.other  |

DirectExchange