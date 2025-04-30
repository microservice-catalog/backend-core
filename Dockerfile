# Этап 1: Сборка проекта с помощью Gradle
FROM gradle:8.13-jdk17 AS build

WORKDIR /app

# Копируем файлы проекта
COPY gradlew .
COPY settings.gradle .
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY src ./src

# Выполняем сборку проекта с помощью Gradle
RUN chmod +x ./gradlew
RUN ./gradlew bootJar -x test

# Этап 2: Запуск приложения
FROM openjdk:17-alpine

WORKDIR /app

# Копируем собранный JAR файл
COPY --from=build /app/build/libs/*.jar app.jar

# Указываем переменные окружения
EXPOSE $APP_PORT

# Запуск приложения
ENTRYPOINT ["java", "-Xmx2g", "-jar", "/app/app.jar"]
