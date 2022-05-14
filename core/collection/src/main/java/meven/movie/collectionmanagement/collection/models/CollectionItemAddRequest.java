package meven.movie.collectionmanagement.collection.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author meven
 */
public record CollectionItemAddRequest(@NotNull Integer movieId, @NotBlank @Size(max = 50) String title) {

}
