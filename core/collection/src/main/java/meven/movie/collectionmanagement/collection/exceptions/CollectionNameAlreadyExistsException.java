package meven.movie.collectionmanagement.collection.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author meven
 */
public class CollectionNameAlreadyExistsException extends AbstractServiceException {

  public CollectionNameAlreadyExistsException(String name) {
    super(HttpStatus.UNPROCESSABLE_ENTITY,
          "You already have a collection with the name %s. Please choose another!".formatted(name));
  }
}
