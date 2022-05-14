package meven.movie.collectionmanagement.movie.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * @author meven
 */
public record SearchResult(Integer page, List<ExternalMovie> results,
                           @JsonProperty("total_results") Integer totalResults,
                           @JsonProperty("total_pages") Integer totalPages) {

}
