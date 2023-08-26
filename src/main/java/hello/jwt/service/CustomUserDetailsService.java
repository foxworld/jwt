package hello.jwt.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import hello.jwt.domain.User;
import hello.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{
	private final UserRepository userRepository;
	
//	public CustomUserDetailsService(UserRepository userRepository) {
//		this.userRepository = userRepository;
//	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("loadUserByUsername={}", username);
		return userRepository.findOneWithAuthoritiesByUsername(username)
				.map(user -> createUser(username, user))
				.orElseThrow(()-> new UsernameNotFoundException(username+" 을 찾을 수 없습니다."));
	}

	private org.springframework.security.core.userdetails.User createUser(String username, User user) {
		log.info("createUser  User={}", user.toString());
		if(!user.isActivated()) {
			throw new RuntimeException(username+" -> 활성화되어 있지 않습니다.");
		}
		List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
				.map(Authority -> new SimpleGrantedAuthority(Authority.getAuthorityName()))
				.collect(Collectors.toList());
		return new org.springframework.security.core.userdetails.User(user.getUsername(),
				user.getPassword(),
				grantedAuthorities);
	}

}
