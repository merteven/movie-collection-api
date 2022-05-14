package meven.movie.collectionmanagement.handlers;

import javax.validation.ConstraintViolationException;
import meven.movie.collectionmanagement.collection.exceptions.AbstractServiceException;
import meven.movie.collectionmanagement.user.exceptions.AbstractUserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

  @ExceptionHandler(value = {AbstractServiceException.class})
  protected ResponseEntity<Object> handleServiceErrors(AbstractServiceException ex, WebRequest request) {
    return getResponse(ex.getHttpStatus(), ex, request);
  }

  @ExceptionHandler(value = {AbstractUserException.class})
  protected ResponseEntity<Object> handleUserErrors(AbstractUserException ex, WebRequest request) {
    return getResponse(ex.getHttpStatus(), ex, request);
  }

  @ExceptionHandler(value = {ConstraintViolationException.class})
  protected ResponseEntity<Object> handleConstraintError(RuntimeException ex, WebRequest request) {
    return getResponse(HttpStatus.BAD_REQUEST, ex, request);
  }

  @ExceptionHandler(value = {RuntimeException.class})
  protected ResponseEntity<Object> handleUnknownError(RuntimeException ex, WebRequest request) {
    return getResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex, request);
  }

  private ResponseEntity<Object> getResponse(HttpStatus status, Exception ex, WebRequest request) {
    if (status.is5xxServerError()) {
      LOGGER.error(ex.getMessage());
    } else {
      LOGGER.debug(ex.getMessage());
    }
    return handleExceptionInternal(ex, new ApiErrorResponse(status.value(), ex.getMessage()), new HttpHeaders(), status,
                                   request);
  }

  record ApiErrorResponse(int status, String message) {

  }
}
