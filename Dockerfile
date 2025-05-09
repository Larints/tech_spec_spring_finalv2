# Используем официальное изображение для OpenJDK
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем скомпилированный jar-файл приложения в контейнер
COPY target/subscriptions-0.0.1-SNAPSHOT.jar /app/subscriptions-app.jar

# Открываем порт, на котором будет работать приложение
EXPOSE 8080

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "/app/subscriptions-app.jar"]
