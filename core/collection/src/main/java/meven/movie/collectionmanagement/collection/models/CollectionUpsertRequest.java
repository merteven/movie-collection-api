package meven.movie.collectionmanagement.collection.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author meven
 */
public record CollectionUpsertRequest(@NotBlank @Size(max = 50) String name) {

}
