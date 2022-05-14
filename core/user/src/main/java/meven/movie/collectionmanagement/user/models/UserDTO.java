package meven.movie.collectionmanagement.user.models;

import meven.movie.collectionmanagement.user.entities.User;

/**
 * @author meven
 */
public record UserDTO(String username, User.Role role) {

}
