package meven.movie.collectionmanagement.user.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author meven
 */
public class UserAlreadyExistsException extends AbstractUserException {

  public UserAlreadyExistsException(String username) {
    super(HttpStatus.UNPROCESSABLE_ENTITY, "User with name %s already exists.".formatted(username));
  }
}
