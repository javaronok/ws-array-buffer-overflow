���� ��� ���������� ������ ������������ ������� ��� ByteArrayBuffer ��� ������ ������ �������� ����� 1Gb 
-----------

������ �����: ```gradle test```

��� �������������� ������������ ������� � ����� write(InputStream in) ������ ByteArrayBuffer ��������� �������� � �������� �������������� ��������:
```
if (cap == 0) {                 // Prevent infinite loop
    int size = buf.length;
    buf = null;                 // Help GC clear effects
    throw new IllegalStateException("Buffer overflow, read: " + count + " bytes, buffer size: " + size);
}
``` 