# Avito tech trainee 

[https://github.com/avito-tech/android-trainee-task-2021](Тестовое задание)

Тагилов А.М.

## Локализация
- Только RU

## Технологии
 - Kotlin
 - Compose
 - MVVM
 - OkHttp, Retrofit
 - 

## Фичи к реализации
* Геолокация (+API запрос)
* Подробная информация о суточной погоде по нажатию)
* Экран ошибки
* Полная поддержка темной темы
* Мб персистентное хранилище с последним городом и последней погодой?
* Если пользователь отказался выдать локацию, то используем geo по ip
* SwipeToRefresh
* Rationale для пермишена
* Хорошо бы собрать через ProGuard на релизе, чтобы api ключи не разбазаривать)


## API
- openweathermap

```
// погода
https://api.openweathermap.org/data/2.5/onecall?lon=37.6156&lat=55.7522&appid=b66745312676702b882b8e673d774421&exclude=minutely&lang=ru&units=metric

// найти город по названию (и его гео)
https://api.openweathermap.org/geo/1.0/direct?q=Moscow&appid=b66745312676702b882b8e673d774421&limit=1

// найти название города по локации
https://api.openweathermap.org/geo/1.0/reverse?lon=37.6156&lat=55.7522&appid=b66745312676702b882b8e673d774421&limit=1

//локация по ip
https://api.ipgeolocation.io/ipgeo?apiKey=dd8bacba38c84ee4a11cbe0614b07322
```

### Экран погоды
* Название города (топ бар)
* Отображение текущей погоды (решил делать без иконки, так красИвее, тем более чуть ниже та же инфо)
* Погода на день (горизонтальный скролл) + иконки
* Погода на неделю (вертикальные элементы) + иконки
* SwipeToRefresh
* Хорошо бы разделить ошибки подключения/сервера


### Экран поиска
* Задержка перед поиском (модификатор flow)


### TODO
* 

### Замечания
 - Думал сделать простенький Koin DI на основе, но с навигацией есть [https://github.com/InsertKoinIO/koin/issues/1079](некоторые проблемы), поэтому, чтобы не интегрировать что-то сложнее, решил отказаться от DI.
 - Пермишен можно запросить через гугловскую либу [https://medium.com/compose-in-the-room/requesting-permissions-with-ease-in-jetpack-compose-using-accompanist-permissions-apis-76d5d9ca5f97](Accompanist), но хочу поработать напрямую
 - Да и вообще сильно ли тут и нужна локация, если можно ее получать из апи. Видимо обычно приложения либо запоминают последнее место, либо получают по своему api.