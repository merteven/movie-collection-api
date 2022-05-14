package meven.movie.collectionmanagement.movie.models;

import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author meven
 */
public record MovieDTO(Integer id, String title, String overview, String posterUrl) {

  private static final UriComponentsBuilder POSTER_URI_BUILDER =
      UriComponentsBuilder.fromHttpUrl("https://image.tmdb.org/t/p/w500");

  public MovieDTO(ExternalMovie externalMovie) {
    this(externalMovie.id(), externalMovie.title(), externalMovie.overview(),
         POSTER_URI_BUILDER.path(externalMovie.posterPath()).toUriString());
  }
}
