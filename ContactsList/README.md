<br/>
<p align="center">
  <a href="https://github.com/ShaanCoding/ReadME-Generator">

[//]: # (    <img src="src/main/resources/img/icons-контакты.png" alt="Logo" width="80" height="80">)
  </a>
<h1 align="center">СПИСОК КОНТАКТОВ</h1>

### Содержание:

* [О приложении](#О-приложении)
* [Приступая к работе](#приступая-к-работе)
* [Использование](#Использование)
* [Автор](#Автор)

<h2 align="left">Разработка с использованием:</h2>

######

<div align="left">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" height="40" alt="spring logo"  />
  <img width="12" />
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" height="40" alt="java logo"  />
  <img width="12" />
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/intellij/intellij-original.svg" height="40" alt="intellij logo"  />
</div>

## О приложении

Данное приложение дает возможность создать, просмотреть и удалить данные о контактах пользователя через интерфейс.

Интерфейс приложения реализован по адресу: http://localhost:8080/

Приложение дает возможность:
* Добавить пользователя через форму
* Видеть список всех контактов
* Изменить данные выбранного пользователя
* Удалить выбранного пользователя

### Приступая к работе
Приложение работает с базой данных, поэтому перед запуском необходимо подключиться к БД.

Для этого, перейдя в папку с приложением, запустите в командной строке:  

    cd docker
    docker-compose up

Приложение можно запустить двумя способами:

Скачайте архив с приложением, распакуйте его в папку.
1. Запуск приложения осуществляется в среде разработки.
   
   Запустите метод ContactsApplication.main() командой Run.
   
   
2. Запуск можно осуществить через командную строку.

   Для этого,перейдя в папку с приложением, запустите в командной строке:

  
    ./gradlew clean bootJar
    ./gradlew bootRun

Затем перейдите в браузер, для использования интерфейса приложения, по адресу:
http://localhost:8080/
###Использование

После запуска, приложение выведет через интерфейс форму:

    Contact Management

    ID	FirstName	LastName	Email	Phone
    
    Create task

  * Нажмите на:   _Create task_  - для добавления контакта 
  * В появившейся форме заполните все поля контакта данными, нажмите _Save_
  * В добавленном контакте вы можете отредактировать данные, нажав: _Edit_
  * Либо удалить добавленный контакт, нажав: _Delete_
  
### Автор
* **Устименко Дарья** - *Студент курсов Skillbox* - [DaryaUstimenko](https://github.com/DaryaUstimenko/projects) 
