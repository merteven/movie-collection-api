package meven.movie.collectionmanagement.collection.services;

import java.time.LocalDateTime;
import meven.movie.collectionmanagement.collection.entities.Collection;
import meven.movie.collectionmanagement.collection.entities.CollectionItem;
import meven.movie.collectionmanagement.collection.exceptions.CollectionItemNotFoundException;
import meven.movie.collectionmanagement.collection.repositories.CollectionItemRepository;
import meven.movie.collectionmanagement.collection.repositories.CollectionRepository;
import meven.movie.collectionmanagement.user.entities.User;
import meven.movie.collectionmanagement.user.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author meven
 */
@DataJpaTest
public class CollectionItemServiceTest {

  @Autowired
  private CollectionItemRepository collectionItemRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CollectionRepository collectionRepository;

  private CollectionItemService collectionItemService;

  private Collection collection;

  @BeforeEach
  public void setup() {
    User user = userRepository.save(new User("meven", "pass", User.Role.USER, LocalDateTime.now()));
    collection = collectionRepository.save(new Collection("name", user, LocalDateTime.now()));
    collectionItemService = new CollectionItemService(collectionItemRepository);
  }

  @Test
  public void testAddItem() {
    CollectionItem item = collectionItemService.add(1, "title", collection);
    assertThat(item.getCollection().getId()).isEqualTo(collection.getId());
  }

  @Test
  public void testAddItemMaxLimit() {
    for (int i = 0; i < 100; i++) {
      collectionItemService.add(i, "title" + i, collection);
    }
    assertThrows(RuntimeException.class,
                 () -> collectionItemService.add(101, "another", collection));
  }

  @Test
  public void testRemoveItem() {
    CollectionItem item = collectionItemService.add(1, "title", collection);
    collectionItemService.remove(1, collection);
    assertThat(collectionItemRepository.findById(item.getId())).isEmpty();
  }

  @Test
  public void testRemoveNonexistingItem() {
    assertThrows(CollectionItemNotFoundException.class,
                 () -> collectionItemService.remove(1, collection));
  }
}
