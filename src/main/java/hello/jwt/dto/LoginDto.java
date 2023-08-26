package hello.jwt.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
	@NotNull
	@Size(min=3, max=50)
	private String username;
	
	@NotNull
	@Size(min=3, max=100)
	private String password;

	@NotNull
	@Size(min=8, max=8)
	private String targetdate;

	@NotNull
	@Size(min=5, max=5)
	private String datatype;
	
	@NotNull
	@Size(min=4, max=4)
	private String sendrecv;

	@NotNull
	@Size(min=0, max=10)
	private String filetype;
	
}
