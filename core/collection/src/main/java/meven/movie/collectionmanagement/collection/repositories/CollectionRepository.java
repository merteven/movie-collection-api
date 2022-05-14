package meven.movie.collectionmanagement.collection.repositories;

import java.util.Optional;
import meven.movie.collectionmanagement.collection.entities.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author meven
 */
public interface CollectionRepository extends JpaRepository<Collection, Long> {

  Optional<Collection> findByIdAndUserId(Long id, Long userId);

  Page<Collection> findAllByUserId(Long userId, Pageable page);

  Optional<Collection> findByNameAndUserId(String name, Long userId);

  Integer countByUserId(Long userId);
}
