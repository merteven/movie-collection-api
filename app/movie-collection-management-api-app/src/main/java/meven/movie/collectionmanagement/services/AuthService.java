package meven.movie.collectionmanagement.services;

import meven.movie.collectionmanagement.models.JwtDTO;
import meven.movie.collectionmanagement.models.LoginRequest;
import meven.movie.collectionmanagement.user.models.CreateUserRequest;
import meven.movie.collectionmanagement.user.models.UserDTO;
import meven.movie.collectionmanagement.user.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author meven
 */
@Service
public class AuthService {

  private final AuthenticationManager authenticationManager;

  private final JWTService jwtService;

  private final UserService userService;

  private final PasswordEncoder passwordEncoder;

  public AuthService(AuthenticationManager authenticationManager, JWTService jwtService, UserService userService,
                     PasswordEncoder passwordEncoder) {
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
  }

  public JwtDTO login(LoginRequest loginRequest) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));
    String jwt = jwtService.generateToken(authentication);
    return new JwtDTO(jwt);
  }

  public UserDTO register(CreateUserRequest request) {
    String encodedPassword = passwordEncoder.encode(request.password());
    return userService.create(new CreateUserRequest(request.username(), encodedPassword));
  }
}
