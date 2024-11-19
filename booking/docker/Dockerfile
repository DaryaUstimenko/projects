#Базовый образ
FROM openjdk:17-oracle

#Директория приложения внутри приложения
WORKDIR /app

#Копирование JAR в контейнер
COPY build/libs/booking-0.0.1-SNAPSHOT.jar app.jar

ENV KAFKA_BROKERCONNECT kafka:9092

# Указываем порт, который будет прослушивать приложение
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

#Команда для запуска
CMD ["java","-jar","app.jar"]