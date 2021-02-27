### SimpleAuthProvider

Простая реализация Auth Provider.
Для создания аккаунтов загялите в Main#verifyPassword.

Авторизация с этой системой выглядит примерно так:
```json
"auth": [
  {
    "provider": {
    "type": "request",
    "usePermission": true,
    "flagsEnabled": false,
    "url": "http://127.0.0.1/auth?username=%login%&password=%password%&ip=%ip%",
    "response": "OK:(?<username>.+):(?<permissions>.+)"
    }
  }
]
```

Автор в вк - https://vk.com/badcodemylife
