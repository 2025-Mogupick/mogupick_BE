# --- Java 17 JRE 기반 이미지 ---
FROM eclipse-temurin:17-jre-alpine

# 앱 실행 디렉토리
WORKDIR /app

# JAR 파일 복사 (GitHub Actions에서 ./gradlew bootJar 실행 후 build/libs에 생성)
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# JVM 옵션은 환경변수 JAVA_OPTS로 주입 가능 (.env에서 관리)
ENV JAVA_OPTS=""

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
