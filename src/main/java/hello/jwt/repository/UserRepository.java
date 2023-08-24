package hello.jwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import hello.jwt.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	// @EntityGraph는  Lazy(지연로딩)조회가 아니고 Eager(즉시로딩)조회로 authorities 정보를 가져오게 한다
	@EntityGraph(attributePaths = "authorities")
	Optional<User> findOneWithAuthoritiesByUsername(String username);

}
