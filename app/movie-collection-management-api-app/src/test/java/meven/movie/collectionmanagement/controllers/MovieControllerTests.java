package meven.movie.collectionmanagement.controllers;

import java.util.List;
import meven.movie.collectionmanagement.movie.models.MovieDTO;
import meven.movie.collectionmanagement.movie.services.MovieService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author meven
 */
@WebMvcTest(MovieController.class)
public class MovieControllerTests extends AbstractControllerTest {

  @MockBean
  private MovieService movieService;

  @Test
  public void getShouldAcceptValidQuery() throws Exception {
    String query = "batman";
    List<MovieDTO> response = List.of();
    when(movieService.searchExternalMovieByQuery(query)).thenReturn(response);

    this.mockMvc.perform(get("/api/movies")
                             .param("query", query)
                             .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(response)));
  }
}
