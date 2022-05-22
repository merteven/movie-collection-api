package meven.movie.collectionmanagement.collection.services;

import java.util.List;
import meven.movie.collectionmanagement.collection.entities.Collection;
import meven.movie.collectionmanagement.collection.entities.CollectionItem;
import meven.movie.collectionmanagement.collection.models.CollectionDTO;
import meven.movie.collectionmanagement.collection.models.CollectionItemAddRequest;
import meven.movie.collectionmanagement.collection.models.CollectionUpsertRequest;
import meven.movie.collectionmanagement.movie.models.MovieDTO;
import meven.movie.collectionmanagement.movie.services.MovieService;
import meven.movie.collectionmanagement.user.entities.User;
import meven.movie.collectionmanagement.user.models.DbUserDetails;
import meven.movie.collectionmanagement.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * @author meven
 */
@Service
public class CollectionOperatorService {

  private final CollectionService collectionService;

  private final CollectionItemService collectionItemService;

  private final UserService userService;

  private final MovieService movieService;

  public CollectionOperatorService(CollectionService collectionService, CollectionItemService collectionItemService,
                                   UserService userService, MovieService movieService) {
    this.collectionService = collectionService;
    this.collectionItemService = collectionItemService;
    this.userService = userService;
    this.movieService = movieService;
  }

  public CollectionDTO create(CollectionUpsertRequest createDto) {
    DbUserDetails userDetails = getCurrentUserDetails();
    User user = userService.getById(userDetails.id());
    return toCollectionDTO(collectionService.create(createDto, user));
  }

  public CollectionDTO getById(Long id) {
    return toCollectionDTO(collectionService.getById(id));
  }

  public Page<CollectionDTO> getAll(Pageable page) {
    return collectionService.getAll(page).map(this::toCollectionDTO);
  }

  public Page<CollectionDTO> getAllByUser(Pageable page) {
    DbUserDetails userDetails = getCurrentUserDetails();
    return collectionService.getAllByUserId(userDetails.id(), page).map(this::toCollectionDTO);
  }

  public CollectionDTO updateName(Long id, String name) {
    DbUserDetails userDetails = getCurrentUserDetails();
    return toCollectionDTO(collectionService.updateName(id, name, userDetails.id()));
  }

  public void delete(Long id) {
    DbUserDetails userDetails = getCurrentUserDetails();
    collectionService.delete(id, userDetails.id());
  }

  public MovieDTO addItem(CollectionItemAddRequest itemAddDTO, Long collectionId) {
    // Checking the movie's existence first
    MovieDTO externalMovieById = movieService.getExternalMovieById(itemAddDTO.movieId());
    //
    DbUserDetails userDetails = getCurrentUserDetails();
    Collection collection = collectionService.getByIdAndUserId(collectionId, userDetails.id());
    collectionItemService.add(itemAddDTO.movieId(), itemAddDTO.title(), collection);
    return externalMovieById;
  }

  public Page<MovieDTO> getItems(Long collectionId, String query, Pageable page) {
    // Checking if the collection exists
    collectionService.getById(collectionId);
    //
    Page<CollectionItem> items = collectionItemService.getItems(collectionId, query, page);
    List<Integer> movieIds = items.stream().map(CollectionItem::getMovieId).toList();
    List<MovieDTO> moviesByIds = movieService.getExternalMoviesByIds(movieIds);
    return new PageImpl<>(moviesByIds, page, items.getTotalElements());
  }

  public void removeItem(Integer externalMovieId, Long collectionId) {
    DbUserDetails userDetails = getCurrentUserDetails();
    Collection collection = collectionService.getByIdAndUserId(collectionId, userDetails.id());
    collectionItemService.remove(externalMovieId, collection);
  }

  private CollectionDTO toCollectionDTO(Collection collection) {
    return new CollectionDTO(collection.getId(), collection.getName(), collection.getUser().getUsername());
  }

  private DbUserDetails getCurrentUserDetails() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return (DbUserDetails) authentication.getPrincipal();
  }
}
