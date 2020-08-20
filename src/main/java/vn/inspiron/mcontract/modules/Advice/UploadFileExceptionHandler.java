package vn.inspiron.mcontract.modules.Advice;

import java.util.HashMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

@RestControllerAdvice
public class UploadFileExceptionHandler {
    @ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<Object> catchUploadFileTooLarge(MaxUploadSizeExceededException e) {
		HashMap<String, Object> responseBody = new HashMap<String, Object>();
		responseBody.put("status", HttpStatus.BAD_REQUEST);
		responseBody.put("message", "file is too large");
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
	}
	
	@ExceptionHandler(MultipartException.class)
	public ResponseEntity<Object> catchUploadDeletedFile(MultipartException e) {
		HashMap<String, Object> responseBody = new HashMap<String, Object>();
		responseBody.put("status", HttpStatus.BAD_REQUEST);
		responseBody.put("message", "file not found");
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
	}
}