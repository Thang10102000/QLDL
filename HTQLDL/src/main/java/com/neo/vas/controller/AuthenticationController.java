/**
 * 
 */
package com.neo.vas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.neo.vas.domain.Users;
import com.neo.vas.repository.UsersRepository;

/**
 * @author KhanhBQ
 *
 */
@Controller
public class AuthenticationController {
	@Autowired
	private UsersRepository userRepo;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/admin/register/first")
	public String register() {
		return "register";
	}

	@PostMapping("/register")
	public String register2(@RequestParam String username, String password) {
		Users newUser = new Users();
		newUser.setUsername(username);
		newUser.setAuthorized(1);
		newUser.setStatus(1);
		newUser.setPasswordNeverExpired(1);
		String encodedPassword = bCryptPasswordEncoder.encode(password);
		newUser.setPassword(encodedPassword);
		userRepo.saveAndFlush(newUser);
		return "redirect:/login";
	}
}
