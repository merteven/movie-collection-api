package meven.movie.collectionmanagement.controllers;

import javax.validation.Valid;
import meven.movie.collectionmanagement.models.JwtDTO;
import meven.movie.collectionmanagement.models.LoginRequest;
import meven.movie.collectionmanagement.services.AuthService;
import meven.movie.collectionmanagement.user.models.CreateUserRequest;
import meven.movie.collectionmanagement.user.models.UserDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author meven
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/register")
  public UserDTO register(@Valid @RequestBody CreateUserRequest request) {
    return authService.register(request);
  }

  @PostMapping("/login")
  public JwtDTO login(@Valid @RequestBody LoginRequest request) {
    return authService.login(request);
  }
}
