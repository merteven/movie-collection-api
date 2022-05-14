package meven.movie.collectionmanagement.user.services;

import java.time.LocalDateTime;
import meven.movie.collectionmanagement.user.entities.User;
import meven.movie.collectionmanagement.user.models.CreateUserRequest;
import meven.movie.collectionmanagement.user.models.UserDTO;
import meven.movie.collectionmanagement.user.repositories.UserRepository;
import meven.movie.collectionmanagement.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author meven
 */
@DataJpaTest
public class UserServiceTest {

  @Autowired
  private UserRepository userRepository;

  private UserService userService;

  @BeforeEach
  public void setup() {
    userService = new UserService(userRepository);
  }

  @Test
  public void testAddUser() {
    CreateUserRequest request = new CreateUserRequest("meven", "pass");
    UserDTO user = userService.create(request);

    assertThat(user.username()).isEqualTo(request.username());
  }

  @Test
  public void testGetById() {
    User user = userRepository.save(new User("meven", "pass", User.Role.USER, LocalDateTime.now()));

    assertThat(userService.getById(user.getId())).isEqualTo(user);
  }

  @Test
  public void testGetByUsername() {
    String username = "meven";
    User user = userRepository.save(new User(username, "pass", User.Role.USER, LocalDateTime.now()));

    assertThat(userService.loadUserByUsername(username).getUsername()).isEqualTo(user.getUsername());
  }

}
