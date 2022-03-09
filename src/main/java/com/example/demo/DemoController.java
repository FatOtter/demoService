package com.example.demo;

import com.example.demo.securityService.Role;
import com.example.demo.securityService.User;
import com.example.demo.securityService.DemoService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

  @Autowired
  private DemoService demoService;

  @RequestMapping("/getUsers")
  public List<User> getUserList() {
    return demoService.getUserList();
  }

  @RequestMapping(method = RequestMethod.POST, value = "/createUser")
  public String createUser(@RequestBody Map<String, String> payload) {
    String username = payload.get("username");
    String password = payload.get("password");
    if (demoService.createUser(username, password)) {
      return String.format("User %s has been created.", username);
    }
    return "Creation failed!";
  }

  @RequestMapping(method = RequestMethod.POST, value = "/deleteUser")
  public String deleteUser(@RequestBody User user) {
    if (demoService.deleteUser(user)) {
      return String.format("User %s has been deleted.", user.getUsername());
    }
    return "Deletion failed!";
  }

  @RequestMapping(method = RequestMethod.POST, value = "/createRole")
  public String createRole(@RequestBody Map<String, String> payload) {
    String roleName = payload.get("roleName");
    if (roleName != null && demoService.createRole(roleName)) {
      return String.format("Role %s has been created.", roleName);
    }
    return "Creation failed!";
  }

  @RequestMapping(method = RequestMethod.POST, value = "/deleteRole")
  public String deleteRole(@RequestBody Map<String, String> payload) {
    Role role = new Role(payload.get("roleName"));
    if (demoService.deleteRole(role)) {
      return String.format("Role %s has been deleted.", role.getRoleName());
    }
    return "Deletion failed!";
  }

  @RequestMapping(method = RequestMethod.POST, value = "/assignRole")
  public String addRoleToUser(@RequestBody Map<String, String> payload) {
    String username = payload.get("username");
    String roleName = payload.get("roleName");
    User user = demoService.findUser(username);
    Role role = demoService.findRole(roleName);
    if (role != null && user != null && demoService.addRoleToUser(user, role)) {
      return String.format("Role %s has been added to user %s.", roleName, username);
    }
    return "Adding failed!";
  }

  @RequestMapping(method = RequestMethod.POST, value = "/authentication")
  public String authentication(@RequestBody Map<String, String> payload) {
    String username = payload.get("username");
    String password = payload.get("password");
    String token = demoService.authenticate(username, password);
    return String.format("Authentication success, token=\n%s", token);
  }

  @RequestMapping(method = RequestMethod.POST, value = "/invalidate")
  public String invalidate(@RequestBody Map<String, String> payload) {
    String token = payload.get("token");
    demoService.Invalidate(token);
    return "Invalidation of token has been processed.";
  }

  @RequestMapping(method = RequestMethod.POST, value = "/checkRole")
  public String checkRole(@RequestBody Map<String, String> payload) {
    String token = payload.get("token");
    String roleName = payload.get("roleName");
    Role role = new Role(roleName);
    if (demoService.checkRole(token, role)) {
      return String.format("User belongs to role %s.", roleName);
    }
    return String.format("User does not belong to role %s", roleName);
  }

  @RequestMapping(method = RequestMethod.POST, value = "/allRoles")
  public List<Role> allRoles(@RequestBody Map<String, String> payload) {
    String token = payload.get("token");
    return demoService.allRoles(token);
  }
}
