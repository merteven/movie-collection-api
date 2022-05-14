package meven.movie.collectionmanagement.models;

import javax.validation.constraints.NotBlank;

/**
 * @author meven
 */
public record LoginRequest(@NotBlank String username, @NotBlank String password) {

}
