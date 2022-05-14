# README

## Running the app

### Docker

The easiest way is by using Docker. Just run this:

```
make docker-run
```

### Manually

To run it in your local env, you have to:

- Install Java 18 (https://jdk.java.net/18/)
- Point `JAVA_HOME` env variable to Java installation folder (Ex: '/opt/openjdk-18')
- Point `PATH` env variable to Java bin folder (Ex: '/opt/openjdk-18/bin')

Then run these commands in order,
- `make install` (Install dependencies and build the project)
- `make run` (Run the jar package created by the build step)

The last command is `make test`, which will run all the tests in the app.

## How to use

The app has swagger integration at the link: http://localhost:8080/swagger-ui/index.html.

You have to log in via `/api/auth/login` to use user-specific operations.
(If you don't have a user, first visit `/api/auth/register).

It will return a JWT token if succeeded. You need to use it as a bearer token to authenticate.
(ie. your 'Authorization' header should be 'Bearer {token} for every request.')

Collection based operations are under `/api/collections`.

You can use `/api/movies?query=` to search for movies to be added to a collection. It uses [the MovieDB API](https://www.themoviedb.org/documentation/api).

## App Structure

The app consists of four (maven) modules.

### app/movie-collection-management-api-app

Its main task is running the app itself. It also stores the controllers, which define the API paths and operations. Most operations are delegated to the entity-specific modules except the security ones.

### core/collection

Handles collection-related DB operations.

### core/user

Handles user-related DB operations.

### core/movie

Communicates with the MovieDB API to search and find movies

## Notes

- The app uses a file-based H2 database that initializes during the first run. Usually, one prefers regular DB like MySQL or Postgresql, but I chose H2 for convenience.
- I didn't set up an expiration date for the JWT to remove the need to log in multiple times during testing. An app in production should have one.
- All secrets are located in the file `application.properties`. These secrets should be provided by a CI tool or some remote key-vault service in the real world.
- Currently, we are storing only the external movie ids in the collections. While fetching the collection items, we need to make an API call to get movie metadata. It could be better to store this data in our DB by setting up a cron job that dumps the data every month. 
