package meven.movie.collectionmanagement.collection.repositories;

import meven.movie.collectionmanagement.collection.entities.CollectionItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author meven
 */
public interface CollectionItemRepository extends JpaRepository<CollectionItem, Long> {

  Page<CollectionItem> findAllByCollectionId(Long collectionId, Pageable page);

  boolean existsByMovieIdAndCollectionId(Integer movieId, Long collectionId);

  Page<CollectionItem> findAllByCollectionIdAndTitleContainingIgnoreCase(Long collectionId, String title, Pageable page);

  void deleteByMovieIdAndCollectionId(Integer movieId, Long collectionId);
}
