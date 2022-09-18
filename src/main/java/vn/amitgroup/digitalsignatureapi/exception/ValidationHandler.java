package vn.amitgroup.digitalsignatureapi.exception;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.validation.ConstraintViolationException;


import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.MessagingException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import vn.amitgroup.digitalsignatureapi.dto.response.BaseResponse;

@RestControllerAdvice
@Slf4j
public class ValidationHandler{
	// @ExceptionHandler(MethodArgumentNotValidException.class)
    // @ResponseBody
    // @ResponseStatus
	// protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
	// 		org.springframework.http.HttpHeaders headers, HttpStatus status, WebRequest request) {
			
	// 			Map<String, String> errors = new HashMap<>();
	// 			ex.getBindingResult().getAllErrors().forEach((error) ->{
					
	// 				String fieldName = ((FieldError) error).getField();
	// 				String message = error.getDefaultMessage();
	// 				errors.put(fieldName, message);
	// 			});
	// 			return new ResponseEntity<Object>(errors, HttpStatus.BAD_REQUEST);
	// }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<String> handleMethodArgumentNotValid(MethodArgumentNotValidException e,WebRequest request) {
    	log.error(e.getMessage(), e);
        return  new BaseResponse<>(e.getMessage(),"Dữ liệu không hợp lệ",HttpStatus.BAD_REQUEST.value(),"OTP_FAILED");
    }
	@ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse<String> handleConstraintViolationException(ConstraintViolationException e,WebRequest request) {
    	log.error(e.getMessage(), e);
        return  new BaseResponse<>(e.getMessage(),null,HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
	@ExceptionHandler(NotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BaseResponse<String> handleResourceNotFoundException(NotFoundException e,WebRequest request) {
        log.error(e.getMessage(), e);
        return  new BaseResponse<>(e.getMessage(),null,HttpStatus.NOT_FOUND.value());
    }
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse<String> handleAllExceptions(Exception e,WebRequest request) {
        log.error(e.getMessage(), e);
        return  new BaseResponse<>(e.getMessage(),"Lỗi chưa xác định",HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public BaseResponse<String> handleAccessDeniedException(AccessDeniedException e,WebRequest request) {
        log.error(e.getMessage(), e);
        return  new BaseResponse<>(e.getMessage(),"Sai xác thực",HttpStatus.FORBIDDEN.value());
    }
    @ExceptionHandler(TokenRefreshException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public BaseResponse<String> handleAccessDeniedException(TokenRefreshException e,WebRequest request) {
        log.error(e.getMessage(), e);
        return  new BaseResponse<>(e.getMessage(),"Sai xác thực",HttpStatus.FORBIDDEN.value());
    }
    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<String> handleAuthenticationException(AuthenticationException e,WebRequest request) {
        log.error(e.getMessage(), e);
        return  new BaseResponse<>(e.getMessage(),"Sai xác thực",HttpStatus.BAD_REQUEST.value());
    }
    
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<String> badCredentialsException(BadCredentialsException e,WebRequest request) {
        log.error(e.getMessage(), e);
        return  new BaseResponse<>(e.getMessage(),"Sai xác thực",HttpStatus.BAD_REQUEST.value(),"OTP_FAILED");
    }
    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public BaseResponse<String> expiredJwtException(ExpiredJwtException e,WebRequest request) {
        log.error(e.getMessage(), e);
        return  new BaseResponse<>(e.getMessage(),"Sai xác thực",HttpStatus.UNAUTHORIZED.value());
    }
    @ExceptionHandler(MessagingException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BaseResponse<String> handleAuthenticationException(MessagingException e,WebRequest request) {
        log.error(e.getMessage(), e);
        return  new BaseResponse<>(e.getMessage(),"Lỗi gửi mail",HttpStatus.NOT_FOUND.value());
    }
    @ExceptionHandler(IOException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BaseResponse<String> handleAuthenticationException(IOException e,WebRequest request) {
        log.error(e.getMessage(), e);
        return  new BaseResponse<>(e.getMessage(),"Không đọc được file",HttpStatus.NOT_FOUND.value(),"NOT_READ_FILE");
    }
    @ExceptionHandler(FileNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BaseResponse<String> fileNotFoundException(FileNotFoundException e,WebRequest request) {
        log.error(e.getMessage(), e);
        return  new BaseResponse<>(e.getMessage(),"Không tìm thấy file",HttpStatus.NOT_FOUND.value(),"NOT_FILE");
    }
    
    @ExceptionHandler(ApiException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BaseResponse<String> handleCustomizedException(ApiException e) {
        log.info(e.getMessage(), e);
        return  new BaseResponse<>(null,e.getMessage(),HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(ErrorCodeException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<String> handleExpiredLoginException(ErrorCodeException e) {
        log.info(e.getMessage(), e);
        return  new BaseResponse<>(null,e.getMessage(),e.getStatus(),e.getErrorCode());
    }
}
