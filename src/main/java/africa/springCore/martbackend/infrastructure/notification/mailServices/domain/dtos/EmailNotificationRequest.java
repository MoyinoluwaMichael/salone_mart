package africa.springCore.martbackend.infrastructure.notification.mailServices.domain.dtos;

import africa.springCore.martbackend.infrastructure.notification.mailServices.domain.data.Recipient;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EmailNotificationRequest {
	private List<Recipient> to = new ArrayList<> ();
	private String subject;
	private String text;
	private String htmlContent;
}
