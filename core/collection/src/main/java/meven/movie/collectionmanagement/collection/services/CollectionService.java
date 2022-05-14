package meven.movie.collectionmanagement.collection.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import meven.movie.collectionmanagement.collection.entities.Collection;
import meven.movie.collectionmanagement.collection.exceptions.CollectionMaxCountExceededException;
import meven.movie.collectionmanagement.collection.exceptions.CollectionNameAlreadyExistsException;
import meven.movie.collectionmanagement.collection.exceptions.CollectionNotFoundException;
import meven.movie.collectionmanagement.collection.models.CollectionUpsertRequest;
import meven.movie.collectionmanagement.collection.repositories.CollectionRepository;
import meven.movie.collectionmanagement.user.entities.User;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author meven
 */
@Service
class CollectionService {

  private static final int MAX_COLLECTION_COUNT = 10;

  private final CollectionRepository collectionRepository;

  public CollectionService(CollectionRepository collectionRepository) {
    this.collectionRepository = collectionRepository;
  }

  public Collection create(CollectionUpsertRequest createDto, User user) {
    Long userId = user.getId();
    Integer countByUserId = collectionRepository.countByUserId(userId);
    if (MAX_COLLECTION_COUNT == countByUserId) {
      throw new CollectionMaxCountExceededException(MAX_COLLECTION_COUNT);
    }
    String name = createDto.name();
    Optional<Collection> withSameName = collectionRepository.findByNameAndUserId(name, userId);
    if (withSameName.isPresent()) {
      throw new CollectionNameAlreadyExistsException(name);
    }
    Collection collection = new Collection(name, user, LocalDateTime.now());
    return collectionRepository.save(collection);
  }

  public Collection getById(Long id) {
    return collectionRepository.findById(id).orElseThrow(CollectionNotFoundException::new);
  }

  public Collection getByIdAndUserId(Long id, Long userId) {
    return collectionRepository.findByIdAndUserId(id, userId).orElseThrow(CollectionNotFoundException::new);
  }

  public List<Collection> getAll(Pageable page) {
    return collectionRepository.findAll(page).stream().toList();
  }

  public List<Collection> getAllByUserId(Long userId, Pageable page) {
    return collectionRepository.findAllByUserId(userId, page).stream().toList();
  }

  public Collection update(Collection existingCollection) {
    existingCollection.setUpdatedAt(LocalDateTime.now());
    return collectionRepository.save(existingCollection);
  }

  public Collection updateName(Long id, String name, Long userId) {
    Collection existingCollection = getByIdAndUserId(id, userId);
    existingCollection.setName(name);
    return update(existingCollection);
  }

  public void delete(Long id, Long userId) {
    Collection existingCollection = getByIdAndUserId(id, userId);
    collectionRepository.delete(existingCollection);
  }
}
