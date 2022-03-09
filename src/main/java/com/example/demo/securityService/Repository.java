package com.example.demo.securityService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Repository {
  private final HashMap<String, User> userMap;
  private final HashMap<String, Role> roles;
  private final HashMap<String, Token> tokenHashMap;


  public Repository() {
    userMap = new HashMap<>();
    roles = new HashMap<>();
    tokenHashMap = new HashMap<>();
  }

  //Look up in repository

  public User findByUsername(String username) {
    return userMap.get(username);
  }

  public Role findByRoleName(String roleName) {
    return roles.get(roleName);
  }

  public Token findToken(String securityCode) {
    return tokenHashMap.get(securityCode);
  }

  // Create records in repository

  public boolean addUser(String username, String password) {
    User existing = this.findByUsername(username);
    if (existing == null) {
      User toAdd = new User(username, password);
      this.userMap.put(username, toAdd);
      return true;
    }
    return false;
  }

  public boolean addRole(String roleName) {
    Role existing = this.findByRoleName(roleName);
    if (existing == null) {
      Role toAdd = new Role(roleName);
      this.roles.put(roleName, toAdd);
      return true;
    }
    return false;
  }

  public boolean addToken(String securityCode, User user) {
    if (!this.tokenHashMap.containsKey(securityCode)) {
      Token toAdd = new Token(securityCode, user);
      this.tokenHashMap.put(securityCode, toAdd);
      return true;
    }
    return false;
  }

  // Remove record in repository

  public boolean removeUser(String username) {
    if (this.userMap.containsKey(username)) {
      this.userMap.remove(username);
      return true;
    }
    return false;
  }

  public boolean removeRole(String roleName) {
    if (this.roles.containsKey(roleName)) {
      this.roles.remove(roleName);
      return true;
    }
    return false;
  }

  public boolean removeToken(String securityCode) {
    if (this.tokenHashMap.containsKey(securityCode)) {
      this.tokenHashMap.remove(securityCode);
      return true;
    }
    return false;
  }

  // Other getter method for testing purpose

  public List<User> getUserList() {
    return new ArrayList<>(this.userMap.values());
  }

}
