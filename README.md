# Java Multithreading Bank

Задача состоит в разработке приложения, способного эффективно обрабатывать переводы средств между банковскими счетами, 
учитывая множественные потоки и обеспечивая безопасность данных.

## Описание

Приложение создает несколько банковских счетов с начальным балансом и запускает несколько потоков, 
каждый из которых выполняет переводы средств между счетами. После выполнения заданного количества транзакций 
приложение завершается.

## Требования

- Java SE 8.0 или выше.
- Использование библиотек и фреймворков на усмотрение разработчика (кроме Spring и Lombok).
- Система логирования (например, Log4j) для записи действий и обработки ошибок.
- Параллельное выполнение транзакций с использованием многопоточности.
- Запись результатов транзакций в лог.

## Использование

1. Клонировать репозиторий: