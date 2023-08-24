package hello.jwt.jwt;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {
	private static final String AUTHORIZATION_HEADER = "Authorization";
	private final TokenProvider tokenProvider;
	
//	public JwtFilter(TokenProvider tokenProvider) {
//		this.tokenProvider = tokenProvider;
//	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String jwt = resolveToken(httpServletRequest);
		String requestURI = httpServletRequest.getRequestURI(); //URI: Uniform Resource Identifier
		
		if(StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
			Authentication authentication = tokenProvider.getAuthentication(jwt);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			log.debug("Security Context에 '{}' 인증 정보를 저정했습니다, uri:{}", authentication.getName(), requestURI);
		}else {
			log.error("유효한 JWT 토큰이 없습니다, url:{}", requestURI);
		}
		
		chain.doFilter(request, response);
	}
	
	// Request Header에서 토큰정보를 가져온다
	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
		if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
	
	

}
