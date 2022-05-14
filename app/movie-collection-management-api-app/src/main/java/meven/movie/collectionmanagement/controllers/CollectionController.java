package meven.movie.collectionmanagement.controllers;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import meven.movie.collectionmanagement.collection.models.CollectionDTO;
import meven.movie.collectionmanagement.collection.models.CollectionItemAddRequest;
import meven.movie.collectionmanagement.collection.models.CollectionUpsertRequest;
import meven.movie.collectionmanagement.collection.services.CollectionOperatorService;
import meven.movie.collectionmanagement.movie.models.MovieDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author meven
 */
@RestController
@RequestMapping("/api/collections")
public class CollectionController {

  private final CollectionOperatorService collectionOperatorService;

  public CollectionController(CollectionOperatorService collectionOperatorService) {
    this.collectionOperatorService = collectionOperatorService;
  }

  @PostMapping
  public CollectionDTO create(@Valid @RequestBody CollectionUpsertRequest collectionUpsertRequest) {
    return collectionOperatorService.create(collectionUpsertRequest);
  }

  @GetMapping(value = "/{id}")
  public CollectionDTO getById(@PathVariable Long id) {
    return collectionOperatorService.getById(id);
  }

  @GetMapping
  public List<CollectionDTO> get(@RequestParam(defaultValue = "0") Integer page,
                                 @RequestParam(defaultValue = "10") @Max(10) Integer size) {
    return collectionOperatorService.getAll(PageRequest.of(page, size));
  }

  @GetMapping(value = "/owned")
  public List<CollectionDTO> getOwned(@RequestParam(defaultValue = "0") Integer page,
                                      @RequestParam(defaultValue = "10") @Max(10) Integer size) {
    return collectionOperatorService.getAllByUser(PageRequest.of(page, size));
  }

  @PatchMapping("/{id}/name")
  public CollectionDTO updateName(@PathVariable Long id, @Valid @RequestBody CollectionUpsertRequest upsertDTO) {
    return collectionOperatorService.updateName(id, upsertDTO.name());
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    collectionOperatorService.delete(id);
  }

  @PostMapping("/{id}/items")
  public MovieDTO addItem(@PathVariable Long id, @Valid @RequestBody CollectionItemAddRequest itemAddDTO) {
    return collectionOperatorService.addItem(itemAddDTO, id);
  }

  @GetMapping("/{id}/items")
  public List<MovieDTO> getItems(@PathVariable Long id,
                                 @RequestParam(required = false) String query,
                                 @RequestParam(defaultValue = "0") Integer page,
                                 @RequestParam(defaultValue = "10") @Max(10) Integer size) {
    return collectionOperatorService.getItems(id, query, PageRequest.of(page, size));
  }

  @DeleteMapping("/{id}/items/{movieId}")
  public void removeItem(@PathVariable Long id, @PathVariable Integer movieId) {
    collectionOperatorService.removeItem(movieId, id);
  }
}
