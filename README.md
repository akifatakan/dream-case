## Akif Atakan Yilmaz

This project was developed as a case study for backend engineering of Dream Games.

### Startup

**1. Create MySql Database**

```bash
create database dream_case
```

* First time you run the program, tables will be created automatically. After first run of the program, make sure to change `spring.jpa.hibernate.ddl-auto` property in `src/main/resources/application.yaml` from `create` to `update` to keep the data.

**2. Change mysql username and password as per your installation**

+ open `src/main/resources/application.yaml`
+ change `spring.datasource.username` and `spring.datasource.password` as per your mysql installation

**4. Run the app using maven**

```bash
mvn spring-boot:run
```
The app will start running at <http://localhost:8080>

### User

| Method | Url                                    | Description                                                 | Sample Valid Request Body |
|--------|----------------------------------------|-------------------------------------------------------------|---------------------------|
| GET    | /api/user/get-all                      | Get All Users                                               |                           |
| POST   | /api/user/create                       | Create User                                                 | [JSON](#createuser)       |
| GET    | /api/user/get/{id}                     | Get User with id                                            |                           |
| PUT    | /api/user/update-level/{id}            | Update level of user with id                                |                           |

### Tournament


| Method | Url                                                | Description                                        | Sample Valid Request Body |
|--------|----------------------------------------------------|----------------------------------------------------|--------------------|
| POST   | /api/tournament/enter-tournament/{userId}          | Enter tournament with user id                      |                    |
| POST   | /api/tournament/claim-reward/{userId}              | Claim tournament reward with user id               |                    |
| GET    | /api/tournament/group-leaderboard/{groupId}        | Get Group Leaderboard for today with groupId       |                    |
| GET    | /api/tournament/country-leaderboard?day=YYYY-MM-DD | Get Country leaderboard for day YYYY-MM-DD         |                    |
| GET    | /api/tournament/country-leaderboard                | Get Country Leaderboard for today's tournament     |                    |
| GET    | /api/tournament/group-rank?userId=X&day=YYYY-MM-DD | Get group rank of user with id X on day YYYY-MM-DD |                    |
| GET    | /api/tournament/group-rank?userId=X                | Get group rank of user with id X for today         |                    |

## Sample Valid JSON request Bodys

##### <a id= "createuser"> Create User -> /api/user/create </a>
```json
{
  "username" : "akifatakan"
}
```


### Personal Notes

* This was my first time ever working with Spring, or Java in general. Thus there might be some mistakes and practices I did not use. I believe everything will get better with practice.
* More filtering options can be added.
