package meven.movie.collectionmanagement.controllers;

import java.util.List;
import javax.validation.constraints.NotBlank;
import meven.movie.collectionmanagement.movie.models.MovieDTO;
import meven.movie.collectionmanagement.movie.services.MovieService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author meven
 */
@RestController
@RequestMapping("/api/movies")
public class MovieController {

  private final MovieService movieService;

  public MovieController(MovieService movieService) {
    this.movieService = movieService;
  }

  @GetMapping
  public List<MovieDTO> get(@NotBlank @RequestParam String query) {
    return movieService.searchExternalMovieByQuery(query);
  }
}
