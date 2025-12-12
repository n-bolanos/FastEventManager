# Whole compose

Run the whole app using the following command.
```
docker compose --project-name "fem" up --build
```


# Specific composes

If you want to test the app in the local environment, the kafka pub/sub and user database containers are composed individually **from their directories** (`/kafka` and `user_db`), using the following command:
```
docker compose up
```
