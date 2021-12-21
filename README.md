# Avito tech trainee 

[Тестовое задание](https://github.com/avito-tech/android-trainee-task-2021)

Тагилов А.М.

## Локализация
- Только RU

## Технологии
 - Kotlin
 - Compose
 - MVVM
 - Kotlin Flow
 - OkHttp, Retrofit
 - Jetpack Compose Navigation
 - Glide

## Фичи к реализации
* Геолокация+
* Подробная информация о суточной погоде по нажатию)+
* Экран ошибки+
* Полная поддержка темной темы+
* Мб персистентное хранилище с последним городом и последней погодой? ОЧЕНЬ ХОЧУ СДЕЛАТЬ ПОКА НЕКОГДА (TODO)
* Если пользователь отказался выдать локацию, то используем geo по ip+
* SwipeToRefresh+
* Rationale для пермишена- (TODO)
* Хорошо бы собрать через ProGuard на релизе, чтобы api ключи не разбазаривать) (TODO)


## API
- openweathermap

```
// погода+
https://api.openweathermap.org/data/2.5/onecall?lon=37.6156&lat=55.7522&appid=b66745312676702b882b8e673d774421&exclude=minutely&lang=ru&units=metric

// найти город (и его гео) по названию+
https://api.openweathermap.org/geo/1.0/direct?q=Moscow&appid=b66745312676702b882b8e673d774421&limit=1

// найти название города по локации+
https://api.openweathermap.org/geo/1.0/reverse?lon=37.6156&lat=55.7522&appid=b66745312676702b882b8e673d774421&limit=1

//локация по ip+
https://api.ipgeolocation.io/ipgeo?apiKey=dd8bacba38c84ee4a11cbe0614b07322
```

### Экран погоды
* Название города (топ бар)+
* Отображение текущей погоды (решил делать без иконки, так красИвее, тем более чуть ниже та же инфо)+
* Погода на день (горизонтальный скролл) + иконки+
* Погода на неделю (вертикальные элементы) + иконки+
* SwipeToRefresh
* Хорошо бы разделить ошибки подключения/сервера
* Переключение между сохраненными городами+локацией
* Если контент загружен, то его обновление не приведет к потере текущих данных, будет отображен снекбар
* Да, кстати, макс и мин актуальные температуры вычисляются на основе следующих 24 часов, а не являются данными текущего дня

### Экран поиска
* Задержка перед поиском (посмотреть модификаторы flow). upd. блин он в превью, ну ладно, пусть будет
* Отображение сохраненных городов (TODO)
* Удаление сохраненных городов (мб свайпом как на айфоне)
* Основная логика такова - отображаем сохраненные, если есть что-то в строке поиска (сделал, но персистентного хранилища пока нет)

### TODO
* Иконки переделать в ассеты+
* Проверить, не едет ли верстка на экранах маленьких размеров и на ветре > 10мс (TODO)


### Замечания
 - Думал сделать простенький DI на основе Koin, но с навигацией есть [некоторые проблемы](https://github.com/InsertKoinIO/koin/issues/1079), поэтому, чтобы не интегрировать что-то сложнее, решил отказаться от DI.
 - Пермишен можно запросить через гугловскую либу [Accompanist](https://medium.com/compose-in-the-room/requesting-permissions-with-ease-in-jetpack-compose-using-accompanist-permissions-apis-76d5d9ca5f97), но хочу поработать напрямую
 - Да и вообще сильно ли тут и нужна локация, если можно ее получать из апи. Видимо обычно приложения либо запоминают последнее место, либо получают по своему api.
 - Ищет, конечно, openweather не очень. Возвращает вроде список городов, а по факту всегда один, да и тот без каких-либо данных о погоде
 - Плохо, конечно, что data слой напрямую мапит в ui модели
 - Все-таки решил не сохранять последнюю погоду в персистентном хранилище, это погода и нельзя надеяться на то, что она не поменяется (но добавить сохранение самого города стоит - ПОЗЖЕ)
 - Вообще итоговая идея nullable типов для верстки мне не нравится - все-таки лучше логику шиммера и контента разграничить
 - Честно так и не придумал, как правильно организовать хранение данных синглтона Networking с okhttp клиентом и апи, ведь это привязано по сути к одному экрану. Поэтому решил оставить для каждого экрана свой синглтон, что не есть хорошо(
 - Стоит анализировать ошибки при обращении к API
 - (TODO) Логика сохранения лежит на экране погоды. Если город не из текущей локации, то сохраняем (нужно, чтобы пользователь не ждал перехода на экран прогноза)
 - MVI все-таки с Compose больше вяжется(
 - Тк нет особо опыта с Compose, сделал ошибку - куча стейтов. Стейт должен быть один (общий), чтобы по несколько раз не рекомпозировать