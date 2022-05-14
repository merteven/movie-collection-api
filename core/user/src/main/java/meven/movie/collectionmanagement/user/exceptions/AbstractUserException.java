package meven.movie.collectionmanagement.user.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author meven
 */
public abstract class AbstractUserException extends RuntimeException {

  private final HttpStatus httpStatus;

  public AbstractUserException(HttpStatus httpStatus, String message) {
    super(message);
    this.httpStatus = httpStatus;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }
}
