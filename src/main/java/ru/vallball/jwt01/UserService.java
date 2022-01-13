package ru.vallball.jwt01;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {


	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();;
	public List<User> users = generateUsers();

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		for (User u : users) {
			System.out.println(u);
			if (u.getLogin().equals(username)) {
				return u;
			}
		}
		return null;
	}

	public List<User> generateUsers() {
		List<User> list = new ArrayList<>();
		User admin = new User();
		admin.setLogin("admin");
		admin.setPassword("admin");
		admin.setRole(Role.ROLE_ADMIN);
		list.add(admin);
		User user = new User();
		user.setLogin("user");
		user.setPassword("user");
		user.setRole(Role.ROLE_USER);
		list.add(user);
		return list;
	}

	public void save(User user) {
		System.out.println("user.getLogin() = " + user.getLogin());
		System.out.println(user.getPassword());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole(Role.ROLE_USER);
		users.add(user);
		for (User u : users) {
			System.out.println("yuuu " + u);
		}
	}

	public List<User> list() {
		return users;
	}

	public void delete(String login) {
		int answer = -1;
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getLogin().equals(login)) {
				answer = i;
				break;
			}
		}
		if (answer > 0) {
			users.remove(answer);
		}
	}

	public User findByLoginAndPassword(String login, String password) {
		User userEntity = (User) loadUserByUsername(login);
		if (userEntity != null) {
			if (passwordEncoder.matches(password, userEntity.getPassword())) {
				return userEntity;
			}
		}
		return null;
	}
}
