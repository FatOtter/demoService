package com.example.demo;

import com.example.demo.securityService.DemoService;
import com.example.demo.securityService.Role;
import com.example.demo.securityService.Token;
import com.example.demo.securityService.User;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DemoApplicationTests {

	private DemoService demoService;

	static List<String> usernameList = List.of(
			"Spring", "Summer", "Autumn", "Winter"
	);

	static List<String> passwordList = List.of(
			"0123456789", "abcdefg", "hijklmnop", "p@ssw0rd"
	);

	static List<String> roleList = List.of(
			"Wind", "Fire", "Water", "Earth"
	);

	@BeforeEach
	void setUp() {
		demoService = new DemoService();
	}

	@Test
	void createNewUser() {
		assertTrue(demoService.createUser(usernameList.get(0), passwordList.get(0)));
		assertTrue(demoService.findUser(usernameList.get(0)).validate(passwordList.get(0)));
	}

	@Test
	void createExistingUser() {
		demoService.createUser(usernameList.get(0), passwordList.get(0));
		assertFalse(demoService.createUser(usernameList.get(0), passwordList.get(1)));
	}

	@Test
	void deleteExistingUser() {
		demoService.createUser(usernameList.get(0), passwordList.get(0));
		User user = demoService.findUser(usernameList.get(0));
		assertTrue(demoService.deleteUser(user));
	}

	@Test
	void deleteNonExistingUser() {
		User user = new User(usernameList.get(1), passwordList.get(1));
		assertFalse(demoService.deleteUser(user));
	}

	@Test
	void createNewRole() {
		assertTrue(demoService.createRole(roleList.get(0)));
		assertNotEquals(null, demoService.findRole(roleList.get(0)));
	}

	@Test
	void createExistingRole() {
		demoService.createRole(roleList.get(0));
		assertFalse(demoService.createRole(roleList.get(0)));
	}

	@Test
	void deleteExistingRole() {
		demoService.createRole(roleList.get(0));
		Role role = demoService.findRole(roleList.get(0));
		assertTrue(demoService.deleteRole(role));
	}

	@Test
	void deleteNonExistingRole() {
		assertNull(demoService.findRole(roleList.get(1)));
		Role role = new Role(roleList.get(1));
		assertFalse(demoService.deleteRole(role));
	}

	@Test
	void assignRoleNormal() {
		demoService.createUser(usernameList.get(0), passwordList.get(0));
		demoService.createRole(roleList.get(0));
		User user = demoService.findUser(usernameList.get(0));
		Role role = demoService.findRole(roleList.get(0));
		assertTrue(demoService.addRoleToUser(user, role));
	}

	@Test
	void assignRoleToNonExistingUser() {
		demoService.createUser(usernameList.get(0), passwordList.get(0));
		demoService.createRole(roleList.get(0));
		User user = new User(usernameList.get(1), passwordList.get(1));
		Role role = demoService.findRole(roleList.get(0));
		assertNull(demoService.findUser(usernameList.get(1)));
		assertFalse(demoService.addRoleToUser(user, role));
	}

	@Test
	void assignNonExistingRole() {
		demoService.createUser(usernameList.get(0), passwordList.get(0));
		User user = demoService.findUser(usernameList.get(0));
		Role role = new Role(roleList.get(1));
		assertNull(demoService.findRole(roleList.get(1)));
		assertFalse(demoService.addRoleToUser(user, role));
	}

	@Test
	void authenticationNormal() {
		demoService.createUser(usernameList.get(0), passwordList.get(0));
		String securityCode = demoService.authenticate(usernameList.get(0), passwordList.get(0));
		assertNotNull(securityCode);
		Token token = demoService.findToken(securityCode);
		assertTrue(token.isValid());
	}

	@Test
	void authenticationWrongPassword() {
		assertThrows(IllegalArgumentException.class, () -> {
			demoService.createUser(usernameList.get(0), passwordList.get(0));
			User user = demoService.findUser(usernameList.get(0));
			assertFalse(user.validate(passwordList.get(1)));
			demoService.authenticate(usernameList.get(0), passwordList.get(1));
		});
	}

	@Test
	void authenticationNonExistingUser() {
		assertThrows(IllegalArgumentException.class, () -> {
			assertNull(demoService.findUser(usernameList.get(2)));
			demoService.authenticate(usernameList.get(2), passwordList.get(2));
		});
	}

	@Test
	void invalidateTest() {
		demoService.createUser(usernameList.get(0), passwordList.get(0));
		String securityCode = demoService.authenticate(usernameList.get(0), passwordList.get(0));
		assertNotNull(securityCode);
		Token token = demoService.findToken(securityCode);
		assertTrue(token.isValid());
		demoService.Invalidate(securityCode);
		assertFalse(token.isValid());
	}

	@Test
	void checkRoleNormal() {
		demoService.createUser(usernameList.get(0), passwordList.get(0));
		demoService.createRole(roleList.get(0));
		User user = demoService.findUser(usernameList.get(0));
		Role role = demoService.findRole(roleList.get(0));
		demoService.addRoleToUser(user, role);
		String securityCode = demoService.authenticate(usernameList.get(0), passwordList.get(0));
		assertTrue(demoService.findToken(securityCode).isValid());
		assertTrue(demoService.findUser(usernameList.get(0)).hasRole(role));
		assertTrue(demoService.checkRole(securityCode, role));
	}

	@Test
	void checkRoleFalse() {
		demoService.createUser(usernameList.get(0), passwordList.get(0));
		demoService.createRole(roleList.get(0));
		User user = demoService.findUser(usernameList.get(0));
		Role role = demoService.findRole(roleList.get(0));
		String securityCode = demoService.authenticate(usernameList.get(0), passwordList.get(0));
		assertTrue(demoService.findToken(securityCode).isValid());
		assertFalse(user.hasRole(role));
		assertFalse(demoService.checkRole(securityCode, role));
	}

	@Test
	void checkRoleInvalidToken() {
		assertThrows(IllegalArgumentException.class, () -> {
			demoService.createUser(usernameList.get(0), passwordList.get(0));
			demoService.createRole(roleList.get(0));
			User user = demoService.findUser(usernameList.get(0));
			Role role = demoService.findRole(roleList.get(0));
			demoService.addRoleToUser(user, role);
			String securityCode = demoService.authenticate(usernameList.get(0), passwordList.get(0));
			Token token = demoService.findToken(securityCode);
			demoService.Invalidate(securityCode);
			assertFalse(token.isValid());
			demoService.checkRole(securityCode, role);
		});
	}

	@Test
	void allRolesNormal() {
		demoService.createUser(usernameList.get(0), passwordList.get(0));
		demoService.createRole(roleList.get(0));
		demoService.createRole(roleList.get(1));
		demoService.createRole(roleList.get(2));
		demoService.createRole(roleList.get(3));
		User user = demoService.findUser(usernameList.get(0));
		Role role0 = demoService.findRole(roleList.get(0));
		Role role1 = demoService.findRole(roleList.get(1));
		Role role2 = demoService.findRole(roleList.get(2));
		Role role3 = demoService.findRole(roleList.get(3));
		demoService.addRoleToUser(user, role0);
		demoService.addRoleToUser(user, role1);
		demoService.addRoleToUser(user, role2);
		String securityCode = demoService.authenticate(usernameList.get(0), passwordList.get(0));
		Token token = demoService.findToken(securityCode);
		assertTrue(token.isValid());
		List<Role> allRoles = demoService.allRoles(securityCode);
		assertTrue(allRoles.contains(role0));
		assertTrue(allRoles.contains(role1));
		assertTrue(allRoles.contains(role2));
		assertFalse(allRoles.contains(role3));
	}

	@Test
	void tokenExpiry() throws InterruptedException {
		Token.setValidPeriod(1000);
		demoService.createUser(usernameList.get(0), passwordList.get(0));
		String securityCode = demoService.authenticate(usernameList.get(0), passwordList.get(0));
		assertNotNull(securityCode);
		Token token = demoService.findToken(securityCode);
		assertTrue(token.isValid());
		TimeUnit.MILLISECONDS.sleep(1200);
		assertFalse(token.isValid());
		Token.resetValidPeriod();
	}
}
