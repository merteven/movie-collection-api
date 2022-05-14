package meven.movie.collectionmanagement.user.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author meven
 */
public record CreateUserRequest(@NotBlank @Size(max = 25) String username, @NotBlank @Size(max = 64) String password) {

}
