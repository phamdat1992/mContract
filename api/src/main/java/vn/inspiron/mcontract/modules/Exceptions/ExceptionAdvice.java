package vn.inspiron.mcontract.modules.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class ExceptionAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequest.class)
    public ResponseEntity<ErrorResponse> badRequest() {
        ErrorResponse exception = new ErrorResponse();
        exception.setStatus(400);
        exception.setErrorCode("BAD_REQUEST");
        exception.setErrorMsg("Bad request");
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(Unauthorized.class)
    public ResponseEntity<ErrorResponse> unauthorized() {
        ErrorResponse exception = new ErrorResponse();
        exception.setStatus(401);
        exception.setErrorCode("UNAUTHORIZED");
        exception.setErrorMsg("You are not allowed to access this.");
        return new ResponseEntity<>(exception, HttpStatus.UNAUTHORIZED);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> notFound0() {
        ErrorResponse exception = new ErrorResponse();
        exception.setStatus(404);
        exception.setErrorCode("NOT_FOUND");
        exception.setErrorMsg("Request not found on this server.");
        return new ResponseEntity<>(exception, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFound.class)
    public ResponseEntity<ErrorResponse> notFound1() {
        ErrorResponse exception = new ErrorResponse();
        exception.setStatus(404);
        exception.setErrorCode("NOT_FOUND");
        exception.setErrorMsg("Request not found on this server.");
        return new ResponseEntity<>(exception, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidToken.class)
    public ResponseEntity<ErrorResponse> invalidToken() {
        ErrorResponse exception = new ErrorResponse();
        exception.setStatus(404);
        exception.setErrorCode("INVALID_TOKEN");
        exception.setErrorMsg("The token is invalid. Please try again.");
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TokenExpired.class)
    public ResponseEntity<ErrorResponse> tokenExpired() {
        ErrorResponse exception = new ErrorResponse();
        exception.setStatus(404);
        exception.setErrorCode("TOKEN_EXPIRED");
        exception.setErrorMsg("The token has already expired. Please try again.");
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }

}
