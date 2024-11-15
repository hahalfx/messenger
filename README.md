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
com.example.notification
├── controller
│   ├── MessageTemplateController.java
│   ├── TriggerEventController.java
│   └── MessageSendController.java
├── service
│   ├── MessageTemplateService.java
│   ├── TriggerEventService.java
│   └── MessageSendService.java
├── repository
│   ├── MessageTemplateRepository.java
│   ├── TriggerEventRepository.java
│   └── UserPreferencesRepository.java
├── model
│   ├── MessageTemplate.java
│   ├── TriggerEvent.java
│   └── UserPreference.java
├── config
│   └── FunctionComputeConfig.java
└── utils
    └── MessageFormatter.java
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



## RabbitMQ

| 队列名      | 绑定键      |
| ----------- | ----------- |
| feishuQueue | send.feishu |
| emailQueue  | send.email  |
| smsQueue    | send.sms    |
| netQueue    | send.net    |
| otherQueue  | send.other  |

DirectExchange