package meven.movie.collectionmanagement.collection.entities;

import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import meven.movie.collectionmanagement.user.entities.User;

/**
 * @author meven
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "user_id" }) })
public class Collection {

  @Id
  @GeneratedValue
  @Column(nullable = false, updatable = false)
  private Long id;

  @Column(nullable = false)
  private String name;

  @OneToMany(mappedBy = "collection", cascade= CascadeType.ALL, orphanRemoval=true)
  private Set<CollectionItem> collectionItems;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="user_id", nullable = false, updatable = false)
  private User user;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime updatedAt;

  public Collection() {
  }

  public Collection(String name, User user, LocalDateTime now) {
    this.name = name;
    this.user = user;
    this.createdAt = now;
    this.updatedAt = now;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }
}
