package com.example.demo.securityService;
import java.util.LinkedList;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class User {
  private final String username;
  private final String password;
  private LinkedList<Role> roles;

  public User(String username, String password) {
    this.username = username;
    this.password = Utility.encode(password);
    this.roles = new LinkedList<>();
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public LinkedList<Role> getRoles() {
    return roles;
  }

  public void addRole(Role role) {
    this.roles.add(role);
  }

  public void removeRole(Role role) {
    this.roles.remove(role);
  }

  public boolean hasRole(Role role) {
    return roles.stream().anyMatch(r -> r.getRoleName().equals(role.getRoleName()));
  }

  public boolean validate(String password) {
    String hashed = Utility.encode(password);
    return hashed.equals(this.password);
  }
}
