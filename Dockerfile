# 第一阶段：构建阶段
FROM maven:3.8.6-openjdk-21 AS build
WORKDIR /app

# 复制 pom.xml 和下载依赖
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 复制源码并打包
COPY src ./src
RUN mvn clean package -DskipTests

# 第二阶段：运行阶段
FROM openjdk:21-jdk-alpine
WORKDIR /app

# 将构建阶段的 JAR 文件复制到运行阶段
COPY --from=build /app/target/messenger-0.0.1-SNAPSHOT.jar app.jar

# 暴露应用程序运行的端口
EXPOSE 8080

# 设置应用程序的启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]