Тест для повторения ошибки переполнения буффера для ByteArrayBuffer при чтении потока размером более 1Gb 
-----------

Запуск теста: ```gradle test```

Для предотвращения переполнения буффера в метод write(InputStream in) класса ByteArrayBuffer добавлена проверка с выбросом исключительной ситуации:
```
if (cap == 0) {                 // Prevent infinite loop
    int size = buf.length;
    buf = null;                 // Help GC clear effects
    throw new IllegalStateException("Buffer overflow, read: " + count + " bytes, buffer size: " + size);
}
``` 