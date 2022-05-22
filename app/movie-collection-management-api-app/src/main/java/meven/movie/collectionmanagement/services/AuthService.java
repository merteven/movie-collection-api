package meven.movie.collectionmanagement.services;

import meven.movie.collectionmanagement.models.LoginRequest;
import meven.movie.collectionmanagement.models.UserJwtDTO;
import meven.movie.collectionmanagement.user.models.CreateUserRequest;
import meven.movie.collectionmanagement.user.models.DbUserDetails;
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

  public UserJwtDTO login(LoginRequest loginRequest) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));
    String jwt = jwtService.generateToken(authentication);
    DbUserDetails userDetails = (DbUserDetails) authentication.getPrincipal();
    return new UserJwtDTO(userDetails.username(), userDetails.role(), jwt);
  }

  public UserJwtDTO register(CreateUserRequest request) {
    String encodedPassword = passwordEncoder.encode(request.password());
    userService.create(new CreateUserRequest(request.username(), encodedPassword));
    return login(new LoginRequest(request.username(), request.password()));
  }
}
