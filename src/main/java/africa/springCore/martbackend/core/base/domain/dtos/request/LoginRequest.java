package africa.springCore.martbackend.core.base.domain.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
	@NotNull(message = "Field email must not be null")
	@Email
	private String email;
	@NotNull(message = "Field password must not be null")
	private String password;
}
