package meven.movie.collectionmanagement.collection.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author meven
 */
public class CollectionItemAlreadyExistsException extends AbstractServiceException {

  public CollectionItemAlreadyExistsException() {
    super(HttpStatus.UNPROCESSABLE_ENTITY, "Collection item already exists!");
  }
}
