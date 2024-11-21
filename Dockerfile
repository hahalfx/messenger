# 构建阶段
FROM openjdk:21 AS build

# 设置工作目录
WORKDIR /app

# 复制项目文件并构建
COPY . .
RUN ./mvnw clean package -DskipTests

# 运行阶段
FROM openjdk:21

# 复制构建好的 JAR 文件
COPY --from=build /app/target/messenger-0.0.1-SNAPSHOT.jar app.jar

# 暴露应用程序运行的端口（根据需要修改）
EXPOSE 8080

# 设置入口点
ENTRYPOINT ["java", "-jar", "app.jar"]