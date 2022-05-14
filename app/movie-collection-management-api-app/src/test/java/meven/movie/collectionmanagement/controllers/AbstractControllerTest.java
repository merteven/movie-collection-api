package meven.movie.collectionmanagement.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import meven.movie.collectionmanagement.handlers.JWTUnauthorizedHandler;
import meven.movie.collectionmanagement.services.JWTService;
import meven.movie.collectionmanagement.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author meven
 */
public abstract class AbstractControllerTest {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  @Autowired
  protected MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @MockBean
  private JWTUnauthorizedHandler unauthorizedHandler;

  @MockBean
  private JWTService jwtService;

  protected String asJsonString(Object obj) {
    try {
      return OBJECT_MAPPER.writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
