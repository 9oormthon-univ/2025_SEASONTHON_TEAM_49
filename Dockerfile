# 1. 빌드 환경: Gradle + JDK 17
FROM gradle:8.5-jdk17 AS builder

# 작업 디렉토리
WORKDIR /app

# Gradle Wrapper 먼저 복사 (권한 + 줄바꿈 LF 필수)
COPY gradlew .
COPY gradle ./gradle
RUN chmod +x gradlew

# build.gradle, settings.gradle 복사
COPY build.gradle settings.gradle ./

# 의존성 캐시 (선택적)
RUN ./gradlew dependencies --no-daemon || return 0

# 프로젝트 전체 복사
COPY . .

# 테스트 제외하고 빌드
RUN ./gradlew build -x test --no-daemon

# 2. 실행 환경 (경량 JDK)
FROM openjdk:17-jdk-slim
WORKDIR /app

# 빌드된 JAR 복사 (와일드카드 사용)
COPY --from=builder /app/build/libs/*.jar app.jar

# Spring Boot 실행
CMD ["java", "-jar", "app.jar"]
