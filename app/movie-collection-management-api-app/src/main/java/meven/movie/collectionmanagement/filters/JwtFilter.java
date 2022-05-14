package meven.movie.collectionmanagement.filters;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import meven.movie.collectionmanagement.services.JWTService;
import meven.movie.collectionmanagement.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtFilter extends OncePerRequestFilter {

  private static final Logger LOGGER = LoggerFactory.getLogger(JwtFilter.class);

  private final UserService userService;

  private final JWTService jwtService;

  public JwtFilter(UserService userService, JWTService jwtService) {
    this.userService = userService;
    this.jwtService = jwtService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String token = parseBearerToken(request);
      if (token != null) {
        UserDetails userDetails = userService.loadUserByUsername(jwtService.extractUsername(token));
        var authentication = createUsernamePasswordAuthentication(request, userDetails);
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch (Exception ex) {
      LOGGER.error("Cannot authenticate the user. Message: {}", ex.getMessage());
    }
    filterChain.doFilter(request, response);
  }

  private UsernamePasswordAuthenticationToken createUsernamePasswordAuthentication(HttpServletRequest request,
                                                                                   UserDetails userDetails) {
    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    return authentication;
  }

  private String parseBearerToken(HttpServletRequest request) {
    String headerAuth = request.getHeader("Authorization");
    if (StringUtils.hasText(headerAuth)) {
      return headerAuth.replace("Bearer ", "");
    }
    return null;
  }
}
