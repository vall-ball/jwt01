package ru.vallball.jwt01;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
	@Autowired
	private UserService userService;
	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping("/register")
	public String registerUser(@RequestBody @Valid RegistrationRequest registrationRequest) {
		User u = new User();
		u.setPassword(registrationRequest.getPassword());
		System.out.println(registrationRequest.getPassword());
		u.setLogin(registrationRequest.getLogin());
		System.out.println(registrationRequest.getLogin());
		userService.save(u);
		return "OK";
	}

	@PostMapping("/auth")
	public AuthResponse auth(@RequestBody AuthRequest request) {
		User user = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
		String token = jwtUtil.generateToken(user.getLogin());
		return new AuthResponse(token);
	}
}