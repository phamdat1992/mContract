package vn.amitgroup.digitalsignatureapi.exception;

import lombok.Getter;
import lombok.Setter;
import vn.amitgroup.digitalsignatureapi.dto.response.ERROR;
@Getter
@Setter
public class ApiException extends RuntimeException {
    private int code;

    public ApiException(String message, int code) {
        super(message);
        this.code = code;
    }
    public ApiException(Exception e) {
        super(e.getMessage());
        this.code = 404;
    }



    public ApiException(ERROR error){
        super(error.getMessage());
        this.code =  error.getCode();
    }
    public ApiException(ERROR error, String message){
        super(message);
        this.code =  error.getCode();
    }
}
