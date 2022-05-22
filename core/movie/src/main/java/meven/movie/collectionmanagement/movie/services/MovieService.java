package meven.movie.collectionmanagement.movie.services;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import meven.movie.collectionmanagement.movie.models.ExternalMovie;
import meven.movie.collectionmanagement.movie.models.MovieDTO;
import meven.movie.collectionmanagement.movie.models.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author meven
 */
@Service
public class MovieService {

  private static final Logger LOGGER = LoggerFactory.getLogger(MovieService.class);

  private static final String MOVIE_API_URI = "https://api.themoviedb.org/3/";

  private final String apiKey;

  private final RestTemplate restTemplate;

  private final AsyncRestTemplate asyncRestTemplate;

  public MovieService(@Value("${external.moviedb.api.key}") String apiKey) {
    this.apiKey = apiKey;
    this.restTemplate = new RestTemplate();
    this.asyncRestTemplate = new AsyncRestTemplate();
  }

  public List<MovieDTO> getExternalMoviesByIds(List<Integer> ids) {
    List<CompletableFuture<ResponseEntity<ExternalMovie>>> futures = ids.stream()
                                                                        .map(this::getFutureExternalMovieById)
                                                                        .toList();
    try {
      CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).get();
    } catch (InterruptedException | ExecutionException ex) {
      LOGGER.error("Fetching movie details from the external service failed. Movie ids: {}.", ids, ex);
      throw new RuntimeException(ex.getMessage());
    }
    return futures.stream()
                  .map(CompletableFuture::join)
                  .map(HttpEntity::getBody)
                  .filter(Objects::nonNull)
                  .map(this::toMovieDTO)
                  .toList();
  }

  public List<MovieDTO> searchExternalMovieByQuery(String query) {
    String url = getMovieApiUriBuilder().path("/search")
                                        .path("/movie")
                                        .queryParam("query", query)
                                        .build()
                                        .toUriString();
    return Objects.requireNonNull(restTemplate.getForObject(url, SearchResult.class))
                  .results()
                  .stream()
                  .map(this::toMovieDTO)
                  .toList();
  }

  private CompletableFuture<ResponseEntity<ExternalMovie>> getFutureExternalMovieById(Integer id) {
    String url = getMovieApiUriBuilder().path("/movie")
                                        .path("/" + id.toString())
                                        .build()
                                        .toUriString();
    return asyncRestTemplate.getForEntity(url, ExternalMovie.class).completable();
  }

  public MovieDTO getExternalMovieById(Integer id) {
    String url = getMovieApiUriBuilder().path("/movie")
                                        .path("/" + id.toString())
                                        .build()
                                        .toUriString();
    return toMovieDTO(Objects.requireNonNull(restTemplate.getForObject(url, ExternalMovie.class)));
  }

  private MovieDTO toMovieDTO(ExternalMovie externalMovie) {
    return new MovieDTO(externalMovie, createMovieImageUrl(externalMovie.posterPath()));
  }

  private UriComponentsBuilder getMovieApiUriBuilder() {
    return UriComponentsBuilder.fromHttpUrl(MOVIE_API_URI).queryParam("api_key", apiKey);
  }

  private String createMovieImageUrl(String path) {
    return UriComponentsBuilder.fromHttpUrl("https://image.tmdb.org/t/p/w500/").path(path).toUriString();
  }
}
