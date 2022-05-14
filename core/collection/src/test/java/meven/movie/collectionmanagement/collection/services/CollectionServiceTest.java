package meven.movie.collectionmanagement.collection.services;

import java.time.LocalDateTime;
import java.util.List;
import meven.movie.collectionmanagement.collection.entities.Collection;
import meven.movie.collectionmanagement.collection.models.CollectionUpsertRequest;
import meven.movie.collectionmanagement.collection.repositories.CollectionRepository;
import meven.movie.collectionmanagement.user.entities.User;
import meven.movie.collectionmanagement.user.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author meven
 */
@DataJpaTest
public class CollectionServiceTest {

  @Autowired
  private CollectionRepository collectionRepository;

  @Autowired
  private UserRepository userRepository;

  private CollectionService collectionService;

  @BeforeEach
  public void setup() {
    collectionService = new CollectionService(collectionRepository);
  }

  @Test
  public void testGetById() {
    User user = userRepository.save(new User("meven", "pass", User.Role.USER, LocalDateTime.now()));
    Collection created = collectionRepository.save(new Collection("name", user, LocalDateTime.now()));

    Collection byId = collectionService.getById(created.getId());
    assertThat(byId).isEqualTo(created);
  }

  @Test
  public void testGetByIdAndUserId() {
    User user = userRepository.save(new User("meven", "pass", User.Role.USER, LocalDateTime.now()));
    Collection created = collectionRepository.save(new Collection("name", user, LocalDateTime.now()));

    Collection byId = collectionService.getByIdAndUserId(created.getId(), user.getId());
    assertThat(byId).isEqualTo(created);
  }

  @Test
  public void testCreate() {
    User user = userRepository.save(new User("meven", "pass", User.Role.USER, LocalDateTime.now()));

    CollectionUpsertRequest request = new CollectionUpsertRequest("name");
    Collection byId = collectionService.create(request, user);
    assertThat(byId.getUser().getId()).isEqualTo(user.getId());
    assertThat(byId.getName()).isEqualTo(request.name());
  }

  @Test
  public void testMaxCreateLimit() {
    User user = userRepository.save(new User("meven", "pass", User.Role.USER, LocalDateTime.now()));

    for (int i = 0; i < 10; i++) {
      CollectionUpsertRequest request = new CollectionUpsertRequest("name"+i);
      collectionService.create(request, user);
    }
    assertThrows(RuntimeException.class, () -> {
      CollectionUpsertRequest request = new CollectionUpsertRequest("another");
      collectionService.create(request, user);
    });
  }

  @Test
  public void testUpdateName() {
    User user = userRepository.save(new User("meven", "pass", User.Role.USER, LocalDateTime.now()));
    Collection created = collectionRepository.save(new Collection("name", user, LocalDateTime.now()));

    Long userId = user.getId();
    String newName = "newName";
    Collection byId = collectionService.updateName(created.getId(), newName, userId);
    assertThat(byId.getUser().getId()).isEqualTo(userId);
    assertThat(byId.getName()).isEqualTo(newName);
  }

  @Test
  public void testDelete() {
    User user = userRepository.save(new User("meven", "pass", User.Role.USER, LocalDateTime.now()));
    Collection created = collectionRepository.save(new Collection("name", user, LocalDateTime.now()));

    Long userId = user.getId();
    collectionService.delete(created.getId(), userId);
    assertThat(collectionRepository.findById(created.getId())).isEmpty();
  }

  @Test
  public void testGetAll() {
    User user = userRepository.save(new User("meven", "pass", User.Role.USER, LocalDateTime.now()));
    collectionRepository.save(new Collection("name", user, LocalDateTime.now()));

    List<Collection> first = collectionService.getAll(PageRequest.of(0, 10));
    assertThat(first).hasSize(1);
    List<Collection> second = collectionService.getAll(PageRequest.of(1, 10));
    assertThat(second).hasSize(0);
  }

  @Test
  public void testGetAllByUserId() {
    User user = userRepository.save(new User("meven", "pass", User.Role.USER, LocalDateTime.now()));
    User unknown = userRepository.save(new User("unknown", "pass", User.Role.USER, LocalDateTime.now()));
    collectionRepository.save(new Collection("name", user, LocalDateTime.now()));

    List<Collection> byUser = collectionService.getAllByUserId(user.getId(), PageRequest.of(0, 10));
    assertThat(byUser).hasSize(1);
    List<Collection> byUnknown = collectionService.getAllByUserId(unknown.getId(), PageRequest.of(1, 10));
    assertThat(byUnknown).hasSize(0);
  }
}
