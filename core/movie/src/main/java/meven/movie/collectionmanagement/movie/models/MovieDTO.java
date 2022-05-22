package meven.movie.collectionmanagement.movie.models;

/**
 * @author meven
 */
public record MovieDTO(Integer id, String title, String overview, String posterUrl) {

  public MovieDTO(ExternalMovie externalMovie, String posterUrl) {
    this(externalMovie.id(), externalMovie.title(), externalMovie.overview(), posterUrl);
  }
}
