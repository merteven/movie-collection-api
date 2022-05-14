package meven.movie.collectionmanagement.user.models;

import java.util.Collection;
import java.util.List;
import meven.movie.collectionmanagement.user.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author meven
 */
public record DbUserDetails(Long id, String username, String password, String role) implements UserDetails {

  public DbUserDetails(User user) {
    this(user.getId(), user.getUsername(), user.getPassword(), user.getRole().name());
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role()));
  }

  @Override
  public String getPassword() {
    return password();
  }

  @Override
  public String getUsername() {
    return username();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
