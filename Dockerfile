# 第一阶段：构建阶段
FROM openjdk:21

# 安装必要的工具
RUN apk add --no-cache curl tar bash

# 设置 Maven 版本
ENV MAVEN_VERSION=3.9.9

# 下载并安装 Maven
RUN mkdir /opt/maven \
    && curl -fsSL https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz | tar -xzC /opt/maven --strip-components=1 \
    && ln -s /opt/maven/bin/mvn /usr/bin/mvn

# 设置工作目录
WORKDIR /app

# 复制 pom.xml 并下载依赖
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 复制源码并打包
COPY src ./src
RUN mvn clean package -DskipTests

# 第二阶段：运行阶段
FROM openjdk:21
WORKDIR /app

# 复制构建阶段的 JAR 文件
COPY --from=build /messenger/target/messenger-0.0.1-SNAPSHOT.jar app.jar

# 暴露应用程序运行的端口
EXPOSE 8080

# 设置应用程序的启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]