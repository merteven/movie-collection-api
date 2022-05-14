package meven.movie.collectionmanagement.user.repositories;

import java.util.Optional;
import meven.movie.collectionmanagement.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author meven
 */
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username);
}
