## Процедура запуска

### Запуск БД и Payment/Credit Gate в docker
Для запуска переходим в папку artifacts и выполняем комманду 
```bash
docker-compose up
```
По результату в терминале увидим результат запуска
```bash
  { number: '4444 4444 4444 4442', status: 'DECLINED' }
```


### Запуск приложения
Для запуска приложения во второй вкладке терминала переходим в папку artifacts и выполняем команду  
Для работы с MySQL базой данных:
```bash 
 java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" -jar ./artifacts/aqa-shop.jar
```
Для работы с PostgreSQL базой данных:
```bash
java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar ./artifacts/aqa-shop.ja
```

### Запуск тестов
В новой вкладке терминала выполняем команду:  
Для MySQL:
```bash
./gradlew clean test "-Ddb.url=jdbc:mysql://localhost:3306/app"
```
Для PostreSQL:
```bash
./gradlew clean test "-Ddb.url=jdbc:postgresql://localhost:5432/app"
```

### Просмотр результата тестов
Результат тестов можно просмотреть в 
./build/reports/tests/test/index.html
