package meven.movie.collectionmanagement.collection.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author meven
 */
public class CollectionItemNotFoundException extends AbstractServiceException {

  public CollectionItemNotFoundException() {
    super(HttpStatus.NOT_FOUND, "Collection item not found!");
  }
}
