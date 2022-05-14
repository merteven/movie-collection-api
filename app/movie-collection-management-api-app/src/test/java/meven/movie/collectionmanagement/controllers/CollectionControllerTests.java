package meven.movie.collectionmanagement.controllers;

import meven.movie.collectionmanagement.collection.models.CollectionDTO;
import meven.movie.collectionmanagement.collection.models.CollectionItemAddRequest;
import meven.movie.collectionmanagement.collection.models.CollectionUpsertRequest;
import meven.movie.collectionmanagement.collection.services.CollectionOperatorService;
import meven.movie.collectionmanagement.movie.models.MovieDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author meven
 */
@WebMvcTest(CollectionController.class)
public class CollectionControllerTests extends AbstractControllerTest {

  @MockBean
  private CollectionOperatorService collectionOperatorService;

  @Test
  public void addItemShouldAcceptValidRequest() throws Exception {
    MovieDTO movieDTO = new MovieDTO(1, "title", "t", "t");
    when(collectionOperatorService.addItem(any(), any())).thenReturn(movieDTO);
    CollectionItemAddRequest request = new CollectionItemAddRequest(1, "title");

    this.mockMvc.perform(post("/api/collections/1/items")
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(asJsonString(request)))
                .andExpect(status().isOk());
  }

  @Test
  public void updateNameShouldAcceptValidRequest() throws Exception {
    CollectionUpsertRequest request = new CollectionUpsertRequest("name");
    CollectionDTO response = new CollectionDTO(1L, "name", "me");
    when(collectionOperatorService.updateName(any(), any())).thenReturn(response);

    this.mockMvc.perform(patch("/api/collections/1/name")
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(asJsonString(request)))
                .andExpect(status().isOk());
  }
}
