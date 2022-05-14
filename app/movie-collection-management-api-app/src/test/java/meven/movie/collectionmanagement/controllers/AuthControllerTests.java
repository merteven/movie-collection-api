package meven.movie.collectionmanagement.controllers;

import meven.movie.collectionmanagement.models.JwtDTO;
import meven.movie.collectionmanagement.models.LoginRequest;
import meven.movie.collectionmanagement.services.AuthService;
import meven.movie.collectionmanagement.user.entities.User;
import meven.movie.collectionmanagement.user.models.CreateUserRequest;
import meven.movie.collectionmanagement.user.models.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author meven
 */
@WebMvcTest(AuthController.class)
public class AuthControllerTests extends AbstractControllerTest {

  @MockBean
  private AuthService authService;

  @Test
  public void loginShouldAcceptValidUsernameAndPassword() throws Exception {
    LoginRequest request = new LoginRequest("username", "password");
    JwtDTO response = new JwtDTO("jwt_token");
    when(authService.login(request)).thenReturn(response);

    this.mockMvc.perform(post("/api/auth/login")
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(response)));
  }

  @Test
  public void loginShouldRejectInvalidUsernameOrPassword() throws Exception {
    LoginRequest request = new LoginRequest("", "password");

    this.mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(asJsonString(request)))
                .andExpect(status().isBadRequest());
  }

  @Test
  public void registerShouldAcceptValidUsernameAndPassword() throws Exception {
    CreateUserRequest request = new CreateUserRequest("username", "password");
    UserDTO response = new UserDTO("merteven", User.Role.USER);
    when(authService.register(request)).thenReturn(response);

    this.mockMvc.perform(post("/api/auth/register")
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(response)));
  }

  @Test
  public void registerShouldRejectInvalidUsernameOrPassword() throws Exception {
    CreateUserRequest request = new CreateUserRequest("", "");
    this.mockMvc.perform(post("/api/auth/register")
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(asJsonString(request)))
                .andExpect(status().isBadRequest());
  }
}
