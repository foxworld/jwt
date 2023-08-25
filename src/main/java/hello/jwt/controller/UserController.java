package hello.jwt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.jwt.domain.User;
import hello.jwt.dto.UserDto;
import hello.jwt.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
	
	@PostMapping("/signup")
	public ResponseEntity<User> signup(@Valid @RequestBody UserDto userDto) {
		log.info("userDto={}", userDto.toString());
		return ResponseEntity.ok(userService.signup(userDto));
	}
	
	@GetMapping("/user")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<User> getMyUserInfo() {
		log.info("userService.getMyUserWithAuthorities().get()={}", userService.getMyUserWithAuthorities().get().getUsername());
		return ResponseEntity.ok(userService.getMyUserWithAuthorities().get());
	}
	
	@GetMapping("/user/{username}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<User> getUserInfo(@PathVariable String username) {
		log.info("username={}", username);
		return ResponseEntity.ok(userService.getUserWithAuthorities(username).get());
	}
}
