package africa.springCore.martbackend.core.utils;

import africa.springCore.martbackend.core.domain.dtos.response.ApiResponse;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

public class Message {
	
	public static final String CREATED = "Account has been created successfully";
	public static final String MAIL_HAS_BEEN_SENT_SUCCESSFULLY = "Mail has been sent successfully";
	public static final String TOKEN_HAS_BEEN_VERIFIED_SUCCESSFULLY = "Token has been verified successfully";
	public static final String SENT = "Mail has been sent successfully";
	public static final String FAILED_TO_GET_ACTIVATION_LINK="Failed to get activation link";
	public static final String EMAIL_ALREADY_EXIST = "Email has already been used";
	public static final String CAMPUS_ALREADY_EXISTS = "Campus already exists";
	public static final String CAMPUS_WITH_NAME_ALREADY_EXISTS = "Campus with name %s already exists";
	public static final String CAMPUS_NOT_FOUND = "Campus not found";
	public static final String LOGIN_FAIL = "invalid login details";
	public static final String USER_NOT_FOUND = "User not found";
	public static final String INVALID_TOKEN = "Invalid token";
	public static final String USER_WITH_EMAIL_NOT_FOUND = "User with email %s not found";
	public static final String USER_WITH_PHONE_NUMBER_NOT_FOUND = "User with phone number %s not found";
	public static final String USER_WITH_EMAIL_ALREADY_EXISTS = "User with email %s already exists";
	public static final String USER_WITH_PHONE_NUMBER_ALREADY_EXISTS = "User with phone number %s already exists";
	public static final String ADMIN_WITH_EMAIL_ALREADY_EXISTS = "Admin with email %s already exists";
	public static final String ADMIN_WITH_PHONE_NUMBER_ALREADY_EXISTS = "Admin with phone number %s already exists";
	public static final String USER_WITH_ID_NOT_FOUND = "User with id %s not found";
	public static final String ORDER_WITH_ID_NOT_FOUND = "Order with id %s not found";
	public static final String ADMIN_WITH_ID_NOT_FOUND = "Admin with id %s not found";
	public static final String MINISTER_WITH_ID_NOT_FOUND = "Minister with id %s not found";
	public static final String DEPARTMENT_WITH_ID_NOT_FOUND = "Department with id %s not found";
	public static final String PRODUCT_CATEGORY_WITH_ID_NOT_FOUND = "Product category with id %s not found";
	public static final String PRODUCT_CATEGORY_WITH_NAME_NOT_FOUND = "Product category with name %s not found";
	public static final String PRODUCT_WITH_ID_NOT_FOUND = "Product with id %s not found";
	public static final String PRODUCT_WITH_ID_NOT_OWNED_BY_VENDOR = "Product with id %s not owned by vendor with id %s";
	public static final String CUSTOMER_WITH_EMAIL_ALREADY_EXISTS = "Customer with email %s already exists";
	public static final String USER_ADDRESS_WITH_ID_NOT_FOUND= "%s address with id %s not found";
	public static final String VENDOR_WITH_EMAIL_ALREADY_EXISTS = "Vendor with email %s already exists";
	public static final String CUSTOMER_WITH_PHONE_NUMBER_ALREADY_EXISTS = "Customer with phone number %s already exists";
	public static final String VENDOR_WITH_PHONE_NUMBER_ALREADY_EXISTS = "Vendor with phone number %s already exists";
	public static final String ACC_VERIFY_FAILURE = "account verification failed";
	public static final String PASSWORDS_DO_NOT_MATCH = "Passwords do not match";
	public static final String ACCOUNT_ALREADY_EXIST = "Account with email %s already exist";
	public static final String CAMPUS_WITH_ID_NOT_FOUND = "Campus with ID %s not found";
	public static final String CAMPUS_WITH_NAME_NOT_FOUND = "Campus with name %s not found";
	public static final String AUTHENTICATION_FAILED_FOR_USER_WITH_EMAIL = "Authentication failed for User with email %s";
	public static final String INVALID_EMAIL_OR_PASSWORD = "Invalid email or password";
	public static final String EVENT_ALREADY_CREATED = "An Event Already Exist With This Name";
	public static final String EVENT_CREATED_SUCCESSFULLY = "You have successfully created an event";
	public static final String NO_EVENT_FOUND = "Sorry, we couldn't find an event with such name";
	public static final String EVENT_UPDATED_SUCCESSFULLY ="Event has been updated successfully" ;
	public static final String EVENT_DELETED_SUCCESSFULLY = "Event deleted successfully";



	public static ApiResponse apiResponse(String message){
		return ApiResponse.builder()
				.message (message)
				.success (true)
				.statusCode (HttpStatus.OK.value ())
				.build();
	}

	public static ApiResponse apiResponse(BigDecimal amount){
		return ApiResponse.builder()
				.message ("Total order amount calculated successfully")
				.totalOrderAmount (amount)
				.success (true)
				.statusCode (HttpStatus.OK.value ())
				.build();
	}
}
