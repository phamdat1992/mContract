package vn.amitgroup.digitalsignatureapi.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ErrorCodeException extends RuntimeException {
    private int code;
    private String errorCode;
    private int status = 400;

    public ErrorCodeException(String message, int code,String errorCode) {
        super(message);
        this.code = code;
        this.errorCode= errorCode;
    }

    public static ErrorCodeException GetCodeExpired()
    {
        return new ErrorCodeException("Bad credentials", HttpStatus.BAD_REQUEST.value(),"CODE_EXPIRED");
    }
    public static ErrorCodeException NullException()
    {
        return new ErrorCodeException("No resource", HttpStatus.NOT_FOUND.value(),"NO_RESOURCE");
    }
    public static ErrorCodeException AccountException()
    {
        return new ErrorCodeException("Account already exist", HttpStatus.NOT_FOUND.value(),"USER_EXIST");
    }
    public static ErrorCodeException AccountNotFoundException()
    {
        return new ErrorCodeException("Account not found", HttpStatus.NOT_FOUND.value(),"ACCOUNT_NOTFOUND");
    }
}
