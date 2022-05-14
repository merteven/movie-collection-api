package meven.movie.collectionmanagement.user.service;

import java.time.LocalDateTime;
import java.util.Optional;
import meven.movie.collectionmanagement.user.entities.User;
import meven.movie.collectionmanagement.user.exceptions.UserAlreadyExistsException;
import meven.movie.collectionmanagement.user.models.CreateUserRequest;
import meven.movie.collectionmanagement.user.models.DbUserDetails;
import meven.movie.collectionmanagement.user.models.UserDTO;
import meven.movie.collectionmanagement.user.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author meven
 */
@Service
public class UserService implements UserDetailsService {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public UserDTO create(CreateUserRequest request) {
    String username = request.username();
    Optional<User> byUsername = userRepository.findByUsername(username);
    if (byUsername.isPresent()) {
      throw new UserAlreadyExistsException(username);
    }
    User user = new User(username, request.password(), User.Role.USER, LocalDateTime.now());
    return toUserDTO(userRepository.save(user));
  }

  public User getById(Long id) {
    Optional<User> user = userRepository.findById(id);
    if (user.isEmpty()) {
      LOGGER.error("Non-existing user is requested. How? User id: {}", id);
      throw new UsernameNotFoundException("User %s not found".formatted(id));
    }
    return user.get();
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
                              .orElseThrow(
                                  () -> new UsernameNotFoundException("User %s not found".formatted(username)));
    return new DbUserDetails(user);
  }

  private UserDTO toUserDTO(User user) {
    return new UserDTO(user.getUsername(), user.getRole());
  }
}
