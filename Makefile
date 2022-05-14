build:
	./mvnw clean install

test:
	./mvnw test

run:
	java -jar app/movie-collection-management-api-app/target/movie-collection-management-api-app-0.0.1-SNAPSHOT.jar

docker-run:
	docker build -t movie_collection_api . && docker run --rm -p 8080:8080 movie_collection_api
