package meven.movie.collectionmanagement.movie.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author meven
 */
public record ExternalMovie(Integer id, String title, String overview, @JsonProperty("poster_path") String posterPath) {

}
