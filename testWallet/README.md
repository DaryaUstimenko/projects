<h1 align="left">Wallet</h1>


<h3>_Задание выполнила Устименко Дарья_</h3>

######

<h2 align="left">О работе приложения:</h2>

######

<p align="left">
Прложение для управления электронными кошельками создано на основе Spring Boot и предоставляет конечные точки для создания, получения и обновления информации о кошельках.<br><br>
 Приложение имеет два контроллера:<br><br>
<h3>1. Контроллер с RESTful API для управления электронными кошельками.</h3>  <br>
Проверка осуществлялась через postman по адресам:<br><br>
<h4>http://localhost:8080/api/v1/wallet</h4> - для создания кошелька с телом запроса <br>{<br>
operationType: DEPOSIT or WITHDRAW,<br>
amount: 1000<br>
}<br><br>
<h4>http://localhost:8080/api/v1/wallet/{id}</h4> - для обновления данных счета<br><br> 
Значения для операции  (<br><br>
operationType: "WITHDRAW" - для снятия средств, <br><br>operationType: DEPOSIT - для пополнеия счета<br><br>). <br><br>
Пример тела запроса для пополнения: <br>{<br><
  "operationType": "DEPOSIT", <br><br>               
  "amount": 689.0 <br>
}<br><br>
<h4>http://localhost:8080/api/v1/wallets/{id}</h4> - для просмотра баланса кошелька.<br><br>

<h4>2.Контроллер для демонстрации работы приложения через страницы html.</h4>

После запуска приложениния через docker-compose просмотр страницы приложения можно выполнить через браузер по адресу: http://localhost:8080/api/v1/createForm


######

<h2 align="left">Разработка с использованием:</h2>

######

<div align="left">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" height="40" alt="spring logo"/>
  <img width="12" />
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" height="40" alt="java logo"/>
  <img width="12" />
  <img src="https://habrastorage.org/getpro/habr/upload_files/7c8/570/d58/7c8570d580f8647bac4ad65db484108c.png" height="40" alt="ps logo"/>
  <img width="12" />
  <img src="https://i0.wp.com/www.techprevue.com/wp-content/uploads/2021/04/docker-image.jpg" height="40" alt="docker logo"/>
  <img width="16" />
<img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/intellij/intellij-original.svg" height="40" alt="intellij logo"/>
</div>

######

<h2 align="left">Информация о выполненной работе:</h2>
<ul>
<li>
Сервис создан на базе фреймворка Spring Boot.
</li> 
<li>
Подключена база данных Postgresq. <br>Написаны миграции для базы данных с помощью liquibase. 
</li>
<li>
Созданы два котроллера для отображения тела запроса в формате json, и для отображения работы через страницы html. 
</li>
<li>
Добавлена валидация получаемых данных и обработка ошибок. 
</li>
<li>
Приложение запускается через docker-compose. 
</li>
<li>
Через application.yaml есть возможность настраивать различные параметры на стороне
приложения.
</li>
<li>
Эндпоинты быть покрыты тестами
</li> <br>
<h4>
<li>
! К сожалению реализовать работу приложения для использования в конкурентной среде не получилось, опыт использования Spring WebFlux оказался недостаточным, что бы в данном случае выполнить задание с его использованием.
</li>
</h4>
</ul>
