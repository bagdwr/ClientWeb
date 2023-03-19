# ClientWeb

1. Чтобы скачать проект с github запускаем команду:
   ```git clone https://github.com/bagdwr/ClientWeb.git```


2. В application.properties есть url базы данных
   ```jdbc:postgresql://localhost:8080/project```

   Чтобы запустить postgres, у вас необходимо быть установлен ```docker```.
   После установки docker запустить скрипт ```recreate.bash```
   Этот скрипт вам автоматически запустит postgres, создаст root аккаунт и бд **project**


3. Запускаете программу.
   Программа запускается на порту 8000


4. Ждём, когда запуститься сервер. После запуска сервера, заходим в браузер по адресу:
   ```http://localhost:8000/```


5. Дальше можете тестировать)

6. После проверки можете, чтобы отключить postgres запустите скрипт ```down.bash```.

PS: ```remove.bash``` полностью удаляет volume, ```restart.bash``` делает рестарт контейнера