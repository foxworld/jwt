package hello.jwt.util;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SecurityUtil {
	
	private SecurityUtil() {
		
	}
	
	public static Optional<String> getCurrentUsername() {
		// authentication 값은 doFilter에 들어오는 시점에 저장한것을 가져온다
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(authentication == null) {
			log.error("Security Context에 인증정보가 없습니다");
			return Optional.empty();
		}
		String username = null;
		if(authentication.getPrincipal() instanceof UserDetails) {
			UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
			username = springSecurityUser.getUsername();
		} else if(authentication.getPrincipal() instanceof String) {
			username = (String) authentication.getPrincipal();
		}
		return Optional.ofNullable(username);
	}
	
	

}
