package africa.springCore.martbackend.infrastructure.notification.mailServices.domain.data;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Recipient {
	private String email;
}
