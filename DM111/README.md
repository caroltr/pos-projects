# Trabalho final da disciplina DM111

## URL do projeto publicado no Google App Engine:
https://projeto-final-201501.appspot.com

## URL de cada serviço desenvolvido, bem como os modelos de requisição e resposta de cada um deles:

### Serviço de usuário:

- **[POST]**		/api/users

**Requisição**:

    {
      "gcmToken": "",
      "email": "user@email.com",
      "password": "password",
      "role": "USER",
      "cpf": "123.456.789-10"
    }

**Resposta**:

    {
        "id": 5629499534213120,
        "gcmToken": "",
        "email": "user@email.com",
        "password": "password",
        "lastLogin": 1524371723571,
        "lastGcmRegister": 1524330488935,
        "role": "USER",
        "cpf": "123.456.789-10",
        "salesId": 0,
        "crmId": 0,
        "enabled": true,
        "username": "user@email.com",
        "authorities": [
            {
                "authority": "USER"
            }
        ],
        "credentialsNonExpired": true,
        "accountNonLocked": true,
        "accountNonExpired": true
    }


- **[GET]**		/api/users/byEmail?email=user@email.com

**Resposta:**

    {
        "id": 5649050225344512,
        "gcmToken": "token",
        "email": "user@email.com",
        "password": "password",
        "lastLogin": 1524326841438,
        "lastGcmRegister": 1524326841438,
        "role": "USER",
        "cpf": "123.456.789-04",
        "salesId": 0,
        "crmId": 0,
        "enabled": true,
        "username": "user@email.com",
        "authorities": [
            {
                "authority": "USER"
            }
        ],
        "credentialsNonExpired": true,
        "accountNonLocked": true,
        "accountNonExpired": true
    }

- **[GET]**		/api/users/byCpf?cpf=123.456.789-04

**Resposta**:

    {
        "id": 5649050225344512,
        "gcmToken": "token",
        "email": "user@email.com",
        "password": "password",
        "lastLogin": 1524326841438,
        "lastGcmRegister": 1524326841438,
        "role": "USER",
        "cpf": "123.456.789-04",
        "salesId": 0,
        "crmId": 0,
        "enabled": true,
        "username": "users@email.com",
        "authorities": [
            {
                "authority": "USER"
            }
        ],
        "credentialsNonExpired": true,
        "accountNonLocked": true,
        "accountNonExpired": true
    }

- **[GET]**		/api/users

**Resposta**:

    [
        {
            "id": 5629499534213120,
            "gcmToken": "",
            "email": "user@email.com",
            "password": "password",
            "lastLogin": 1524330488935,
            "lastGcmRegister": 1524330488935,
            "role": "ADMIN",
            "cpf": "123.456.789-10",
            "salesId": 0,
            "crmId": 0,
            "enabled": true,
            "username": "user@email.com",
            "authorities": [
                {
                    "authority": "ADMIN"
                }
            ],
            "credentialsNonExpired": true,
            "accountNonLocked": true,
            "accountNonExpired": true
        }
    ]

- **[DELETE]**	/api/users/byCpf?cpf=123.456.789-04

**Resposta:**

    {
        "id": 5649050225344512,
        "gcmToken": "token",
        "email": "user@email.com",
        "password": "password",
        "lastLogin": 1524326841438,
        "lastGcmRegister": 1524326841438,
        "role": "USER",
        "cpf": "123.456.789-04",
        "salesId": 0,
        "crmId": 0,
        "enabled": true,
        "username": "user@email.com",
        "authorities": [
            {
                "authority": "USER"
            }
        ],
        "credentialsNonExpired": true,
        "accountNonLocked": true,
        "accountNonExpired": true
    }

---

### Pedido:

- **[POST]**	/api/message/sendOrder

**Requisição:**

    {
    	"userCpf": "123.456.789-03",
    	"reason": "Pedido realizado!",
    	"status": "REALIZADO"
    }

**Resposta:** Status code apenas.

---

### Produto de interesse:

- **[POST]**	/api/productsOfInterest

**Requisição:**

    {
      "userCpf": "123.456.789-03",
      "productId": 2,
      "price": 35
    }

**Resposta:**

    {
        "userCpf": "123.456.789-03",
        "userSalesId": 0,
        "productId": 2,
        "price": 3
    }

- **[GET]**		/api/productsOfInterest/byCpf?cpf=123.456.789-03

**Resposta:**

    [
        {
            "userCpf": "123.456.789-03",
            "userSalesId": 0,
            "productId": 2,
            "price": 3
        },
        {
            "userCpf": "123.456.789-03",
            "userSalesId": 0,
            "productId": 5,
            "price": 180.7
        }
    ]

- **[DELETE]**	/api/productsOfInterest/byCpf?cpf=123.456.789-03&productId=5

**Resposta:**

    {
        "userCpf": "123.456.789-03",
        "userSalesId": 0,
        "productId": 5,
        "price": 180.7
    }

- **[PUT]**		/api/productsOfInterest?productId=5&newPrice=10

**Resposta:** Status code apenas.
