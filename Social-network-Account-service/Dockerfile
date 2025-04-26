#Базовый образ
FROM openjdk:17-oracle

#Директория приложения внутри приложения
WORKDIR /app

#Копирование JAR в контейнер
COPY target/account-service.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

#Команда для запуска
CMD ["java","-jar","app.jar"]