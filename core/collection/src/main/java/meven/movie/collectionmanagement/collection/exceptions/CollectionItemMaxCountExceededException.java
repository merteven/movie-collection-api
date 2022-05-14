package meven.movie.collectionmanagement.collection.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author meven
 */
public class CollectionItemMaxCountExceededException extends AbstractServiceException {

  public CollectionItemMaxCountExceededException(int maxSize) {
    super(HttpStatus.UNPROCESSABLE_ENTITY, "Collections can have %d items at most!".formatted(maxSize));
  }
}
