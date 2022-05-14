package meven.movie.collectionmanagement.collection.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author meven
 */
public class CollectionNotFoundException extends AbstractServiceException {

  public CollectionNotFoundException() {
    super(HttpStatus.NOT_FOUND, "Collection not found!");
  }
}
