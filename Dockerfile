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




#
# # 빌드 환경 (Gradle 포함) 설정
# FROM gradle:8.5-jdk17 AS build
#
# # 작업 디렉토리 설정
# WORKDIR /app
#
# # 의존성 캐싱을 위한 Gradle 파일 먼저 복사
# #COPY build.gradle settings.gradle gradlew ./
# #COPY gradle ./gradle
# COPY gradlew ./gradlew
# COPY gradle ./gradle
# COPY build.gradle ./build.gradle
# COPY settings.gradle ./settings.gradle
#
# # 프로젝트의 모든 파일을 컨테이너 내부로 복사
# COPY . .
#
# # Gradle 실행 권한 부여
# RUN chmod +x gradlew
#
# # 의존성 미리 다운 (캐시 최적화)
# RUN ./gradlew dependencies --no-daemon || return 0
#
#
#
# #실행할 수 있는 권한 명령어
# RUN chmod +x gradlew
#
#
# # 테스트는 제외하고 빌드
# RUN ./gradlew build -x test --no-daemon
#
# # 실행 환경 (경량 JDK) 설정
# FROM openjdk:17-jdk-slim
#
# WORKDIR /app
#
# # 빌드된 JAR 복사 (JAR 이름 명시)
# # Default JAR 이름: {rootProject.name}-{build.gradle.version}.jar
# # build.gradle에 archiveFileName을 작성하면 그대로 들어감
# COPY --from=build /app/build/libs/app.jar app.jar
#
# # Spring Boot 실행
# CMD ["java", "-jar", "app.jar"]
#
