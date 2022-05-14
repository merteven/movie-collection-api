package meven.movie.collectionmanagement.collection.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author meven
 */
public class CollectionMaxCountExceededException extends AbstractServiceException {

  public CollectionMaxCountExceededException(int maxSize) {
    super(HttpStatus.UNPROCESSABLE_ENTITY, "You cannot create more than %d collections!".formatted(maxSize));
  }
}
