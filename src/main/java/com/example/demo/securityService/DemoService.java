package com.example.demo.securityService;

import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class DemoService {

  private final Repository repository;

  public DemoService() {
    repository = new Repository();
  }

  public User loadUserByUsername(String username) {
    return this.repository.findByUsername(username);
  }

  public List<User> getUserList() {
    return repository.getUserList();
  }

  public User findUser(String username) {
    return repository.findByUsername(username);
  }

  public Role findRole(String roleName) {
    return repository.findByRoleName(roleName);
  }

  public Token findToken(String securityCode) {
    return repository.findToken(securityCode);
  }

  public boolean createUser(String username, String password) {
    return this.repository.addUser(username, password);
  }

  public boolean deleteUser(User toRemove) {
    String username = toRemove.getUsername();
    User inRepository = this.repository.findByUsername(username);
    if (inRepository != null && inRepository.getPassword().equals(toRemove.getPassword())) {
      return this.repository.removeUser(username);
    }
    return false;
  }

  public boolean createRole(String roleName) {
    return this.repository.addRole(roleName);
  }

  public boolean deleteRole(Role role) {
    return this.repository.removeRole(role.getRoleName());
  }

  public boolean addRoleToUser(@NotNull User user, @NotNull Role role) {
    User inRepoUser = repository.findByUsername(user.getUsername());
    Role inRepoRole = repository.findByRoleName(role.getRoleName());
    if (inRepoUser != null && inRepoRole != null
        && inRepoUser.getPassword().equals(user.getPassword())) {
      inRepoUser.addRole(inRepoRole);
      return true;
    }
    return false;
  }

  public String authenticate(String username, String password) {
    User inRepoUser = repository.findByUsername(username);
    if (inRepoUser != null && inRepoUser.validate(password)) {
      String securityCode = Utility.randomString();
      if (repository.addToken(securityCode, inRepoUser)) {
        return securityCode;
      }
    }
    throw new IllegalArgumentException("Wrong password");
  }

  public void Invalidate(String securityCode) {
    Token token = repository.findToken(securityCode);
    token.invalidate();
    repository.removeToken(securityCode);
  }

  private void cleanRoles(User user) {
    List<Role> toRemove = new LinkedList<>();
    for (Role toCheck: user.getRoles()) {
      if (repository.findByRoleName(toCheck.getRoleName()) == null) {
        toRemove.add(toCheck);
      }
    }
    for (Role checked: toRemove) {
      user.removeRole(checked);
    }
  }

  public boolean checkRole(String securityCode, Role role) {
    Token token = repository.findToken(securityCode);
    if (token != null && token.isValid()) {
      cleanRoles(token.getUser());
      return token.getUser().hasRole(role);
    }
    throw new IllegalArgumentException("Invalid token");
  }

  public List<Role> allRoles(String securityCode) {
    Token token = repository.findToken(securityCode);
    if (token.isValid()) {
      cleanRoles(token.getUser());
      return token.getUser().getRoles();
    }
    throw new IllegalArgumentException("Invalid token");
  }

}
