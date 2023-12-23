# Шамаев Онар БПИ-227
# Приложение для работников кинотеатра
## О работе:
Реализованы как основные требуемые функции, так и функционал регистрации/авторизации пользователей системы.
Был разработан очень подробный интерфейс для пользователей системы.
Архитектура приложения проектировалась по принципам ООП и SOLID.
JSON формат для сериализации данных был выбран в связи с своей объектно-ориентированностью, так что можно легко изменять данные через их файлы.
UML диаграмы представлены в формате `.drawio` и располагаются в папке `/diagrams` репозитория.
## Особенности реализации:
Интерфейс представлен в виде "консольных виджетов".
Но так как я не нашёл способа очищать терминал IDE, то весь интерфейс целевого виджета почти каждый раз перерисовывается.
Тем самым он пишется прям подряд за предыдущим "консольным виджетом".
## Важно:
Нельзя удалять папки `/film_sessions`, `/films` и `/user_profiles`. Они генерируются автоматически, но это можно обойти, так что удалять папки нельзя.
## Туториал:
Сначала Вас встретит виджет авторизации/регистрации:

![изображение](https://github.com/Enzhine/SD_PHW1/assets/52751569/28f27b51-3fe0-4bfa-ba68-59c674ec6473)

Необходимо выбрать авторизацию/регистрацию. Ввод происходит следующим образом: мы печатаем что-либо в терминал и нажимаем Enter. То есть тут для авторизации мы вводим `0`.

![изображение](https://github.com/Enzhine/SD_PHW1/assets/52751569/04436326-5446-485a-839c-09abb378f643)

Далее нас просят ввести логин, а затем пароль. В системе уже есть профиль с логином `admin` и паролем `admin`. Вводим его.

![изображение](https://github.com/Enzhine/SD_PHW1/assets/52751569/8756f726-e946-4b57-851b-398177a6cc83)

Теперь нам открылось основное меню приложения. В опции `Manage films (N)` в скобочках на месте `N` пишется число существующих фильмов. В опции `Manage films-sessions (N)` в скобочках на месте `N` пишется число существующих сеансов. Выберем опцию под номером `1`.

![изображение](https://github.com/Enzhine/SD_PHW1/assets/52751569/a85c5a9c-4a08-420b-ae89-a74c726d1bbd)

Теперь кроме привычных опций у нас появляются опции выбора фильма для редактирования. Среди доступных фильмов выберем тот, у которого id равен 1, то есть введём `F1`.

![изображение](https://github.com/Enzhine/SD_PHW1/assets/52751569/0ed90a70-3f39-4084-9c66-a68cbfcea752)

Перед нами предстаёт описание фильма и возможный функционал. Далее всё работает аналогично. Мы хотим что-то изменить - вводим новое значение и оно меняется у фильма. Интерфейс достаточно интуитивен и все основные функции работают подобным образом. Однако важно уточнить моменты, которые могут вызвать трудности.

В разделе редактирования сеансов при его создании / изменении даты начала сеанса необходимо ввести дату и время. Выглядит это следующим образом.

![изображение](https://github.com/Enzhine/SD_PHW1/assets/52751569/5398fe50-2610-4f46-ad04-4ef8d3e6927d)

Формат указывается в скобочках: `yyyy-MM-dd HH:mm:ss`. То есть `год-месяц-день часы-минуты-секунды`, например `2023-12-24 23:59:00`. И именно такой ввод программа принимает.

![изображение](https://github.com/Enzhine/SD_PHW1/assets/52751569/69ad978c-8038-44e0-b505-16d63deee836)

Ещё трудности могут возникнуть в разделе управления билетами. При выборе доступного сеанса мы видим рассадку всех мест. По вертикали отображаются ряды (rows), по горизонтали места (places).

![изображение](https://github.com/Enzhine/SD_PHW1/assets/52751569/0a02ec8c-6d3d-4e57-8cec-592ca0028587)

Далее выбирая какую-либо из опций сначала мы указываем число билетов, которые хотим продать / вернуть / зарегистрировать(отметить пришедшего клиента). Потом предстоит указать сами места в зале. Указывать их необходимо через пробел сначала ряд, затем место, затем, если это опция возврата / регистрации билета, то и ключ билета мы тоже указываем. Примеры:

![изображение](https://github.com/Enzhine/SD_PHW1/assets/52751569/775857cb-6540-49e4-b7ad-90cc2ff1817d)

![изображение](https://github.com/Enzhine/SD_PHW1/assets/52751569/c9c224ab-9f3a-434c-8b56-a7bb49db4495)

При этом важно вводить настоящий ключ билета, чтобы подтвердить, что клиент действительно приобрёл таковой.
## Тонкости работы программы
Программа старается быть максимально интуитивной и полезной. Поэтому многие тонкости проработаны. Например, нельзя создать сеансы, если фильмов нет. Сеансы не могут накладываться друг на друга, так как кинозал всего один. Нельзя редактировать длину фильма, если уже есть сеансы и это может привести к их наложению. В разделе редактивроания билетов отображаются только те сеансы, которые будут идти или идут сейчас. Ещё вся программа обрабатывает некорректный ввод, но не всегда разрешает отменить действие, поэтому лучше не вызывать те действия, которые делать не собираешься (будь это не консольный интерфейс, то эту проблему можно было бы решить адекватно).
