package hello.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import hello.jwt.jwt.JwtAccessDeniedHandler;
import hello.jwt.jwt.JwtAuthenticationEntryPoint;
import hello.jwt.jwt.JwtSecurityConfig;
import hello.jwt.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final TokenProvider tokenProvider;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

//	public SecurityConfig(TokenProvider tokenProvider, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
//			JwtAccessDeniedHandler jwtAccessDeniedHandler) {
//		this.tokenProvider = tokenProvider;
//		this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
//		this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
//	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring()
				.requestMatchers(new AntPathRequestMatcher("/h2-console/**"))
				//.requestMatchers(PathRequest.toH2Console())
	            .requestMatchers(new AntPathRequestMatcher("/favicon.ico"));
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	protected SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable);

		// 인증에 대해한 설정
		http.exceptionHandling(authenticationManager -> authenticationManager
					.authenticationEntryPoint(jwtAuthenticationEntryPoint)
					.accessDeniedHandler(jwtAccessDeniedHandler));

		http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));
		
		// Session 사용하지 않게 설정
		http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		// 인증없이 허용 URL 설정
		http.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/api/hello").permitAll() // 해당 URL만 접근가능하고
			.requestMatchers("/api/authenticate").permitAll() // 토근을 받기위해 허용
				.requestMatchers("/api/signup").permitAll() // 회원가입을 받기위해 허용
			.requestMatchers("/error").permitAll()
			.anyRequest().authenticated()); // 나머지는 인증절차에 따라 접근가능하도록한다
		
		// JwtFilter를 addFilterBefore로 등록했던 JwtSecurityConfig 적용
		http.apply(new JwtSecurityConfig(tokenProvider));

		return http.build();
	}

}
