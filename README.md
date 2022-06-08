### SimpleAuthProvider

Простая реализация (затычка) Auth Provider.
Для создания аккаунтов загляните в Main#verifyPassword.

Авторизация с этим скриптом выглядит вот так:
```json
"auth": [
  {
    "provider": {
      "type": "request",
      "usePermission": true,
      "flagsEnabled": false,
      "url": "http://localhost:8001/auth?username=%login%&password=%password%&ip=%ip%",
      "response": "OK:(?<username>.+):(?<permissions>.+)"
    }
  }
]
```

Автор - https://thelivan.ru/
