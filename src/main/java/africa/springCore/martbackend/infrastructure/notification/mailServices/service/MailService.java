package africa.springCore.martbackend.infrastructure.notification.mailServices.service;


import africa.springCore.martbackend.common.data.ApiResponse;
import africa.springCore.martbackend.infrastructure.notification.mailServices.domain.dtos.EmailNotificationRequest;
import jakarta.mail.MessagingException;
public interface MailService {
	
	ApiResponse sendMail(EmailNotificationRequest emailNotificationRequest) throws MessagingException;
	ApiResponse sendHtmlMail(EmailNotificationRequest emailNotificationRequest) throws MessagingException;
}
