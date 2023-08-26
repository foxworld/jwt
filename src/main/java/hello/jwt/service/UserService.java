package hello.jwt.service;


import java.util.Collections;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.jwt.domain.Authority;
import hello.jwt.domain.User;
import hello.jwt.dto.UserDto;
import hello.jwt.repository.UserRepository;
import hello.jwt.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Transactional
	public User signup(UserDto userDto) {
		if(userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) {
			throw new RuntimeException("이미 가입되어 있는 유저입니다.");
		}
		
		Authority authority = Authority.builder()
				.authorityName("ROLE_USER")
				.build();
		
		User user = User.builder()
				.username(userDto.getUsername())
				.password(passwordEncoder.encode(userDto.getPassword()))
				.nickname(userDto.getNickname())
				.authorities(Collections.singleton(authority))
				.activated(true)
				.build();
		
		log.info("user={}", user.toString());
		
		return userRepository.save(user);
	}
	
	@Transactional(readOnly = true)
	public Optional<User> getUserWithAuthorities(String username) {
		log.info("getUserWithAuthorities={}", username);
		return userRepository.findOneWithAuthoritiesByUsername(username);
	}
	
	@Transactional(readOnly = true)
	public Optional<User> getMyUserWithAuthorities() {
		log.info("getUserWithAuthorities:admin");
		return SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername);
		
	}
}
