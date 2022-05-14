package meven.movie.collectionmanagement.collection.services;

import java.time.LocalDateTime;
import java.util.List;
import meven.movie.collectionmanagement.collection.entities.Collection;
import meven.movie.collectionmanagement.collection.entities.CollectionItem;
import meven.movie.collectionmanagement.collection.exceptions.CollectionItemAlreadyExistsException;
import meven.movie.collectionmanagement.collection.exceptions.CollectionItemMaxCountExceededException;
import meven.movie.collectionmanagement.collection.exceptions.CollectionItemNotFoundException;
import meven.movie.collectionmanagement.collection.repositories.CollectionItemRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.util.StringUtils.hasText;

/**
 * @author meven
 */
@Service
class CollectionItemService {

  private static final int MAX_ITEM_COUNT = 100;

  private final CollectionItemRepository collectionItemRepository;

  CollectionItemService(CollectionItemRepository collectionItemRepository) {
    this.collectionItemRepository = collectionItemRepository;
  }

  public CollectionItem add(Integer externalMovieId, String title, Collection collection) {
    List<CollectionItem> collectionItems =
        collectionItemRepository.findAllByCollectionId(collection.getId(), PageRequest.of(0, MAX_ITEM_COUNT))
                                .stream()
                                .toList();
    if (collectionItems.stream()
                       .map(CollectionItem::getMovieId)
                       .anyMatch(id -> id.equals(externalMovieId))) {
      throw new CollectionItemAlreadyExistsException();
    }
    if (MAX_ITEM_COUNT == collectionItems.size()) {
      throw new CollectionItemMaxCountExceededException(MAX_ITEM_COUNT);
    }
    return collectionItemRepository.save(new CollectionItem(collection, externalMovieId, title, LocalDateTime.now()));
  }

  public List<CollectionItem> getItems(Long collectionId, String query, Pageable page) {
    if (hasText(query)) {
      return collectionItemRepository.findAllByCollectionIdAndTitleContainingIgnoreCase(collectionId, query, page)
                                     .toList();
    }
    return collectionItemRepository.findAllByCollectionId(collectionId, page).toList();
  }

  @Transactional
  public void remove(Integer externalMovieId, Collection collection) {
    boolean exists = collectionItemRepository.existsByMovieIdAndCollectionId(externalMovieId, collection.getId());
    if (!exists) {
      throw new CollectionItemNotFoundException();
    }
    collectionItemRepository.deleteByMovieIdAndCollectionId(externalMovieId, collection.getId());
  }
}
