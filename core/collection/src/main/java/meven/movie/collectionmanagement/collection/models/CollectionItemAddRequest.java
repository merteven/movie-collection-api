package meven.movie.collectionmanagement.collection.models;

import javax.validation.constraints.NotNull;

/**
 * @author meven
 */
public record CollectionItemAddRequest(@NotNull Integer movieId) {

}
