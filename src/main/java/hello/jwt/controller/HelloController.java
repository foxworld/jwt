package hello.jwt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
public class HelloController {
	
	@GetMapping("/hello")	
	public ResponseEntity<String> hello() {
		log.info("hello jwt!!!");
		return ResponseEntity.ok("hello");
		
	}

	@GetMapping("/reject")	
	public ResponseEntity<String> helloReject() {
		log.info("hello reject!!!");
		return ResponseEntity.ok("hello");
		
	}

	
}
