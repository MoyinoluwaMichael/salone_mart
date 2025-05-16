package africa.techimmortal.martbackend.infrastructure.notification.mailServices.service;

import africa.techimmortal.martbackend.core.domain.dtos.response.ApiResponse;
import africa.techimmortal.martbackend.infrastructure.notification.mailServices.domain.dtos.EmailNotificationRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import static africa.techimmortal.martbackend.core.utils.Message.MAIL_HAS_BEEN_SENT_SUCCESSFULLY;
import static africa.techimmortal.martbackend.core.utils.Message.apiResponse;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
	private final JavaMailSender mailSender;
	@Value("${spring.mail.username}")
	private String sender;


	@Override
	public ApiResponse sendMail(EmailNotificationRequest emailNotificationRequest) throws MessagingException {
		String email = emailNotificationRequest.getTo().get(0).getEmail();

		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(email);
		mailMessage.setFrom(sender);
		mailMessage.setSubject(emailNotificationRequest.getSubject());
		mailMessage.setText(emailNotificationRequest.getText());

		mailSender.send(mailMessage);
		return apiResponse(MAIL_HAS_BEEN_SENT_SUCCESSFULLY);
	}

	@Override
	public ApiResponse sendHtmlMail(EmailNotificationRequest emailNotificationRequest) throws MessagingException {
		String email = emailNotificationRequest.getTo().get(0).getEmail();

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setTo(email);
		helper.setFrom(sender);
		helper.setSubject(emailNotificationRequest.getSubject());
		helper.setText(emailNotificationRequest.getText(), true);

		mailSender.send(message);
		return apiResponse(MAIL_HAS_BEEN_SENT_SUCCESSFULLY);
	}
}
