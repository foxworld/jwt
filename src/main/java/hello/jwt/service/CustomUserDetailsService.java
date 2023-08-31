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
		
		User user1 = new User();
		
		user1 = userRepository.findOneWithAuthoritiesByUsername(username).get();
		log.debug("LHK:userDetails={}", createUser(username, user1));
		
		return userRepository.findOneWithAuthoritiesByUsername(username)
				.map(user -> createUser(username, user))
				.orElseThrow(()-> new UsernameNotFoundException(username+" 을 찾을 수 없습니다."));
	}

	// token 안에있는 값 만들기
	private org.springframework.security.core.userdetails.User createUser(String username, User user) {
		//log.info("LHK:createUser:User={}", user.toString());
		if(!user.isActivated()) {
			throw new RuntimeException(username+" -> 활성화되어 있지 않습니다.");
		}
			
		List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
				.map(Authority -> new SimpleGrantedAuthority(Authority.getAuthorityName()))
				.collect(Collectors.toList());

		log.debug("LHK:createUser:grantedAuthorities={},{}", grantedAuthorities, user.getAuthorities().stream().toArray());
		log.debug("LHK:createUser:grantedAuthorities={},{}", grantedAuthorities, user.getAuthorities().stream().toArray());
		
		org.springframework.security.core.userdetails.User userDetailsUser  
		= new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
		
		log.debug("LHK:createUser:userDetails={}", userDetailsUser.toString());
		return userDetailsUser;
	}

}
